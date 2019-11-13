package com.example.my_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.my_contacts.models.ModelContact;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditContact extends AppCompatActivity {

    EditText cFname, cLname,cPhone, cEmail,label;
    String fname, lname, phone,email;
    String fName, lName, number, emailAddress,label1;
    Button save;
    DatabaseReference reference;

    ModelContact contact;
    Long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        cFname = findViewById(R.id.editContactFName);
        cLname = findViewById(R.id.editContactLName);
        cPhone = findViewById(R.id.editContactNumber);
        cEmail = findViewById(R.id.editContactEmail);
        label = findViewById(R.id.editContactLabel);

        save = findViewById(R.id.editContactButton);

        fname = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("number");
        contactId = getIntent().getLongExtra("contactId",0);

        cFname.setText(fname);
        cPhone.setText(email);
        cEmail.setText(phone);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContactToDataBase();
            }
        });
    }
    private void updateContactToDataBase(){

        contact = new ModelContact();
        reference = FirebaseDatabase.getInstance().getReference().child("Contact");

        fName = cFname.getText().toString().trim();
        lName = cFname.getText().toString().trim();
        number = cPhone.getText().toString().trim();
        emailAddress = cEmail.getText().toString().trim();
        label1 = label.getText().toString().trim();

        updateContactToSystem(fname,number,emailAddress,label1);
    }
    private void updateContactToSystem(String name, String number, String email, String label){

        if (contactId> 0) {
//            Intent intent = new Intent(Intent.ACTION_EDIT);
//            intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId));
//            startActivity(intent);
            editContactToSystemDatabase(name, number, "home", email, contactId);



        } else {
            Toast.makeText(getApplicationContext(), "contact not in list",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void editContactToSystemDatabase(String name, String phone,String phoneType,String email, Long contactId){
        Uri contactsUri = ContactsContract.Data.CONTENT_URI;
        //insertContactDisplayName(contactsUri, contactId, name);// Add contact name data.
        //insertContactPhoneNumber(contactsUri, contactId, phone, phoneType);
        //updateContact(contactsUri, contactId, phone, name, email);
        finish();
    }

    private void updateContact(Uri contactsUri, Long rawContactId, String phoneNumber, String displayName, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName);
        contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, email);   // Put contact display name value.

        String where = ContactsContract.Data.CONTACT_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf( rawContactId )};
        getContentResolver().update(contactsUri, contentValues, where, selectionArgs);
    }

    private void insertContactEmail(Uri contactsUri, long rawContactId, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, email);   // Put contact display name value.
        getContentResolver().update(contactsUri, contentValues, null, null);
    }

    private void insertContactDisplayName(Uri contactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName);   // Put contact display name value.
        // getContentResolver().insert(contactsUri, contentValues);
        getContentResolver().update(contactsUri, contentValues, null, null);
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
        getContentResolver().update(addContactsUri, contentValues, null, null);

    }



}
