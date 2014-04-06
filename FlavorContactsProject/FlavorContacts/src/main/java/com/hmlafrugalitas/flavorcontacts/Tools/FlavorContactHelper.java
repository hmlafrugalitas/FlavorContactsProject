package com.hmlafrugalitas.flavorcontacts.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.UserDictionary;
import android.widget.GridView;
import android.provider.ContactsContract.CommonDataKinds;

import com.hmlafrugalitas.flavorcontacts.R;
import com.hmlafrugalitas.flavorcontacts.model.Contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hamdi on 24/03/14.
 */
public class FlavorContactHelper {

    public static ArrayList<Contact> getContacts(Context context, int groupId) {

        ArrayList<Contact> groupMembers = new ArrayList<Contact>();
        String where =  CommonDataKinds.GroupMembership.GROUP_ROW_ID +"="+groupId
                +" AND "
                +CommonDataKinds.GroupMembership.MIMETYPE+"='"
                +CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'";
        String[] projection = new String[]{CommonDataKinds.GroupMembership.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI};
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, where,null,
                ContactsContract.Data.DISPLAY_NAME+" COLLATE LOCALIZED ASC");


        while(cursor.moveToNext()){

            if(!groupContainsContact(groupMembers, cursor.getInt(cursor.getColumnIndex(CommonDataKinds.GroupMembership.CONTACT_ID))))
            {
                Contact item = new Contact();
                item.setFullName(cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)));
                item.setThumbnail(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)));
                item.setContactId(cursor.getInt(cursor.getColumnIndex(CommonDataKinds.GroupMembership.CONTACT_ID)));

                Cursor phones = context.getContentResolver().query(CommonDataKinds.Phone.CONTENT_URI, null,
                        CommonDataKinds.Phone.CONTACT_ID + " = " + item.getContactId(), null, null);
                if (phones.moveToFirst()) {
                    String number = phones.getString(phones.getColumnIndex(CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    int type = phones.getInt(phones.getColumnIndex(CommonDataKinds.Phone.TYPE));
                    switch (type) {
                        case CommonDataKinds.Phone.TYPE_MAIN:
                            item.setPhoneNumber(number);
                            break;
                        case CommonDataKinds.Phone.TYPE_MOBILE:
                            item.setPhoneNumber(number);
                            break;
                        case CommonDataKinds.Phone.TYPE_WORK:
                            item.setPhoneNumber(number);
                            break;
                    }
                }
                phones.close();

                groupMembers.add(item);
            }
        }
        cursor.close();
        return groupMembers;
    }

    private static boolean groupContainsContact(ArrayList<Contact> groupMembers, int contactId) {
        boolean found = false;
        for (int i=0;i< groupMembers.size() && found==false;i++)
        {
            if(groupMembers.get(i).getContactId() == contactId)
                found = true;
        }
        return  found;
    }

    public static Bitmap getCircleBitmap(Uri uri, Context context) {
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            Bitmap resized = Bitmap.createScaledBitmap(bm, 104, 104, true);
            Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
            return conv_bm;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xFF33B5E5;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 98, 98);

            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }

}
