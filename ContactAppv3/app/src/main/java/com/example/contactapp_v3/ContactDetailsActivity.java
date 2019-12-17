package com.example.contactapp_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.operations.ContactRemoveService;
import com.example.contactapp_v3.repo.Repository;
import com.google.firebase.database.DatabaseReference;
import static com.example.contactapp_v3.MainActivity.context;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView tvname, tvphone,tvmail;
    long id;
    ContentValues contentValues;
    private Menu menu;
    private TextView btnWhatsApp;
    private LinearLayout btnMessage;
    boolean isFav = false;
    DatabaseReference databaseReference;
    private Contact contact;
    private Repository repository;
    private ContactRemoveService removeService;
    private LinearLayout personCall;
    private LinearLayout personMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Details");

        removeService = new ContactRemoveService();

        tvname =  findViewById(R.id.prfile_displayName);
        tvphone = findViewById(R.id.profile_number);
        tvmail =  findViewById(R.id.prfile_Email);

        personCall = findViewById(R.id.person_phone);
        personMail = findViewById(R.id.person_mail);

        btnWhatsApp = findViewById(R.id.btn_whatsApp);
        btnMessage = findViewById(R.id.btn_message1);

        this.repository = Repository.getInstance();

        Bundle bundle = getIntent().getExtras();

         if (bundle != null){
            this.contact = bundle.getParcelable("detailsContact");
            tvname.setText(contact.getName());
            tvphone.setText(contact.getNumber());
            tvmail.setText(contact.getEmail());

            this.databaseReference = repository.getUserReference().child("contacts")
                    .child(contact.get_id());

        }
         btnWhatsApp.setText( contact.getNumber());
        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = contact.getNumber();
                if (!contact.getNumber().contains("+88")){
                    number = "+88" + contact.getNumber();
                }
                Uri uri = Uri.parse("smsto:" + number);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
        });

        LinearLayout call = findViewById(R.id.btn_call2);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callNumber();
            }
        });
        personCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNumber();
            }
        });

        personMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*final Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setType("plain/text")
                        .setData(Uri.parse("bsse0812@iit.du.ac.bd"))
                        .setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                startActivity(intent);*/
            }
        });
    }

    public void sendMessage(View view) {
        Uri sms_uri = Uri.parse("smsto:" + contact.getNumber());
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        startActivity(sms_intent);
    }
    private void callNumber(){
        Intent intentCall = new Intent(Intent.ACTION_CALL);
        // Toast.makeText(context, "Enable call phone", Toast.LENGTH_LONG).show();

        intentCall.setData(Uri.parse("tel:"+contact.getNumber()));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Enable call phone in the settings", Toast.LENGTH_LONG).show();
        }else {
            context.startActivity(intentCall);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        this.menu = menu;
        if (contact.isFavourite()) menu.getItem(1).setIcon(ContextCompat
                .getDrawable(this, R.drawable.ic_star_white));

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menuItemEditContact) {
            Intent intent = new Intent(ContactDetailsActivity.this,
                    EditContactActivity.class);
            intent.putExtra("edit_contact", contact);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menuItemFavouriteContact) {
            if (contact.isFavourite()){
               /* this.repository.getUserReference().child("contacts")
                        .child(contact.get_id()).removeValue();*/
                this.repository.getUserReference().child("contacts").
                        child("groups").child("favorites").child(contact.get_id()).removeValue();
                contact.setFavourite(false);
                this.repository.getUserReference().child("contacts")
                        .child(contact.get_id()).setValue(contact);
                menu.getItem(1).setIcon(ContextCompat
                        .getDrawable(this, R.drawable.ic_star));
            }
            else {
                contentValues = new ContentValues();
                contentValues.put(ContactsContract.Contacts.STARRED, 1);
                getContentResolver().update(ContactsContract.Contacts.CONTENT_URI,
                        contentValues, ContactsContract.Contacts._ID + "=" + id, null);
                Toast.makeText(ContactDetailsActivity.this,
                        "Contact added to favourite", Toast.LENGTH_SHORT).show();
                menu.getItem(1).setIcon(ContextCompat
                        .getDrawable(this, R.drawable.ic_star_white));
                contact.setFavourite(true);

                this.repository.getUserReference().child("contacts")
                        .child(contact.get_id()).setValue(contact);
                this.repository.getUserGroupFavoriteReference()
                        .push().setValue(contact);
            }
        }
        if (item.getItemId() == R.id.menuItemDeleteContact) {
            this.databaseReference.removeValue();
            this.repository.getUserReference().child("trash").push().setValue(this.contact);
            removeService.removeFromStorage(this, this.contact);
            finish();
        }
        return false;
    }

}
