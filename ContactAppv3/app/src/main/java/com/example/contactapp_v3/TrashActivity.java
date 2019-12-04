package com.example.contactapp_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.contactapp_v3.adapter.TrashAdapter;
import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.repo.Repository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrashActivity extends AppCompatActivity {

    private DatabaseReference referenceTrash;
    private Repository repository;
    private List<Contact> trashList;
    private RecyclerView recyclerView;
    private TrashAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        recyclerView = findViewById(R.id.rv_trash_contacts);

        repository = Repository.getInstance();

        trashList = new ArrayList<>();

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
                recyclerView.setAdapter(new TrashAdapter(TrashActivity.this, trashList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
