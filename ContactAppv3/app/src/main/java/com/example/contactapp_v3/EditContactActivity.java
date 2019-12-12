package com.example.contactapp_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.repo.Repository;
import com.google.firebase.database.DatabaseReference;

public class EditContactActivity extends AppCompatActivity {

    EditText cFname, cLname,cPhone, cEmail,label;
    String fname, lname, phone,email;
    String fName, lName, number, emailAddress,label1;
    Button save;
    DatabaseReference reference;
    private Contact contact;
    private Repository repository;
    Long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Edit Contact");

        cFname = findViewById(R.id.editContactFName);
        cLname = findViewById(R.id.editContactLName);
        cPhone = findViewById(R.id.editContactNumber);
        cEmail = findViewById(R.id.editContactEmail);
        label = findViewById(R.id.editContactLabel);

        save = findViewById(R.id.editContactButton);

//        fname = getIntent().getStringExtra("name");
//        email = getIntent().getStringExtra("email");
//        phone = getIntent().getStringExtra("number");
//        contactId = getIntent().getLongExtra("contactId",0);

        repository = Repository.getInstance();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            this.contact = bundle.getParcelable("edit_contact");
            cFname.setText(this.contact.getName());
            cPhone.setText(this.contact.getNumber());
            cEmail.setText(this.contact.getEmail());
            // todo: set other fields
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = cFname.getText().toString();
                String number = cPhone.getText().toString();
                String email = cEmail.getText().toString();
                contact.setName(name);
                contact.setNumber(number);
                contact.setEmail(email);
                repository.getUserReference().child("contacts")
                        .child(contact.get_id()).setValue(contact);

            }
        });

    }
}
