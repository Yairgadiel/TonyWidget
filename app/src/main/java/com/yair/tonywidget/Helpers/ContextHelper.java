package com.yair.tonywidget.Helpers;

import android.content.Context;

/**
 * Created by yair on 20/12/17.
 */

public class ContextHelper {
    static Context _context = null;

    /**
     * Sets the context object
     * @param context the context object to set
     */
    public static void setContext(Context context) {
        _context = context;
    }

    /**
     * Gets the context object
     * @return the context
     */
    public static Context getContext() {
        return (_context);
    }
}
