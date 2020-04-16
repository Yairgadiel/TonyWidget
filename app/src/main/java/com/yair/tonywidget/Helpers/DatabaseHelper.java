package com.yair.tonywidget.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.yair.tonywidget.General.ContactWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yair on 05/01/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Constants
    public static final int     nDATABASE_VERSION           = 1;
    public static final String  stDATABASE_NAME             = "TonyWidget.db";
    public static final String  stTABLE_NAME                = "T_CONTACTS";
    public static final String  stCOLUMN_CONTACT_WIDGET_ID  = "_id";
    public static final String  stCOLUMN_CONTACT_NAME       = "NAME";
    public static final String  stCOLUMN_CONTACT_NUMBER     = "NUMBER";
    public static final String  stCOLUMN_CONTACT_ICON       = "ICON";
    public static final String  stCOLUMN_CONTACT_COLOR      = "COLOR";

    // Singleton Instance
    private static DatabaseHelper _Instance = null;

    private static final String SQL_CREATE_ENTRIES      = "CREATE TABLE " + stTABLE_NAME + " (" +
                                                           stCOLUMN_CONTACT_WIDGET_ID + " INTEGER PRIMARY KEY," +
                                                           stCOLUMN_CONTACT_NAME + " TEXT," +
                                                           stCOLUMN_CONTACT_NUMBER + " TEXT," +
                                                           stCOLUMN_CONTACT_ICON + " TEXT," +
                                                           stCOLUMN_CONTACT_COLOR + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES      = "DROP TABLE IF EXISTS " + stTABLE_NAME;


    /**
     * Private C'tor to the DatabaseHelper
     * @param context the context (duh)
     */
    private DatabaseHelper(Context context)
    {
        super(context, stDATABASE_NAME, null, nDATABASE_VERSION);
    }

    /**
     * Singleton's getInstance method
     * @return a class instance
     */
    public static DatabaseHelper getInstance()
    {
        // Checking if the instance was already created
        if (_Instance == null)
        {
            // Create the DB Instance
            _Instance = new DatabaseHelper(ContextHelper.getContext());
        }

        return (_Instance);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    // DB Methods

    /**
     * Adding ContactWidget the DB
     * @param contactWidget the ContactWidget to add
     */
    public void addContactWidget(final ContactWidget contactWidget)
    {
        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                // Gets the data repository in write mode
                SQLiteDatabase db = getInstance().getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();

                values.put(stCOLUMN_CONTACT_WIDGET_ID, contactWidget.getId());
                values.put(stCOLUMN_CONTACT_NAME, contactWidget.getName());
                values.put(stCOLUMN_CONTACT_NUMBER, contactWidget.getPhoneNumber());
                values.put(stCOLUMN_CONTACT_ICON, BitmapHelper.BitmapToString(contactWidget.getIcon()));
                values.put(stCOLUMN_CONTACT_COLOR, contactWidget.getNameColor());

                // Inserting the new row
                db.insert(stTABLE_NAME, null, values);
            }
        });
    }

    /**
     * Getting all the ContactWidgets from the DB
     * @return list of the ContactWidgets retrieved from the DB
     */
    public ArrayList<ContactWidget> getAllContactWidgets()
    {
        final String stSELECT_QUERY = "SELECT * FROM " + stTABLE_NAME;

        ArrayList<ContactWidget> lsContactWidgets = new ArrayList<>();

        // Gets the data repository in write mode
        SQLiteDatabase db = getInstance().getReadableDatabase();

        // Get the objects from the DB
        Cursor cursorContactWidgets = db.rawQuery(stSELECT_QUERY, null);

        // Go over the Cursor
        while (cursorContactWidgets.moveToNext())
        {
            lsContactWidgets.add(
                    // Building the current Contact Widget
                    new ContactWidget(cursorContactWidgets
                                            .getInt(cursorContactWidgets.getColumnIndex(stCOLUMN_CONTACT_WIDGET_ID)),
                                    cursorContactWidgets
                                            .getString(cursorContactWidgets.getColumnIndex(stCOLUMN_CONTACT_NAME)),
                                    cursorContactWidgets
                                            .getString(cursorContactWidgets.getColumnIndex(stCOLUMN_CONTACT_NUMBER)),
                                    BitmapHelper.StringToBitmap(cursorContactWidgets
                                            .getString(cursorContactWidgets.getColumnIndex(stCOLUMN_CONTACT_ICON)))));
        }

        return (lsContactWidgets);
    }

    /**
     * Removes a given list of ContactWidgets from the DB
     * @param lsContactWidgetsToDelete the list of ContactWidgets to delete
     */
    public void removeContacts(final List<ContactWidget> lsContactWidgetsToDelete)
    {
        // Checking if the list isn't empty
        if (lsContactWidgetsToDelete.size() > 0)
        {
            AsyncTask.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    String stWhereStatement = "";
                    String[] arrsIdsToDelete = new String[lsContactWidgetsToDelete.size()];

                    // Generate the delete statement
                    // Go over the ContactWidgets to delete list
                    for (ContactWidget currContactWidget : lsContactWidgetsToDelete)
                    {
                          stWhereStatement += (stCOLUMN_CONTACT_WIDGET_ID  + " = ?");

                          arrsIdsToDelete[lsContactWidgetsToDelete.indexOf(currContactWidget)] =
                                                            String.valueOf(currContactWidget.getId());

                          // Checking if we're not on the last ContactWidget
                          if (lsContactWidgetsToDelete.indexOf(currContactWidget) + 1 <
                                                                        lsContactWidgetsToDelete.size())
                          {
                                stWhereStatement += " OR ";
                          }
                    }

                    // Gets the data repository in write mode and executes the query
                    SQLiteDatabase db = getInstance().getWritableDatabase();

                    db.delete(stTABLE_NAME, stWhereStatement, arrsIdsToDelete);
                }
            });
        }
    }
}
