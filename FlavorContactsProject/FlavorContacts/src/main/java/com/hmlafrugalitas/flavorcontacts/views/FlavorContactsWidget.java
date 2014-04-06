package com.hmlafrugalitas.flavorcontacts.views;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hmlafrugalitas.flavorcontacts.Enums.FlavorContactEnums;
import com.hmlafrugalitas.flavorcontacts.R;
import com.hmlafrugalitas.flavorcontacts.Tools.FlavorContactHelper;
import com.hmlafrugalitas.flavorcontacts.Tools.FlavorContactRemoteViewsService;
import com.hmlafrugalitas.flavorcontacts.Tools.Utilities;
import com.hmlafrugalitas.flavorcontacts.model.Contact;
import com.hmlafrugalitas.flavorcontacts.model.FlavorContact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mlaouhih on 21/03/14.
 */
public class FlavorContactsWidget extends AppWidgetProvider {
    private static RemoteViews remoteViews;
    private static FlavorContact flavorContact;

    public static final String ACTION_TOAST = "com.hmlafrugalitas.flavorcontacts.ACTION_TOAST";
    public static final String EXTRA_STRING = "com.hmlafrugalitas.flavorcontacts.EXTRA_STRING";

    /*
	 * this method is called every 30 mins as specified on widgetinfo.xml
	 * this method is also called on every phone reboot
	 */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;
		/*int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            initFlavorContact(context,appWidgetIds[i]);
            remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        initFlavorContact(context,appWidgetId);
        remoteViews = updateWidgetListView(context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.flavor_contact_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, FlavorContactRemoteViewsService.class);
        svcIntent.putExtra(FlavorContactEnums.GROUP_ID, flavorContact.getGroupId());
        svcIntent.putExtra(FlavorContactEnums.CONTACTS, Utilities.getGson(flavorContact.getContacts()));
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.gridView,
                svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.empty_view, R.id.empty_view);

        // This section makes it possible for items to have individualized behavior.
        // It does this by setting up a pending intent template. Individuals items of a collection
        // cannot set up their own pending intents. Instead, the collection as a whole sets
        // up a pending intent template, and the individual items set a fillInIntent
        // to create unique behavior on an item-by-item basis.
        Intent toastIntent = new Intent(context, FlavorContactsWidget.class);
        // Set the action for the intent.
        // When the user touches a particular view, it will have the effect of
        // broadcasting TOAST_ACTION.
        toastIntent.setAction(FlavorContactsWidget.ACTION_TOAST);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.gridView, toastPendingIntent);
        return remoteViews;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        //final int N = appWidgetIds.length;
        //for (int i = 0; i < N; i++) {
//
        //}
    }

    @Override
    public void onEnabled(Context context) {
    }

    // Called when the BroadcastReceiver receives an Intent broadcast.
    // Checks to see whether the intent's action is TOAST_ACTION. If it is, the app widget
    // displays a Toast message for the current item.
    @Override
    public void onReceive(Context context, Intent intent) {
        //AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(FlavorContactsWidget.ACTION_TOAST)) {
            Integer contactId = intent.getIntExtra(FlavorContactsWidget.EXTRA_STRING, 0);
            if(contactId != 0)
            {
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactId));
                ContactsContract.QuickContact.showQuickContact(context, intent.getSourceBounds(), uri, ContactsContract.QuickContact.MODE_SMALL, null);
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private static void initFlavorContact(Context context, int appWidgetId) {
        try {
            Integer selectedGroupId = Integer.parseInt(String.valueOf(
                    FlavorContactsConfigureActivity.loadPref(context, appWidgetId, FlavorContactEnums.GROUP_ID)));
            flavorContact = new FlavorContact(context, selectedGroupId);
            flavorContact.loadContacts(context);
        }catch (Exception ex)
        {
            flavorContact = new FlavorContact(context,-1);
        }
    }

}