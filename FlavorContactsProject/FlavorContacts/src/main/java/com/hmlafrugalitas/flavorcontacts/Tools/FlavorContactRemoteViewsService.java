package com.hmlafrugalitas.flavorcontacts.Tools;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.hmlafrugalitas.flavorcontacts.Enums.FlavorContactEnums;
import com.hmlafrugalitas.flavorcontacts.model.Contact;

import java.util.ArrayList;

/**
 * Created by mlaouhih on 24/03/2014.
 */
public class FlavorContactRemoteViewsService extends RemoteViewsService {
	/*
	 * So pretty simple just defining the Adapter of the listview
	 * here Adapter is ListProvider
	 * */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int groupId = intent.getIntExtra(FlavorContactEnums.GROUP_ID,-1);
        ArrayList<Contact> contacts = Utilities.getArrayOfContactFromGson(intent.getStringExtra(FlavorContactEnums.CONTACTS));
        return (new ContactGridViewFactory(this.getApplicationContext(), intent, groupId, contacts));
    }
}
