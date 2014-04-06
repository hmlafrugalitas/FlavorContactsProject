package com.hmlafrugalitas.flavorcontacts.model;

import android.content.Context;
import android.provider.ContactsContract;

import com.hmlafrugalitas.flavorcontacts.Tools.FlavorContactHelper;
import com.hmlafrugalitas.flavorcontacts.Tools.Utilities;

import java.util.ArrayList;

/**
 * Created by Hamdi on 21/03/14.
 */
public class FlavorContact {
    private int groupId;
    private Context context;
    private ArrayList<Contact> contacts;
    public FlavorContact(Context context, int _groupId) {
        this.context = context;
        this.groupId = _groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void loadContacts(Context context) {
        contacts = FlavorContactHelper.getContacts(context, groupId);
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
