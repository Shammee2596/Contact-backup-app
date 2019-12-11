package com.example.contactapp_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactapp_v3.adapter.ContactAdapter;
import com.example.contactapp_v3.listener.OnContactDetailsListener;
import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.operations.ContactAddService;
import com.example.contactapp_v3.reader.ContactReader;
import com.example.contactapp_v3.repo.Repository;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements OnContactDetailsListener {

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private List<Contact> phoneContactList = null;
    private List<Contact> currentValueList = new ArrayList<>();


    private TextView textViewAddContact;
    private EditText editTextSearchContact;
    private ContactAddService contactAddService;

    public static Context context;
    public static boolean calling = false;
    public static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static final List<Contact> contactList = new ArrayList<>();
    public static final List<Contact> trashList = new ArrayList<>();
    public static DatabaseReference referenceTrueCaller;
    private OnContactDetailsListener detailsListener;
    private ValueEventListener eventListener;
    private DatabaseReference referenceContact;
    private DatabaseReference referenceTrash;
    private Repository repository;

    private FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addToolBar();
        context = this;

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.rv_contacts);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        contactAddService = new ContactAddService(this);

        //textViewAddContact = findViewById(R.id.addContact);
       // button = findViewById(R.id.buttonAddContact);

        detailsListener = this;

        editTextSearchContact = findViewById(R.id.editTextSearchContact);

        repository = Repository.getInstance();

        referenceContact = repository.getUserReference().child("contacts");

        referenceContact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    contact.set_id(postSnapshot.getKey());
                    contactList.add(contact);
                }
                atomicInteger.set(1);
                Collections.sort(contactList);
                Toast.makeText(MainActivity.this, String.valueOf(contactList.size()),
                        Toast.LENGTH_LONG).show();
                recyclerView.setAdapter(new ContactAdapter(MainActivity.this,
                        contactList, detailsListener));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceTrueCaller = FirebaseDatabase.getInstance().getReference("Truecaller");

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*textViewAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });*/

        editTextSearchContact.addTextChangedListener(new TextWatcher() {
            int textLength = 0;
            public void afterTextChanged(Editable s){}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextSearchContact.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textLength = editTextSearchContact.getText().length();
                String text = editTextSearchContact.getText().toString();
                currentValueList.clear();
                for (Contact currentContact: contactList) {
                    String name = currentContact.getName();
                    String number = currentContact.getNumber();
                    String email = currentContact.getEmail();
                    if (textLength <= name.length()) {
                        if(name.toLowerCase().contains(editTextSearchContact.
                                getText().toString().toLowerCase().trim())) {
                            currentValueList.add(currentContact);
                        } else if (textLength <= number.length()) {
                            if(number.toLowerCase().contains(editTextSearchContact.
                                    getText().toString().toLowerCase().trim())) {
                                currentValueList.add(currentContact);
                            }
                        } else if (textLength <= email.length()) {
                            if(email.toLowerCase().contains(editTextSearchContact.
                                    getText().toString().toLowerCase().trim())) {
                                currentValueList.add(currentContact);
                            }
                        }
                    }
                }
                Collections.sort(currentValueList);
                recyclerView.setAdapter(new ContactAdapter(MainActivity.this,
                        currentValueList, detailsListener));
            }
        });
        setRepeatingAsyncTask();
    }

    private void addToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStartPage();
        }
    }

    private void setRepeatingAsyncTask() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
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
        timer.schedule(task, 0, 60 * 1000);
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
        if (item.getItemId() == R.id.addContact) {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.menu_sync) {
            while (phoneContactList == null);
            mergeContact(phoneContactList, contactList);

        }

        if (item.getItemId() == R.id.menu_fav){
            Intent intent = new Intent(MainActivity.this, FavoriteContactActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void mergeContact(List<Contact> phoneContactList, List<Contact> firebaseContactList) {

        for (Contact firebaseContact : firebaseContactList) {
            boolean found = false;
            for (Contact phoneContact : phoneContactList) {
                if (firebaseContact.getName().equals(phoneContact.getName())
                        && firebaseContact.getNumber().equals(phoneContact.getNumber())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                contactAddService.addToPhoneStorage(firebaseContact);
            }
        }
    }

    @Override
    public void onContactDetails(Contact contact) {
        Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
        intent.putExtra("detailsContact", contact);
        startActivity(intent);
    }

    class ContactAsyncTask extends AsyncTask<Void, Void, Void> {
        ContactReader contactReader = new ContactReader(MainActivity.this);
        Repository repository;
        DatabaseReference reference;

        ContactAsyncTask(){
            repository = Repository.getInstance();
            reference = repository.getUserReference().child("contacts");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Toast.makeText(MainActivity.this, "Async called", Toast.LENGTH_LONG).show();
            if (atomicInteger.get() == 0){
                System.out.println("Async returned");
                return null;
            }
            try {
                System.out.println("Async called");
                do {
                    phoneContactList = contactReader.getContacts();
                } while (phoneContactList == null);

                for (Contact contact : phoneContactList) {
                    boolean found = false;
                    for (Contact contact1 : contactList) {
                        if (contact.findContact(contact1)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                       for(Contact contact1: trashList){
                           if (contact.findContact(contact1)) {
                               found = true;
                               break;
                           }
                       }
                    }
                    if (!found) {
                        String num = contact.getNumber();
                        if(!contact.getNumber().contains("+88")) {
                            num = "+88" + contact.getNumber();
                        }
                        reference.push().setValue(contact);
                        referenceTrueCaller.child(num).push()
                                .setValue(contact.getName());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }
    }
}
