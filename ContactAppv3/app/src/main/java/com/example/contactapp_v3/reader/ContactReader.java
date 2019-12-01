package com.example.contactapp_v3.reader;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.contactapp_v3.App;
import com.example.contactapp_v3.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactReader {

    private Context context;

    public ContactReader(Context context){
        this.context = context;
    }


    public static List<Contact> getContacts(){
        List<Contact> contactList = new ArrayList<>();

        String phoneNumber="";
        String name;
//        Contact contact = new Contact();


        final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor cursor = null;

        try {
            cursor = App.getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, ContactsContract.Contacts.DISPLAY_NAME +" ASC");
        } catch (Exception e) {
            return null;
        }

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String star = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.STARRED));


                if (hasPhone.equalsIgnoreCase("1"))
                    hasPhone = "true";
                else
                    hasPhone = "false";

                if (Boolean.parseBoolean(hasPhone)) {
                    Cursor phones = App.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
                            new String[]{contactId}, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    phones.close();
                }

                // Find Email Addresses
                Cursor emails = App.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =" + contactId,
                        null, null);


                String emailAddress = null;
                while (emails.moveToNext()) {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                }
                emails.close();

                boolean isFavorite = false;

                if (star.equals("1")) isFavorite = true;

                contactList.add(new Contact(name, phoneNumber, emailAddress, isFavorite));
                Log.e("Test", "Email: " + emailAddress + "  Name: " + name + "  Number: " + phoneNumber);
            }

            cursor.close();
        }
        return contactList;
    }

}
