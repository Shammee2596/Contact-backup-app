package com.example.my_contacts.fragments;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_contacts.R;
import com.example.my_contacts.adapters.Contact_rv_adapter;
import com.example.my_contacts.models.ModelContact;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Favourite extends Fragment {
    private View v;
    private RecyclerView recyclerView;
    List<ModelContact> favouriteContactList = new ArrayList<>();

    public Fragment_Favourite() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_fav,container,false);
        recyclerView = v.findViewById(R.id.rv_favourite);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        favouriteContactList = getContacts1();
        Contact_rv_adapter contactAapter = new Contact_rv_adapter(getContext(),favouriteContactList);
        contactAapter.notifyDataSetChanged();
        recyclerView.setAdapter(contactAapter);

        return v;
    }
    public List<ModelContact> getContacts1(){
        List<ModelContact> contactList = new ArrayList<>();

        String phoneNumber="";
        String name;
        ModelContact modelContact = new ModelContact();

        Uri queryUri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.STARRED};

        String selection =ContactsContract.Contacts.STARRED + "='1'";

        Cursor cursor = getContext().getContentResolver().query(queryUri,projection,selection,null, null);

        while (cursor.moveToNext()){

            String contactID = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            String name1 = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

            Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
                    new String[]{contactID}, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

            while (phones.moveToNext()) {
                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

            phones.close();

            Cursor emails = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =" + contactID,
                    null, null);


            String emailAddress = null;
            while (emails.moveToNext()) {
                emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

            }
            emails.close();

            contactList.add(new ModelContact(name1, phoneNumber, emailAddress));

            Log.e("Favourite",name1);
            Log.e("favourite Contact:","Email: " + emailAddress + "  Name: " + name1 + "  Number: " + phoneNumber);
        }
        cursor.close();

        return contactList;
    }



}

