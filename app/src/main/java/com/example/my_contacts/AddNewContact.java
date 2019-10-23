package com.example.my_contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.my_contacts.adapters.Contact_rv_adapter;
import com.example.my_contacts.models.ModelContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNewContact extends AppCompatActivity {

    EditText fName, lName, number, email, label;
    Button saveButon;
    ModelContact contact;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        fName = findViewById(R.id.createContactFName);
        lName = findViewById(R.id.createContactLName);
        number = findViewById(R.id.createContactNumber);
        email = findViewById(R.id.createContactEmail);
        label = findViewById(R.id.createContactLabel);
        saveButon = findViewById(R.id.createContactButton);

        addContactToDatabase();
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
                databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(contact);
                Intent i = new Intent(AddNewContact.this, MainActivity.class);
                startActivity(i);
                Toast.makeText(AddNewContact.this, "Contact saved successfully", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }


}
