package com.hmlafrugalitas.flavorcontacts.Tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hmlafrugalitas.flavorcontacts.R;
import com.hmlafrugalitas.flavorcontacts.model.Contact;
import com.hmlafrugalitas.flavorcontacts.model.FlavorContact;
import com.hmlafrugalitas.flavorcontacts.views.FlavorContactsConfigureActivity;
import com.hmlafrugalitas.flavorcontacts.views.FlavorContactsWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Hamdi on 21/03/14.
 */
public class Utilities {
    private static final Uri GROUP_LIST_URI = ContactsContract.Groups.CONTENT_SUMMARY_URI;

    public static SimpleAdapter getContactGroupList(Context context)
    {
        final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        String[] GROUP_PROJECTION = new String[] {
                ContactsContract.Groups._ID, ContactsContract.Groups.TITLE,ContactsContract.Groups.SUMMARY_COUNT };
        Cursor cursor = context.getApplicationContext().getContentResolver().query(
                ContactsContract.Groups.CONTENT_SUMMARY_URI,
                GROUP_PROJECTION
                ,ContactsContract.Groups.SUMMARY_COUNT +"> 0"
                , null, ContactsContract.Groups.TITLE + " ASC");

        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
            String contactCount = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.SUMMARY_COUNT));
            String sId = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
            HashMap<String,String> temp = new HashMap<String,String>();
            temp.put("groupName",name + " ("+contactCount+")");
            temp.put("groupId",sId);
            list.add(temp);
        }
        SimpleAdapter simpleAdpt = new SimpleAdapter(context, list, R.layout.groupes_list_item, new String[] {"groupName","groupId"}, new int[] {R.id.list_item_textViewGroupName,R.id.list_item_textViewGroupId});

        return simpleAdpt;
    }


    public static CharSequence substring(String fullName, Integer contactNameMaxLength) {
        if(fullName.length() < contactNameMaxLength)
            return fullName;
        else
            return fullName.substring(0,contactNameMaxLength)+"...";
    }

    public static String getGson(Object contacts) {
        Gson gson = new Gson();
        return gson.toJson(contacts);
    }

    public static ArrayList<Contact> getArrayOfContactFromGson(String stringExtra) {
        Gson gson = new Gson();
        return (ArrayList<Contact>) gson.fromJson(stringExtra, new TypeToken<ArrayList<Contact>>(){}.getType());
    }

    /**
     * Enables strict mode. This should only be called when debugging the application and is useful
     * for finding some potential bugs or best practice violations.
     */
    @TargetApi(11)
    public static void enableStrictMode() {
        // Strict mode is only available on gingerbread or later
        if (hasGingerbread()) {

            // Enable all thread strict mode policies
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            // Enable all VM strict mode policies
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            // Honeycomb introduced some additional strict mode features
            if (hasHoneycomb()) {
                // Flash screen when thread policy is violated
                threadPolicyBuilder.penaltyFlashScreen();
                // For each activity class, set an instance limit of 1. Any more instances and
                // there could be a memory leak.
                vmPolicyBuilder
                        .setClassInstanceLimit(FlavorContactsWidget.class, 1)
                        .setClassInstanceLimit(FlavorContactsConfigureActivity.class, 1);
            }

            // Use builders to enable strict mode policies
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * Uses static final constants to detect if the device's platform version is Gingerbread or
     * later.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb or
     * later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb MR1 or
     * later.
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Uses static final constants to detect if the device's platform version is ICS or
     * later.
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
}
