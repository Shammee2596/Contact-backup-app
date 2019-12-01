package com.example.contactapp_v3.contact_sync_service;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.contactapp_v3.reader.ContactReader;

public class ContactObserverService extends Service {
    private static final String TAG = "ContactObserverService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyContactContentObserver extends ContentObserver {
        public MyContactContentObserver() {
            super(null);
            Log.d(":-> Service Called","in construcrtor ");
        }
        @Override
        public void onChange(boolean selfChange) {
            //super.onChange(selfChange);
            Log.d(":-> Service Called","In onChange");
            ContactReader.getContacts();

        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                true, new MyContactContentObserver());
    }
}
