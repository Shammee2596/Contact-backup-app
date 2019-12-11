package com.example.contactapp_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.contactapp_v3.adapter.ContactAdapter;
import com.example.contactapp_v3.listener.OnContactDetailsListener;
import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.repo.Repository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteContactActivity extends AppCompatActivity implements OnContactDetailsListener {

    private RecyclerView recyclerView;
    private List<Contact> favoriteList;
    private DatabaseReference favoriteReference;
    private Repository repository;
    private OnContactDetailsListener detailsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detailsListener = this;
        favoriteList = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_favorite);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        repository = Repository.getInstance();

        favoriteReference = repository.getUserGroupFavoriteReference();

        favoriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    favoriteList.add(contact);
                }
                Collections.sort(favoriteList);
                recyclerView.setAdapter(new ContactAdapter(FavoriteContactActivity.this,
                        favoriteList, detailsListener));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onContactDetails(Contact contact) {

    }
}
