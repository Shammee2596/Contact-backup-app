package com.example.contactapp_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

public class ContactDetailsActivity extends AppCompatActivity {

    private Button btn, starButton;
    private TextView tvname, tvphone,tvmail;
    String name,number, email;
    long id;
    ContentValues contentValues;
    private Menu menu;
    boolean isFav = false;
    DatabaseReference databaseReference;
    private Contact contact;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        tvname =  findViewById(R.id.profile_displayName);
        tvphone = findViewById(R.id.profile_number);
        tvmail =  findViewById(R.id.profile_Email);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        this.menu = menu;
        if (isFav) menu.getItem(1).setIcon(ContextCompat
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
//            contentValues = new ContentValues();
//            contentValues.put(ContactsContract.Contacts.STARRED, 1);
//            getContentResolver().update(ContactsContract.Contacts.CONTENT_URI,
//                    contentValues, ContactsContract.Contacts._ID + "=" + id, null);
//            Toast.makeText(ContactDetails.this, "Contact added to favourite", Toast.LENGTH_SHORT).show();
//            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_white));
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
