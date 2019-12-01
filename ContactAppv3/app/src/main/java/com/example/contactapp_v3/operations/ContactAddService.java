package com.example.contactapp_v3.operations;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.contactapp_v3.models.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactAddService {

    private Context context;

    public ContactAddService(Context context){
        this.context = context;
        //addToPhoneStorage(contact);
        //addToFirebase(contact);
    }

    public void addToPhoneStorage(Contact contact){
        addContactToSystemDatabase(contact.getName(), contact.getNumber(), "home", contact.getEmail());
    }

    public void addToFirebase(Contact contact){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contact")
                .child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.push().setValue(contact);
    }


    private long getRawContactId() {
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }

    private void addContactToSystemDatabase(String name, String phone,String phoneType, String email){
        Uri contactsUri = ContactsContract.Data.CONTENT_URI;
        long rowContactId = getRawContactId();
        insertContactDisplayName(contactsUri, rowContactId, name);
        insertContactPhoneNumber(contactsUri, rowContactId, phone, phoneType);
        insertContactEmail(contactsUri,rowContactId,email);
    }

    private void insertContactEmail(Uri contactsUri, long rawContactId, String email){
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, email);
        context.getContentResolver().insert(contactsUri, contentValues);
    }

    private void insertContactDisplayName(Uri contactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName);   // Put contact display name value.
        context.getContentResolver().insert(contactsUri, contentValues);
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
        context.getContentResolver().insert(addContactsUri, contentValues);

    }


}
