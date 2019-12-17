package com.example.contactapp_v3.operations;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.repo.Repository;
import com.google.firebase.database.DatabaseReference;

public class ContactRemoveService {

    private Repository repository;
    private DatabaseReference referenceContacts;
    private DatabaseReference referenceTrash;

    public ContactRemoveService() {
        this.repository = Repository.getInstance();
        referenceContacts = repository.getUserReference().child("contacts");
        referenceTrash = repository.getUserReference().child("trash");
    }

    public void removeFromFirebaseTrash(Contact contact){
        referenceTrash.child(contact.get_id()).removeValue();
    }

    public boolean removeFromStorage(Context context, Contact contact) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contact.getNumber()));
        Cursor cur = context.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(contact.getName())) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        context.getContentResolver().delete(uri, null, null);
                        Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show();
                        //FragmentsContacts.deleteListener.onContactDelete(number);
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
}
