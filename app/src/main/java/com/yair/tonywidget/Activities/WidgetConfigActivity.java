package com.yair.tonywidget.Activities;

import android.app.Dialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.yair.tonywidget.Adapters.ContactsListAdapter;
import com.yair.tonywidget.Dialogs.WidgetCreationDialog;
import com.yair.tonywidget.General.AppConsts;
import com.yair.tonywidget.General.ContactWidget;
import com.yair.tonywidget.Helpers.BitmapHelper;
import com.yair.tonywidget.Helpers.ContextHelper;
import com.yair.tonywidget.Helpers.DatabaseHelper;
import com.yair.tonywidget.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.yair.tonywidget.General.AppConsts.PROJECTION;
import static com.yair.tonywidget.General.AppConsts.SELECTION;

/**
 * Created by yair on 20/12/17.
 */

public class WidgetConfigActivity extends AppCompatActivity implements View.OnClickListener,
                                                                       AdapterView.OnItemClickListener,
                                                                       WidgetCreationDialog.NoticeWCDListener
{
    // UI components
    private EditText    _etSearchContactName;
    private ImageView   _ivSearchContactIcon;
    private ListView    _lvContacts;

    // Class Members
    private ArrayList<ContactWidget>    _lsContactWidgets;
    private ContactsListAdapter         _adapter;
    private int                         _nWidgetId;
    private Bitmap                      _image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        ContextHelper.setContext(this);

        _lsContactWidgets = new ArrayList<>();
        _adapter    = new ContactsListAdapter(_lsContactWidgets);

        // Initialize the UI components
        initComponents();
        initListeners();

        // Getting the intent started the activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        // Checking if there are extras
        if (extras != null)
        {
            _nWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                       AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }

    /**
     * Initializes the class' UI components
     */
    public void initComponents()
    {
        // Initializing the contact-search EditText
        _etSearchContactName = (EditText) findViewById(R.id.etContactNameToSearch);

        // Initializing the contact-search ImageView
        _ivSearchContactIcon = (ImageView) findViewById(R.id.ivSearch);

        // Initializing the contact ListView
        _lvContacts = (ListView) findViewById(R.id.lvContacts);
        _lvContacts.setAdapter(_adapter);
    }

    /**
     * Initializes the class' components' listeners
     */
    public void initListeners()
    {
        // Initializing the contact-search ImageView OnClick listener
        _ivSearchContactIcon.setOnClickListener(this);

        // Making the contact search available through 'Enter' click
        _etSearchContactName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean bWasActionHandled = false;

                // Checking if the user clicked Enter
                if (i == EditorInfo.IME_NULL)
                {
                    // Search them contacts
                    searchContacts();

                    bWasActionHandled = true;
                }

                return (bWasActionHandled);
            }
        });

        // Initializing the list item click listener
        _lvContacts.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        // Checking if the view triggered the listener is the contact-search ImageView
        if (view.getId() == _ivSearchContactIcon.getId())
        {
            // Search them contacts
            searchContacts();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        // Checking if the ListView triggered the listener is the contacts ListView
        if (adapterView.getId() == _lvContacts.getId())
        {
            // Open the Widget creation dialog
            WidgetCreationDialog wcdDialog = new WidgetCreationDialog(this,
                                                                       _lsContactWidgets.get(i),
                                                                      this);
            wcdDialog.show();
        }
    }

    /**
     * Searching the contacts using the text given in the contact-search EditText
     * and displaying the results in the contacts ListView
     */
    public void searchContacts()
    {
        // Getting the contact name to search
        String stNameToSearch = _etSearchContactName.getText().toString();


        ContentResolver cr = getContentResolver();
        Cursor cursorContacts = cr.query(ContactsContract.Contacts.CONTENT_URI,
                                         PROJECTION,
                                         SELECTION,
                                         new String[] {"%" + stNameToSearch + "%"},
                                        null);

        // Checking if we received the contacts
        if ((cursorContacts != null) && (cursorContacts.getCount() > 0))
        {
            String stContactId;
            String stContactName;
            String stContactNumber;
            Bitmap bitmapContactIcon;

            // Clear the contacts list
            _lsContactWidgets.clear();

            // Going all over the contacts cursor
            while ((cursorContacts != null) && (cursorContacts.moveToNext()))
            {
                // Initialize the contact's details
                stContactId = cursorContacts.getString(cursorContacts.getColumnIndex(
                                                        ContactsContract.Contacts._ID));
                stContactName = cursorContacts.getString(cursorContacts.getColumnIndex(
                                                        ContactsContract.Contacts.DISPLAY_NAME));
                stContactNumber = null;
                bitmapContactIcon = BitmapFactory.decodeResource(getResources(), R.drawable.unknown_contact_icon_48px);

                // Checking if the current contact has a phone number
                if (cursorContacts.getInt(cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorContactNumber = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{stContactId},
                            null);

                    cursorContactNumber.moveToFirst();

                    // Getting the contact's phone number
                    stContactNumber = cursorContactNumber.getString(cursorContactNumber.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));

                    cursorContactNumber.close();
                }

                try
                {
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(stContactId)));

                    // Checking if the InputStream object was successfully initialized
                    if (inputStream != null)
                    {
                        bitmapContactIcon = BitmapFactory.decodeStream(inputStream);

                        inputStream.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }


                // Add the new ContactWidget to the list
                _lsContactWidgets.add(new ContactWidget(_nWidgetId,
                                                        stContactName,
                                                        stContactNumber,
                                                        BitmapHelper.CircleBitmap(bitmapContactIcon)));
            }

            cursorContacts.close();

            // Update the ListView
            _adapter.notifyDataSetChanged();
        }
        // No contacts were found
        else
        {
            Toast.makeText(this, this.getString(R.string.toast_contacts_search_failed), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case WidgetCreationDialog.SELECTED_PIC:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        _image = BitmapHelper.CircleBitmap(BitmapFactory.decodeStream(imageStream));

                        WidgetCreationDialog.handlerImage.sendMessage(new Message());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * Creates the widget according to the received ContactWidget item
     * @param dialog the creation dialog
     * @param contactWidgetToCreate the ContactWidget to create whe widget by
     */
    @Override
    public void onDialogCreateClick(Dialog dialog, ContactWidget contactWidgetToCreate) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        // Get the layout for the App Widget and attach an on-click listener
        // to the button
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.tonywidget_layout);

        // Setting the contact's icon
        views.setImageViewBitmap(R.id.ivContactImage, contactWidgetToCreate.getIcon());

        // Setting the contact's name
        views.setTextViewText(R.id.tvContactName, contactWidgetToCreate.getName());
        views.setTextColor(R.id.tvContactName, contactWidgetToCreate.getNameColor());

        // Creating an Intent to make the call
        Intent intentMakeCall = new Intent(Intent.ACTION_CALL);
        intentMakeCall.setData(Uri.parse("tel:" + contactWidgetToCreate.getPhoneNumber()));

        views.setOnClickPendingIntent(R.id.layoutWidget, PendingIntent.getActivity(ContextHelper.getContext(),
                0,
                intentMakeCall,
                0));

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(_nWidgetId, views);

        // Insert the ContactWidget object to the DB
        DatabaseHelper.getInstance().addContactWidget(contactWidgetToCreate);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _nWidgetId);

        // Setting the contact's number in the widget
        resultValue.putExtra(AppConsts.stCURR_WIDGET_NUMBER, contactWidgetToCreate.getPhoneNumber());

        setResult(RESULT_OK, resultValue);

        // Dismiss the creation dialog
        dialog.dismiss();

        finish();
    }

    @Override
    public Bitmap onPictureReceived() {
        return (_image);
    }
}
