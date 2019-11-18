package com.example.my_contacts.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_contacts.AddNewContact;
import com.example.my_contacts.MainActivity;
import com.example.my_contacts.R;
import com.example.my_contacts.adapters.Contact_rv_adapter;
import com.example.my_contacts.models.ModelContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;


public class FragmentsContacts extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    ModelContact contact;
    DatabaseReference databaseReference;
    List<ModelContact> contactList1 = new ArrayList<>();
    List<ModelContact> contactList2 = new ArrayList<>();
    List<ModelContact> currentValueList = new ArrayList<>();
    AddNewContact addNewContact;
    EditText editTextSearchContact;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_contacts,container,false);
        recyclerView = v.findViewById(R.id.rv_contacts);

        // Add new contact
        TextView createNewContact = v.findViewById(R.id.addContact);
        createNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewContact.class);
                startActivity(intent);
            }
        });

        editTextSearchContact = v.findViewById(R.id.editTextSearchContact);

        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        while (getContacts1() == null);

        contactList2 = getContacts1();

        displayContactList(new ContactStatus() {
            @Override
            public void dataLoaded(List<ModelContact> contactList) {
                contactList1.addAll(contactList2);
                contactList1.addAll(contactList);
                Collections.sort(contactList1);
//                System.out.println(contactList1.size());
//                Set<ModelContact> set = new HashSet<ModelContact>(contactList1);
//                contactList1 = new ArrayList<ModelContact>();
//                for (ModelContact mc: set){
//                    System.out.println(set.size());
//                    contactList1.add(mc);
//                }
//
//                System.out.println(contactList1.size());
                Contact_rv_adapter contactAapter = new Contact_rv_adapter(getContext(),contactList1);
                contactAapter.notifyDataSetChanged();
                contactAapter.setContactList(contactList1);
                recyclerView.setAdapter(contactAapter);
            }
        });

        editTextSearchContact.addTextChangedListener(new TextWatcher() {
            int textLength = 0;
            public void afterTextChanged(Editable s){

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextSearchContact.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textLength = editTextSearchContact.getText().length();
                String text = editTextSearchContact.getText().toString();
                currentValueList.clear();
                for (ModelContact currentContact: contactList1) {
                    String name = currentContact.getName();
                    String number = currentContact.getNumber();
                    String email = currentContact.getEmail();
                    if (textLength <= name.length()) {
                        if(name.toLowerCase().contains(editTextSearchContact.getText().toString().toLowerCase())) {
                            currentValueList.add(new ModelContact(name, currentContact.getNumber(), currentContact.getEmail()));
                        }
                    }
                    if (textLength <= number.length()) {
                        if(number.toLowerCase().contains(editTextSearchContact.getText().toString().toLowerCase().trim())) {
                            currentValueList.add(new ModelContact(name, number, currentContact.getEmail()));
                        }
                    }
                   /*if (textLength <= email.length()) {
                        if(email.toLowerCase().contains(editTextSearchContact.getText().toString().toLowerCase())) {
                            currentValueList.add(new ModelContact(name, number, email));
                        }
                    }*/
                }
                Collections.sort(currentValueList);
                recyclerView.setAdapter(new Contact_rv_adapter((getContext()), currentValueList));
            }
        });

        return v;
    }

    public List<ModelContact> getContacts1(){
        List<ModelContact> contactList = new ArrayList<>();

        String phoneNumber="";
        String name;
        ModelContact modelContact = new ModelContact();

        final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor cursor = null;

        try {
            cursor = getContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, ContactsContract.Contacts.DISPLAY_NAME +" ASC");
        } catch (Exception e) {
            return null;
        }

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));


                if (hasPhone.equalsIgnoreCase("1"))
                    hasPhone = "true";
                else
                    hasPhone = "false";

                if (Boolean.parseBoolean(hasPhone)) {
                    Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
                            new String[]{contactId}, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    phones.close();
                }

                // Find Email Addresses
                Cursor emails = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =" + contactId,
                        null, null);


                String emailAddress = null;
                while (emails.moveToNext()) {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                }
                emails.close();

                contactList.add(new ModelContact(name, phoneNumber, emailAddress));
                Log.e("Test", "Email: " + emailAddress + "  Name: " + name + "  Number: " + phoneNumber);
            }

            cursor.close();
        }
        return contactList;
    }

    public void displayContactList(final ContactStatus contactStatus){
        final List<ModelContact> list;
        list = new ArrayList<>();
        contact = new ModelContact();
        addNewContact = new AddNewContact();

        databaseReference = FirebaseDatabase.getInstance().getReference("Contact")
                .child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.keepSynced(true); // to show the data offline

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    contact = postSnapshot.getValue(ModelContact.class); //getting contacts
                    list.add(contact); //adding contacts to the list
                }
                contactStatus.dataLoaded(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public interface ContactStatus{
        public void dataLoaded(List<ModelContact>contactList);
    }
}

