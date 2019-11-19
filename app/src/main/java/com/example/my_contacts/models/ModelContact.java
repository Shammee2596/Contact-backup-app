package com.example.my_contacts.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class ModelContact implements Comparable<ModelContact>, Parcelable {
    private String name, number;
    private String email;
    private String label;
    private String lastName;
    private String imageUrl;
    private String userId;
    private boolean isFavourite = false;



    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ModelContact(){

    }

    public ModelContact(String name, String number,String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public ModelContact(String name, String number,String email, boolean isFavourite) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.isFavourite = isFavourite;
    }

    @Override
    public int compareTo(ModelContact u) {
        return name.toLowerCase().compareTo(u.name.toLowerCase());
    }

    @Override
    public boolean equals(@Nullable Object u) {
        ModelContact mc = (ModelContact) u;
        return number.equals(mc.number) && name.equals(mc.name);
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

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFavourite() {
        return isFavourite;
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
    }

    protected ModelContact(Parcel in) {
        this.name = in.readString();
        this.number = in.readString();
        this.email = in.readString();
        this.label = in.readString();
        this.lastName = in.readString();
        this.imageUrl = in.readString();
        this.userId = in.readString();
        this.isFavourite = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ModelContact> CREATOR = new Parcelable.Creator<ModelContact>() {
        @Override
        public ModelContact createFromParcel(Parcel source) {
            return new ModelContact(source);
        }

        @Override
        public ModelContact[] newArray(int size) {
            return new ModelContact[size];
        }
    };
}
