package com.example.my_contacts.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_contacts.AddNewContact;
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
import java.util.List;


public class FragmentsContacts extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    ModelContact contact;
    DatabaseReference databaseReference;
    private List<ModelContact> contactList = new ArrayList<>();
    List<ModelContact> list;

    public FragmentsContacts() {
        // some changes
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_contacts,container,false);
        recyclerView = v.findViewById(R.id.rv_contacts);
        TextView createNewContact = v.findViewById(R.id.addContact);
        createNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewContact.class);
                // hhh
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        contactList = getContacts();
        List<ModelContact> contactList1 = new ArrayList<>();
        contactList1.addAll(displayContactList());


        System.out.println(contactList1.size());
        contactList1.addAll(contactList);
        System.out.println(contactList1.size());

        Contact_rv_adapter contactAapter = new Contact_rv_adapter(getContext(),contactList1);
        displayContactList();
        recyclerView.setAdapter(contactAapter);
        return v;
    }

    private List<ModelContact> getContacts(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Contact");
        List<ModelContact> contactList = new ArrayList<>();
        displayContactList();
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null, ContactsContract.Contacts.DISPLAY_NAME +" ASC");
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //String email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
            String emailAddress = "";
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor emails = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID +  " = ?", new String[]{contactId},null, null);

            while (emails.moveToNext())
            {
                emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            contactList.add(new ModelContact(name,phone,emailAddress));
           // databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue
                    //(new ModelContact(name,phone,emailAddress));
            emails.close();
        }


        cursor.close();

        return contactList;
    }



    public List<ModelContact> displayContactList(){
        list = new ArrayList<>();
        contact = new ModelContact();
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
                System.out.println(list.size());
                System.out.println("hi");
                /*Contact_rv_adapter contactAapter = new Contact_rv_adapter(getContext(),contactList);
                recyclerView.setAdapter(contactAapter);*///attaching adapter to the listview
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        System.out.println(list.size());
        return  list;
    }
}

