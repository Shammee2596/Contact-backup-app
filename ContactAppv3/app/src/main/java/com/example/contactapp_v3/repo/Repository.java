package com.example.contactapp_v3.repo;

import android.content.Context;

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

}
