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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_deatils);

        btn =  findViewById(R.id.contact_name);
        tvname = (TextView) findViewById(R.id.profile_displayName);
        tvphone = (TextView) findViewById(R.id.profile_number);
        tvmail = (TextView) findViewById(R.id.profile_Email);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        number = getIntent().getStringExtra("number");

        tvname.setText(name);
        tvphone.setText(number);
        tvmail.setText(email);

    }
}
