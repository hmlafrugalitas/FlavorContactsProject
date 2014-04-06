package com.hmlafrugalitas.flavorcontacts.Tools;


import java.io.IOException;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.widget.Toast;


import com.hmlafrugalitas.flavorcontacts.Enums.FlavorContactEnums;
import com.hmlafrugalitas.flavorcontacts.R;
import com.hmlafrugalitas.flavorcontacts.model.Contact;
import com.hmlafrugalitas.flavorcontacts.views.FlavorContactsWidget;

import java.util.ArrayList;

/**
 * Created by mlaouhih on 24/03/2014.
 */
public class ContactGridViewFactory implements RemoteViewsFactory {
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private Context context = null;
    private int appWidgetId;
    private int groupId;
    private Intent intent;


    public ContactGridViewFactory(Context context, Intent _intent, int _groupId, ArrayList<Contact> _contacts) {
        this.context = context;
        this.intent = _intent;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        this.groupId = _groupId;
        this.contacts = _contacts;
    }

    @Override
    public int getCount() {
        if(contacts == null)
            return 0;
        else
            return contacts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.contact_list_item);
        Contact listItem = contacts.get(position);
        remoteView.setTextViewText(R.id.contactListItemtextViewContactName, Utilities.substring(listItem.getFullName(), FlavorContactEnums.CONTACT_NAME_MAX_LENGTH));
        if(listItem.getThumbnail() != null) {
            Bitmap contactBitmap = FlavorContactHelper.getCircleBitmap(Uri.parse(listItem.getThumbnail()), context);
            if (contactBitmap != null) {
                remoteView.setImageViewBitmap(R.id.contactListItemImageView,
                        contactBitmap);
            }
        }
        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in StackWidgetProvider.
        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putInt(FlavorContactsWidget.EXTRA_STRING, listItem.getContactId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.contactItemAction, fillInIntent);

        System.out.println("Loading view " + position);

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        contacts = Utilities.getArrayOfContactFromGson(intent.getStringExtra(FlavorContactEnums.CONTACTS));
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}