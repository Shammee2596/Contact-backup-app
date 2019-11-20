package com.example.contactapp_v3.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class Contact implements Comparable<Contact>,Parcelable {
    private String name, number;
    private String email;
    private String label;
    private String lastName;
    private String imageUrl;
    private String userId;
    private boolean isFavourite = false;
    private String _id;

    public Contact() {}

    public Contact(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public Contact(String name, String number, String email, boolean isFavourite) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.isFavourite = isFavourite;
    }

    public boolean findContact (Contact contact){
        return number.equals(contact.getNumber());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.number);
        dest.writeString(this.email);
        dest.writeString(this.label);
        dest.writeString(this.lastName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.userId);
        dest.writeByte(this.isFavourite ? (byte) 1 : (byte) 0);
        dest.writeString(this._id);
    }

    protected Contact(Parcel in) {
        this.name = in.readString();
        this.number = in.readString();
        this.email = in.readString();
        this.label = in.readString();
        this.lastName = in.readString();
        this.imageUrl = in.readString();
        this.userId = in.readString();
        this.isFavourite = in.readByte() != 0;
        this._id = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int compareTo(Contact u) {
        return name.toLowerCase().compareTo(u.name.toLowerCase());
    }

    @Override
    public boolean equals(@Nullable Object u) {
        Contact c = (Contact) u;
        return number.equals(c.number) && name.equals(c.name);
    }

    @Override
    public int hashCode() {
        long hash = 0;
        for (int i = 0; i < number.length(); i++) {
            hash = hash * 347L + number.charAt(i) ;
            hash %= 1000000007L;
        }
        return (int) hash;
    }
}
