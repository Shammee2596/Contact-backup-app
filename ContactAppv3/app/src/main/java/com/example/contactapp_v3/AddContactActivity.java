package com.example.contactapp_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contactapp_v3.models.Contact;
import com.example.contactapp_v3.operations.ContactAddService;

public class AddContactActivity extends AppCompatActivity {

    private Contact contact;

    EditText fName, lName, number, email, label;
    Button saveButon;
    ImageView image;
    ContactAddService contactAddService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        contactAddService = new ContactAddService(this);

        fName = findViewById(R.id.createContactFName);
        lName = findViewById(R.id.createContactLName);
        number = findViewById(R.id.createContactNumber);
        email = findViewById(R.id.createContactEmail);
        label = findViewById(R.id.createContactLabel);
        saveButon = findViewById(R.id.createContactButton);

        saveButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact = new Contact();

                contact.setName(fName.getText().toString().trim());
                contact.setLastName(lName.getText().toString().trim());
                contact.setNumber(number.getText().toString().trim());
                contact.setEmail(email.getText().toString().trim());
                contact.setLabel(label.getText().toString().trim());

                contactAddService.addToPhoneStorage(contact);
            }
        });
    }
}
