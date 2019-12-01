package com.example.contactapp_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contactapp_v3.models.Contact;
import com.google.firebase.database.DatabaseReference;

public class EditContactActivity extends AppCompatActivity {

    EditText cFname, cLname,cPhone, cEmail,label;
    String fname, lname, phone,email;
    String fName, lName, number, emailAddress,label1;
    Button save;
    DatabaseReference reference;

    Contact contact;
    Long contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        cFname = findViewById(R.id.editContactFName);
        cLname = findViewById(R.id.editContactLName);
        cPhone = findViewById(R.id.editContactNumber);
        cEmail = findViewById(R.id.editContactEmail);
        label = findViewById(R.id.editContactLabel);

        save = findViewById(R.id.editContactButton);

        fname = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("number");
        contactId = getIntent().getLongExtra("contactId",0);

        cFname.setText(fname);
        cPhone.setText(email);
        cEmail.setText(phone);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
