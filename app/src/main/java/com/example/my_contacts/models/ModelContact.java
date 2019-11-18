package com.example.my_contacts.models;

import androidx.annotation.Nullable;

public class ModelContact implements Comparable<ModelContact> {
    private String name, number;
    private String email;
    private String label;
    private  String lastName;
    private  String imageUrl;
    private String userId;

    public ModelContact(){

    }

    public ModelContact(String name, String number,String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    @Override
    public int compareTo(ModelContact u) {
        return name.toLowerCase().compareTo(u.name.toLowerCase());
    }

    @Override
    public boolean equals(@Nullable Object u) {
        if (u == this){
            ModelContact mc = (ModelContact) u;
            return name.equals(mc.name);
        }
        return false;
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
}
