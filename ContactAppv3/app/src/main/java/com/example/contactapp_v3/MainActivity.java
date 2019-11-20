package com.example.contactapp_v3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //private TabAdapter adapter;
    //private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private View v;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

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

        LayoutInflater inflater = getLayoutInflater();

        //v = inflater.inflate(R.layout.frag_contacts,container,false);
        recyclerView = findViewById(R.id.rv_contacts);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


    }

    private void addContact(FirebaseUser currentUser) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contact")
                .child("User").child(currentUser.getUid()).push();

        Contact contact = new Contact("Name", "0123456789", "asd@gdfd.cvc");
        reference.setValue(contact);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStartPage();
        }else {
            //addContact(currentUser);
            fetch();
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    private void sendToStartPage() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

                final ViewHolder viewHolder = new ViewHolder(view);

                viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                    }
                });

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, Contact model) {
                holder.setName(model.getName());
                holder.setNumber(model.getNumber());
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

}
