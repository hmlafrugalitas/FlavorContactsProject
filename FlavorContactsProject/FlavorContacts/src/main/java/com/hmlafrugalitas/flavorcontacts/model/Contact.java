package com.hmlafrugalitas.flavorcontacts.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mlaouhih on 25/03/2014.
 */
public class Contact {
    private String fullName = "";
    private String phoneNumber= "";
    private String     thumbnail= "";
    private Integer contactId;

    // public static final Parcelable.Creator<Contact> CREATOR
   //         = new Parcelable.Creator<Contact>() {
   //     public Contact createFromParcel(Parcel in) {
   //         return new Contact(in);
   //     }
//
   //     public Contact[] newArray(int size) {
   //         return new Contact[size];
   //     }
   // };
//
   // public Contact(Parcel in) {
   //     try {
   //         fullName = in.readString();
   //         phoneNumber = in.readString();
   //         thumbnail = in.readString();
   //     }catch (Exception ex)
   //     {
//
   //     }
   // }

    public Contact() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Integer getContactId() {
        return contactId;
    }

    //@Override
    //public int describeContents() {
    //    return 0;
    //}
//
    //@Override
    //public void writeToParcel(Parcel parcel, int i) {
    //    parcel.writeString(fullName);
    //    parcel.writeString(phoneNumber);
    //    parcel.writeString(thumbnail);
    //}
}
