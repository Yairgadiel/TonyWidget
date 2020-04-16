package com.yair.tonywidget.Interfaces;

/**
 * Interface for a dialog closing result.
 * Created by yair on 09/02/18.
 */

public interface IOnDialogClosed {
    /**
     * Occurs when the user closes the dialog.
     * Operates according to the result it gets.
     * @param dialogResult object, the result of the dialog
     */
    public void onDialogClosed(Object dialogResult);
}
