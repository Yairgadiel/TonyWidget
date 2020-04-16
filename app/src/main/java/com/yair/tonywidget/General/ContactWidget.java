package com.yair.tonywidget.General;

import android.graphics.Bitmap;

/**
 * Created by yair on 20/12/17.
 */

public class ContactWidget {
    // Data Members
    private int     _nId;
    private String  _stName;
    private int     _nNameColor;
    private String  _stPhoneNumber;
    private Bitmap  _bitmapIcon;

    // C'tor

    /**
     * Default C'tor for the ContactWidget object
     */
    public ContactWidget()
    {}

    /**
     * C'tor for the ContactWidget object
     * @param nId the contact widget's id
     * @param stName the contact's name
     * @param stPhoneNumber the contact's phone number
     * @param bitmapIcon the contact's icon
     */
    public ContactWidget(int nId, String stName, String stPhoneNumber, Bitmap bitmapIcon)
    {
        _nId = nId;
        _stName = stName;
        _stPhoneNumber = stPhoneNumber;
        _bitmapIcon = bitmapIcon;
    }

    // Access Methods

    public int getId()
    {
        return _nId;
    }

    public void setId(int nId)
    {
        _nId = nId;
    }

    public String getName() {
        return _stName;
    }

    public void setName(String stName) {
        this._stName = stName;
    }

    public int getNameColor() {
        return _nNameColor;
    }

    public void setNameColor(int nNameColor) {
        this._nNameColor = nNameColor;
    }

    public String getPhoneNumber() {
        return _stPhoneNumber;
    }

    public void setPhoneNumber(String stPhoneNumber) {
        this._stPhoneNumber = stPhoneNumber;
    }

    public Bitmap getIcon() {
        return _bitmapIcon;
    }

    public void setIcon(Bitmap bitmapIcon) {
        this._bitmapIcon = bitmapIcon;
    }

}
