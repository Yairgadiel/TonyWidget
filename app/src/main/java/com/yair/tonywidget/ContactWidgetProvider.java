package com.yair.tonywidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.yair.tonywidget.Activities.HomeActivity;
import com.yair.tonywidget.General.ContactWidget;
import com.yair.tonywidget.Helpers.ContextHelper;
import com.yair.tonywidget.Helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yair on 20/12/17.
 */

public class ContactWidgetProvider extends AppWidgetProvider
{

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        final int nWidgetAmount = appWidgetIds.length;

        // Getting all the ContactWidgets from the DB
//        List<ContactWidget> lsContactWidgets = DatabaseHelper.getInstance().getAllContactWidgets();
//        List<ContactWidget> lsContactWidgetsToRemove = new ArrayList<>(lsContactWidgets);
//
//        // Perform this loop procedure for each App Widget that belongs to this provider
//        for (int nWidgetIndex = 0; nWidgetIndex < nWidgetAmount; nWidgetIndex++)
//        {
//            int appWidgetId = appWidgetIds[nWidgetIndex];
//
//            // Go over the ContactWidgets list and locate the current one
//            for (ContactWidget currContactWidget : lsContactWidgets)
//            {
//                // Check if the current ContactWidget is the one we're looking for
//                if (currContactWidget.getId() == appWidgetId)
//                {
//                    System.out.println("HI");
//
//                    // Get the layout for the App Widget and attach an on-click listener
//                    // to the button
//                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tonywidget_layout);
//
//                    // Setting the contact's icon
//                    views.setImageViewBitmap(R.id.ivContactImage, currContactWidget.getIcon());
//
//                    // Setting the contact's name
//                    views.setTextViewText(R.id.tvContactName, currContactWidget.getName());
//                    views.setTextColor(R.id.tvContactName, currContactWidget.getNameColor());
//
//                    // Creating an Intent to make the call
//                    Intent intentMakeCall = new Intent(Intent.ACTION_CALL);
//                    intentMakeCall.setData(Uri.parse("tel:" + currContactWidget.getPhoneNumber()));
//
//                    views.setOnClickPendingIntent(R.id.layoutWidget, PendingIntent.getActivity(ContextHelper.getContext(),
//                            0,
//                            intentMakeCall,
//                            0));
//
//                    // Tell the AppWidgetManager to perform an update on the current app widget
//                    appWidgetManager.updateAppWidget(appWidgetId, views);
//
//                    // Update the ContactWidgets lists
//                    lsContactWidgetsToRemove.remove(currContactWidget);
//                    lsContactWidgets.remove(currContactWidget);
//
//                    break;
//                }
//            }
//        }

        // Update the DB
//        DatabaseHelper.getInstance().removeContacts(lsContactWidgetsToRemove);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Checking if the reception happens because of a boot
//        if (!intent.getAction().equals("android.intent.action.BOOT_COMPLETED") ||
//            HomeActivity.bAutoUpdateOnStartUp) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());

            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), ContactWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

            // Validating the widget list
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                // Set the context (for future use)
                ContextHelper.setContext(context);

//                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
//    }
}

