package com.example.contactapp_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.reader.ContactReader;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    private TextView textViewAddContact;

    public static final List<Contact> contactList = new ArrayList<>();

    int ALL_PERMISSIONS = 101;
    final String[] permissions = new String[]{Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_CONTACTS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rv_contacts);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        textViewAddContact = findViewById(R.id.addContact);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contact")
                .child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class); //getting contacts
                    contactList.add(contact);

                }
                Collections.sort(contactList);
                Toast.makeText(MainActivity.this, String.valueOf(contactList.size()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textViewAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStartPage();
        }else {
            setRepeatingAsyncTask();
            fetch();
            adapter.startListening();
        }
    }

    private void setRepeatingAsyncTask() {

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ContactAsyncTask contactAsyncTask = new ContactAsyncTask();
                            contactAsyncTask.execute();
                        } catch (Exception e) {
                            // error, do something
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 10*1000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void sendToStartPage() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_log_out) {
            FirebaseAuth.getInstance().signOut();
            sendToStartPage();
        }
        return true;
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference("Contact")
                .child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("name")
                .startAt("").endAt("" + "\uf8ff");

        FirebaseRecyclerOptions<Contact> options =
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setQuery(query, Contact.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Contact, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.items_contacts, parent, false);


                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Contact model) {
                holder.setName(model.getName());
                holder.setNumber(model.getNumber());

                holder.item_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, contactList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Contact currentContact = contactList.get(position);
                        Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                        intent.putExtra("emptyContact", currentContact);
                        startActivity(intent);
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtNumber;
        Button button;

        LinearLayout item_contact;

        public ViewHolder(View itemView) {
            super(itemView);
            item_contact = itemView.findViewById(R.id.contact_item);
            txtName = itemView.findViewById(R.id.contact_name);
            txtNumber = itemView.findViewById(R.id.number);
            button = itemView.findViewById(R.id.contact_button);

        }

        public void setName(String name) {
            txtName.setText(name);
        }

        public void setNumber(String number) {
            txtNumber.setText(number);
        }



    }

     class ContactAsyncTask extends AsyncTask<Void, Void, Void> {
        ContactReader contactReader = new ContactReader(MainActivity.this);
        List<Contact> phoneContactList;

        @Override
        protected Void doInBackground(Void... voids) {

            //Toast.makeText(MainActivity.this, "Async called", Toast.LENGTH_LONG).show();
            System.out.println("Async called");

            while (contactReader.getContacts() == null);
            phoneContactList = contactReader.getContacts();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contact")
                    .child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            for (Contact contact: phoneContactList) {
                boolean found = false;
                for(Contact contact1: contactList){
                    if (contact.findContact(contact1)){
                        found = true;

                    }
                }
                if(!found){
                    reference.push().setValue(contact);
                }
            }
            return null;
        }
    }


}
