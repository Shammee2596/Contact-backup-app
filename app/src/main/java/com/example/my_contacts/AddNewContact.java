package com.example.my_contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.my_contacts.adapters.Contact_rv_adapter;
import com.example.my_contacts.models.ModelContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewContact extends AppCompatActivity {

    EditText fName, lName, number, email, label;
    Button saveButon;
    ModelContact contact;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);
        fName = findViewById(R.id.createContactFName);
        lName = findViewById(R.id.createContactLName);
        number = findViewById(R.id.createContactNumber);
        email = findViewById(R.id.createContactEmail);
        label = findViewById(R.id.createContactLabel);
        saveButon = findViewById(R.id.createContactButton);
        addContactToDatabase();
    }
    private void addContactToDatabase(){
        contact = new ModelContact();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Contact");
        saveButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.setName(fName.getText().toString().trim());
                contact.setLastName(lName.getText().toString().trim());
                contact.setNumber(number.getText().toString().trim());
                contact.setEmail(email.getText().toString().trim());
                contact.setLabel(label.getText().toString().trim());
                databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(contact);
                addContactToSystemDatabase(fName.getText().toString().trim(),number.getText().toString().trim(),"mobile");
                Intent i = new Intent(AddNewContact.this, MainActivity.class);
                startActivity(i);
                Toast.makeText(AddNewContact.this, "Contact saved successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void addContactToSystemDatabase(String name, String phone,String phoneType){
        Uri contactsUri = ContactsContract.Data.CONTENT_URI;
        long rowContactId = getRawContactId();                      // Add an empty contact and get the generated id.
        insertContactDisplayName(contactsUri, rowContactId, name);// Add contact name data.
        insertContactPhoneNumber(contactsUri, rowContactId, phone, phoneType);
        finish();
    }

    private long getRawContactId() {
        ContentValues contentValues = new ContentValues(); // Insert an empty contact.
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        long ret = ContentUris.parseId(rawContactUri); // Get the newly created contact raw id.
        return ret;
    }

    private void insertContactDisplayName(Uri contactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);   // Put contact display name value.
        getContentResolver().insert(contactsUri, contentValues);
    }

    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr) {

        ContentValues contentValues = new ContentValues(); // Create a ContentValues object.

        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber); // Put phone number value.

        // Calculate phone type by user selection.
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;

        if("home".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        }else if("mobile".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }else if("work".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);

        // Insert new contact data into phone contact list.
        getContentResolver().insert(addContactsUri, contentValues);

    }



}
