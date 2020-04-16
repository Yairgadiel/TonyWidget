package com.yair.tonywidget.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yair.tonywidget.General.ContactWidget;
import com.yair.tonywidget.Helpers.ContextHelper;
import com.yair.tonywidget.R;

import java.util.ArrayList;

/**
 * Created by yair on 20/12/17.
 */

public class ContactsListAdapter extends BaseAdapter {
    private ArrayList _lsContacts;

    public ContactsListAdapter(ArrayList lsContacts) {
        this._lsContacts = lsContacts;
    }

    @Override
    public int getCount() {
        return (_lsContacts.size());
    }

    @Override
    public Object getItem(int i) {
        return (_lsContacts.get(i));
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        // Check if the list item view doesn't exist
        if (v == null) {
            // inflate the layout for each list row
            v = LayoutInflater.from(ContextHelper.getContext()).
                    inflate(R.layout.contact_list_item, viewGroup, false);
        }

        // Getting the current contact object
        ContactWidget currContactWidget = (ContactWidget) getItem(i);

        // Setting the current ListView item views
        // Setting the contact item's name
        ((TextView) v.findViewById(R.id.tvContactName)).setText(currContactWidget.getName());

        // Setting the contact item's number
        ((TextView) v.findViewById(R.id.tvContactNumber)).setText(currContactWidget.getPhoneNumber());

        // Setting the contact item's icon
        ((ImageView) v.findViewById(R.id.ivContactImage)).setImageBitmap(currContactWidget.getIcon());

        return (v);
    }
}
