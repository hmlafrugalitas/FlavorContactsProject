package com.hmlafrugalitas.flavorcontacts.Enums;

import android.provider.ContactsContract;

/**
 * Created by Hamdi on 21/03/14.
 */
public class FlavorContactEnums {
    public static final String PREFS_NAME = "com.hmlafrugalitas.flavorcontacts";
    public final static String[] COLUMNS = new String[] {
                ContactsContract.Groups._ID,
                ContactsContract.Groups.TITLE,
            };
    public static final String GROUP_ID = PREFS_NAME + "_GROUP_ID";
    public static final String CONTACTS = PREFS_NAME + "_CONTACTS";
    public static final Integer CONTACT_NAME_MAX_LENGTH = 13;
}
