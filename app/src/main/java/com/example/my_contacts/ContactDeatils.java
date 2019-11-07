package com.example.my_contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
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

public class ContactDeatils extends AppCompatActivity {

    private TextView btn;
    private TextView tvname, tvphone,tvmail;
    String name,number, email;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_deatils);

        btn =  findViewById(R.id.profile_edit);
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
                Log.e("msg","details is called from adapter class");
                startActivity(intent);
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
                    Log.d("", "Contact id::" + idPhone);
                }
            }
        } finally {
            mcursor.close();
        }
        return idPhone;
    }
}
