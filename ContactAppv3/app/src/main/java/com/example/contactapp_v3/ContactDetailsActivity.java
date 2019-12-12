package com.example.contactapp_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
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
import com.example.contactapp_v3.repo.Repository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.contactapp_v3.MainActivity.context;

public class ContactDetailsActivity extends AppCompatActivity {

    private Button btn, starButton;
    private TextView tvname, tvphone,tvmail;
    long id;
    ContentValues contentValues;
    private Menu menu;
    private Button btnWhatsApp;
    private LinearLayout btnMessage;
    boolean isFav = false;
    DatabaseReference databaseReference;
    private Contact contact;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Details");


        tvname =  findViewById(R.id.prfile_displayName);
        tvphone = findViewById(R.id.profile_number);
        tvmail =  findViewById(R.id.prfile_Email);

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

//        btnMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        LinearLayout call = findViewById(R.id.btn_call2);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                Toast.makeText(context, "Enable call phone", Toast.LENGTH_LONG).show();

                intentCall.setData(Uri.parse("tel:"+contact.getNumber()));

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Enable call phone in the settings", Toast.LENGTH_LONG).show();
                }else {
                    context.startActivity(intentCall);
                }
            }
        });
    }

    public void sendMessage(View view) {
        Uri sms_uri = Uri.parse("smsto:" + contact.getNumber());
        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
        startActivity(sms_intent);
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
        if (item.getItemId() == R.id.menuItemDeleteContact) {
            this.databaseReference.removeValue();
            this.repository.getUserReference().child("trash").push().setValue(this.contact);
            finish();
        }
        /*if (item.getItemId() == R.id.menuItemBlockContact){
            ContentValues values = new ContentValues();
            values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number);
            if (isAppAsDefaultDialer()){
                Toast.makeText(this, "Default", Toast.LENGTH_LONG).show();
                Uri uri = getContentResolver().insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values);
            }
        }
        return true;
    }

    private boolean isAppAsDefaultDialer() {
        TelecomManager telecom = this.getSystemService(TelecomManager.class);

        Log.d("DEFAULT APP ", "isAppAsDefaultDialer: " + telecom.getDefaultDialerPackage().toString());
        Toast.makeText(this, telecom.getDefaultDialerPackage().toString(), Toast.LENGTH_LONG).show();

        if (getApplicationContext().getPackageName().equals(telecom.getDefaultDialerPackage())) {
            return true;
        }*/
        return false;
    }

}
