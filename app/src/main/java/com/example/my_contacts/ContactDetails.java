package com.example.my_contacts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetails extends AppCompatActivity {

    private Button btn, starButton;
    private TextView tvname, tvphone,tvmail;
    String name,number, email;
    long id;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_deatils);

        tvname =  findViewById(R.id.profile_displayName);
        tvphone = findViewById(R.id.profile_number);
        tvmail =  findViewById(R.id.profile_Email);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        number = getIntent().getStringExtra("number");

        tvname.setText(name);
        tvphone.setText(number);
        tvmail.setText(email);
        id = getContactId(number);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menuItemEditContact) {
            Intent intent = new Intent(ContactDetails.this,EditContact.class);
                intent.putExtra("name",name);
                intent.putExtra("number",email);
                intent.putExtra("email",number);
                intent.putExtra("contactId",id);
                startActivity(intent);
        }
        if (item.getItemId() == R.id.menuItemFavouriteContact){
           contentValues = new ContentValues();
            contentValues.put(ContactsContract.Contacts.STARRED,1);
            getContentResolver().update(ContactsContract.Contacts.CONTENT_URI,
                    contentValues, ContactsContract.Contacts._ID + "=" + id, null);
            Toast.makeText(ContactDetails.this, "Contact added to favourite", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menuItemDeleteContact) {
            Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            Cursor cur = this.getContentResolver().query(contactUri, null, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                            String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                            this.getContentResolver().delete(uri, null, null);
                            return true;
                        }

                    } while (cur.moveToNext());
                }

            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            } finally {
                cur.close();
            }
            return false;
        }
        return true;
    }
}
