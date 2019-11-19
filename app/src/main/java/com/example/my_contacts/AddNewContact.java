package com.example.my_contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.my_contacts.adapters.Contact_rv_adapter;
import com.example.my_contacts.fragments.FragmentsContacts;
import com.example.my_contacts.models.ModelContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddNewContact extends AppCompatActivity {

    EditText fName, lName, number, email, label;
    Button saveButon;
    ImageView image;
    ModelContact contact;
    DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        //image = findViewById(R.id.addImage1);
        fName = findViewById(R.id.createContactFName);
        lName = findViewById(R.id.createContactLName);
        number = findViewById(R.id.createContactNumber);
        email = findViewById(R.id.createContactEmail);
        label = findViewById(R.id.createContactLabel);
        saveButon = findViewById(R.id.createContactButton);
        /*image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });*/
        addContactToDatabase();
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(image);
            image.setImageURI(imageUri);
        }
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
                contact.setUserId(databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().getKey());
                databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(contact);

                addContactToSystemDatabase(fName.getText().toString(),number.getText().toString(),
                        "mobile", email.getText().toString());
//                Intent i = new Intent(AddNewContact.this, MainActivity.class);
//                startActivity(i);

                FragmentsContacts.addListener.onContactAdd(contact);

                Intent data = new Intent();

                Toast.makeText(AddNewContact.this, "Contact saved successfully", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, data);

                finish();
            }
        });
    }

    private long getRawContactId() {
        ContentValues contentValues = new ContentValues(); // Insert an empty contact.
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        long ret = ContentUris.parseId(rawContactUri); // Get the newly created contact raw id.
        return ret;
    }

    public void addContactToSystemDatabase(String name, String phone,String phoneType,String email){
        Uri contactsUri = ContactsContract.Data.CONTENT_URI;
        long rowContactId = getRawContactId();                      // Add an empty contact and get the generated id.
        insertContactDisplayName(contactsUri, rowContactId, name);// Add contact name data.
        insertContactPhoneNumber(contactsUri, rowContactId, phone, phoneType);
        insertContactEmail(contactsUri,rowContactId,email);
        finish();
    }

    private void insertContactEmail(Uri contactsUri, long rawContactId, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, email);   // Put contact display name value.
        getContentResolver().insert(contactsUri, contentValues);
    }

    private void insertContactDisplayName(Uri contactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName);   // Put contact display name value.
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
