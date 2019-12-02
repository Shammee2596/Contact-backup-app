package com.example.contactapp_v3.repo;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Repository {

    //private Context context;
    private static Repository instance;

    public static Repository getInstance(){
        if (instance != null) {
            return instance;
        }
        instance = new Repository();
        return instance;
    }

    public DatabaseReference getUserReference() {
        return FirebaseDatabase.getInstance().getReference("contacts")
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().
                        getUid());
    }

}
