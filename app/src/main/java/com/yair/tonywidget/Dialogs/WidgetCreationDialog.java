package com.yair.tonywidget.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yair.tonywidget.General.ContactWidget;
import com.yair.tonywidget.Interfaces.IOnDialogClosed;
import com.yair.tonywidget.R;


/**
 * Created by yair on 12/01/18.
 */

public class WidgetCreationDialog extends Dialog implements View.OnClickListener, IOnDialogClosed {
    // UI Components
    private View            _viewNameColor;
    private EditText        _etNameEditor;
    private RelativeLayout  _rlPreview;
    private ImageView       _ivPreviewIcon;
    private TextView        _tvPreviewText;
    private TextView        _tvSetWidget;
    private TextView        _tvCancelWidget;

    // Data Members
    private Activity        _context;
    private ContactWidget   _currContactWidget;
    private int             _currColor;
    private Bitmap          _currIcon;

    public static final int SELECTED_PIC = 1;
    public static Handler handlerImage;

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface NoticeWCDListener
    {
        public void onDialogCreateClick(Dialog dialog, ContactWidget contactWidgetToCreate);
        public Bitmap onPictureReceived();
    }

    public NoticeWCDListener mListener;

    /**
     * C'tor for the class
     * @param context the context (duh)
     * @param contactWidgetToCreate the ContactWidget we're creating the widget by
     * @param listener an interface listening to the dialog's events
     */
    public WidgetCreationDialog(Activity context,
                                ContactWidget contactWidgetToCreate,
                                NoticeWCDListener listener)
    {
        super(context);

        _currContactWidget = contactWidgetToCreate;
        _context = context;
        _currColor = Color.BLACK;
        _currIcon = contactWidgetToCreate.getIcon();
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_widget_creation);

        // Initialize the class' UI components
        initComponents();
        initListeners();

        handlerImage = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                _currIcon = mListener.onPictureReceived();
                Drawable drawable = new BitmapDrawable(_currIcon);

                // Setting the preview icon
                _ivPreviewIcon.setImageBitmap(_currIcon);
            }
        };
    }

    /**
     * Initializes the class' UI components
     */
    public void initComponents()
    {
        // Initializing the the contact-widget's name editor
        _etNameEditor = (EditText) findViewById(R.id.etCWName);
        _etNameEditor.setText(_currContactWidget.getName());

        // Initializing the the contact-widget's name's color Button
        _viewNameColor = (View) findViewById(R.id.viewCWNameColor);
        _viewNameColor.setBackgroundColor(_currColor);

        // Initializing the widget preview Layout
        _rlPreview = (RelativeLayout) findViewById(R.id.layoutPreview);

        // Initializing the widget preview Icon
        _ivPreviewIcon = (ImageView) _rlPreview.findViewById(R.id.ivContactImage);

        // Initializing the widget preview name TextView
        _tvPreviewText = (TextView) _rlPreview.findViewById(R.id.tvContactName);
        _tvPreviewText.setText(_currContactWidget.getName());

        // Initializing the dialog's 'set' TextView
        _tvSetWidget = (TextView) findViewById(R.id.tvCreateWidget);

        // Initializing the dialog's 'cancel' TextView
        _tvCancelWidget = (TextView) findViewById(R.id.tvCancel);
    }

    /**
     * Initializes the class' components' listeners
     */
    public void initListeners()
    {
        // Initializing the contact-widget's name editor listener
        _etNameEditor.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Setting the preview's name TextView with the new inserted text
                _tvPreviewText.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Initializing the the contact-widget's name's color Button click listener
        _viewNameColor.setOnClickListener(this);

        // Initializing the dialog's preview Icon click listener
        _ivPreviewIcon.setOnClickListener(this);

        // Initializing the dialog's 'set' TextView click listener
        _tvSetWidget.setOnClickListener(this);

        // Initializing the dialog's 'cancel' TextView click listener
        _tvCancelWidget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        // Identify the clicked view
        switch (v.getId())
        {
            case (R.id.viewCWNameColor):
                // Open the color picker
                openColorPicker();

                break;
            case (R.id.ivContactImage):
                // Pick a pic from the gallery
                getImage();

                break;
            case (R.id.tvCreateWidget):
                // Save the properties
                saveProperties();

                // Create the widget
                mListener.onDialogCreateClick(WidgetCreationDialog.this, _currContactWidget);

                break;
            case (R.id.tvCancel):
                // Close the dialog
                dismiss();

                break;
            default:
                break;
        }
    }

    /**
     * Opens the color picker for background button
     */
    private void openColorPicker()
    {
        ColorPickerDialog colorPicker = new ColorPickerDialog(_context, this, _currColor);
        colorPicker.show();
    }

    /**
     * Saves the ContactWidget's new details
     */
    private void saveProperties()
    {
        // The Contact's name
        _currContactWidget.setName(_tvPreviewText.getText().toString());

        // The ContactWidget's name text color
        _currContactWidget.setNameColor(_currColor);

        // The ContactWidget's icon
        _currContactWidget.setIcon(_currIcon);
    }

    /**
     * Gets an image from the gallery
     */
    private void getImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        _context.startActivityForResult(photoPickerIntent, SELECTED_PIC);
    }

    @Override
    public void onDialogClosed(Object dialogResult) {
        try {
            _currColor = (int) dialogResult;
            _viewNameColor.setBackgroundColor(_currColor);
            _tvPreviewText.setTextColor(_currColor);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
