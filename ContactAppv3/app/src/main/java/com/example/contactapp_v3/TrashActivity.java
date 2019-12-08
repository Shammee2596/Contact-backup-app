package com.example.contactapp_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.contactapp_v3.adapter.ContactAdapter;
import com.example.contactapp_v3.adapter.TrashAdapter;
import com.example.contactapp_v3.listener.OnContactRestoreListener;
import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.operations.ContactAddService;
import com.example.contactapp_v3.operations.ContactRemoveService;
import com.example.contactapp_v3.repo.Repository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrashActivity extends AppCompatActivity implements OnContactRestoreListener {

    private DatabaseReference referenceTrash;
    private Repository repository;
    private List<Contact> trashList;
    private RecyclerView recyclerView;
    private TrashAdapter adapter;
    private OnContactRestoreListener restoreListener;
    private ContactAddService contactAddService;
    private ContactRemoveService contactRemoveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        recyclerView = findViewById(R.id.rv_trash_contacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        restoreListener = this;

        repository = Repository.getInstance();

        trashList = new ArrayList<>();

        contactAddService = new ContactAddService();
        contactRemoveService = new ContactRemoveService();

        referenceTrash = repository.getUserReference().child("trash");

        referenceTrash.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trashList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    contact.set_id(postSnapshot.getKey());
                    trashList.add(contact);
                }
                recyclerView.setAdapter(new TrashAdapter(TrashActivity.this,
                        trashList, restoreListener));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRestoreClick(Contact contact) {
        onContactInsert(contact);
        onContactRemoveFromTrash(contact);
    }

    @Override
    public boolean onContactInsert(Contact contact) {
        contactAddService.addToFirebaseContact(contact);
        return true;
    }

    @Override
    public boolean onContactRemoveFromTrash(Contact contact) {
        contactRemoveService.removeFromFirebaseTrash(contact);
        return true;
    }
}
