package com.example.my_contacts;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class My_Contacts extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
