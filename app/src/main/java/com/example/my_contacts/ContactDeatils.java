package com.example.my_contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_contacts.adapters.Contact_rv_adapter;
import com.example.my_contacts.models.ModelContact;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static java.security.AccessController.getContext;

public class ContactDeatils extends AppCompatActivity {

    private Button btn, starButton;
    private TextView tvname, tvphone,tvmail;
    String name,number, email;
    long id;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_deatils);

        btn =  findViewById(R.id.edit_contact);
        starButton = findViewById(R.id.fav_button1);
        tvname = (TextView) findViewById(R.id.profile_displayName);
        tvphone = (TextView) findViewById(R.id.profile_number);
        tvmail = (TextView) findViewById(R.id.profile_Email);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        number = getIntent().getStringExtra("number");

        tvname.setText(name);
        tvphone.setText(number);
        tvmail.setText(email);
        id = getContactId(number);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDeatils.this,EditContact.class);
                intent.putExtra("name",name);
                intent.putExtra("number",email);
                intent.putExtra("email",number);
                intent.putExtra("contactId",id);
                startActivity(intent);
            }
        });

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentValues = new ContentValues();
                contentValues.put(ContactsContract.Contacts.STARRED,1);
                getContentResolver().update(ContactsContract.Contacts.CONTENT_URI,
                        contentValues, ContactsContract.Contacts._ID + "=" + id, null);
                Toast.makeText(ContactDeatils.this, "Contact added to favourite",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private  Long getContactId(String number){
        // CONTENT_FILTER_URI allow to search contact by phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        // This query will return NAME and ID of conatct, associated with phone //number.
        Cursor mcursor = getContentResolver().query(lookupUri,new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID},null, null, null);
        //Now retrive _ID from query result
        long idPhone = 0;
        try {
            if (mcursor != null) {
                if (mcursor.moveToFirst()) {
                    idPhone = Long.valueOf(mcursor.getString(mcursor.getColumnIndex(ContactsContract.PhoneLookup._ID)));
                }
            }
        } finally {
            mcursor.close();
        }
        return idPhone;
    }
}
