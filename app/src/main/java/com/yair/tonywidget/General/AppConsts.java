package com.yair.tonywidget.General;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.ContactsContract;

/**
 * Created by yair on 22/12/17.
 */

public class AppConsts {

    @SuppressLint("InlinedApi")
    public static final String SELECTION =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";

    @SuppressLint("InlinedApi")
    public static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER

            };

    public static final String stCURR_WIDGET_NUMBER     = "Call me maybe?";
    public static final String stCURR_WIDGET_NAME       = "What's my name?";
    public static final String stCURR_WIDGET_ICON       = "How do I look?";
}
