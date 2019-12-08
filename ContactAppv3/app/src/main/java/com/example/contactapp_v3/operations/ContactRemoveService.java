package com.example.contactapp_v3.operations;

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
}
