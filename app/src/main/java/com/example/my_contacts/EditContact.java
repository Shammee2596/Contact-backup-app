package com.example.my_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.my_contacts.models.ModelContact;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditContact extends AppCompatActivity {

    EditText cFname, cLname,cPhone, cEmail,label;
    String fname, lname, phone,email;
    String fName, lName, number, emailAddress,label1;
    Button save;
    DatabaseReference reference;

    ModelContact contact;
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
                updateContactToDataBase();
            }
        });
    }
    private void updateContactToDataBase(){

        contact = new ModelContact();
        reference = FirebaseDatabase.getInstance().getReference().child("Contact");

        fName = cFname.getText().toString().trim();
        lName = cFname.getText().toString().trim();
        number = cPhone.getText().toString().trim();
        emailAddress = cEmail.getText().toString().trim();
        label1 = label.getText().toString().trim();

        updateContactToSystem(fname,number,emailAddress,label1);
    }
    private void updateContactToSystem(String name, String number, String email, String label){




    }
}
