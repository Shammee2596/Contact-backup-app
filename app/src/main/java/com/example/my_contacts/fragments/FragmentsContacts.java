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
    private List<ModelContact> contactList2 = new ArrayList<>();
    List<ModelContact> contactList1 = new ArrayList<>();
    AddNewContact addNewContact;

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
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        contactList2 = getContacts();
        displayContactList(new ContactStatus() {
            @Override
            public void dataLoaded(List<ModelContact> contactList) {

                contactList1.addAll(contactList2);
                contactList1.addAll(contactList);
                Contact_rv_adapter contactAapter = new Contact_rv_adapter(getContext(),contactList1);
                contactAapter.notifyDataSetChanged();
                recyclerView.setAdapter(contactAapter);
            }
        });

        return v;
    }

    private List<ModelContact> getContacts(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Contact");
        List<ModelContact> contactList = new ArrayList<>();
        contactList.clear();
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null, ContactsContract.Contacts.DISPLAY_NAME +" ASC");

        if (cursor != null){
            cursor.moveToFirst();
           do{

               // Get contact display name.
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                // Get contact phone type.
               String phoneTypeStr = "Mobile";
               int phoneTypeColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
               int phoneTypeInt = cursor.getInt(phoneTypeColumnIndex);
               if(phoneTypeInt== ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
               {
                   phoneTypeStr = "Home";
               }else if(phoneTypeInt== ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
               {
                   phoneTypeStr = "Mobile";
               }else if(phoneTypeInt== ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
               {
                   phoneTypeStr = "Work";
               }

               String emailAddress = "";
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor emails = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID +  " = ?", new String[]{contactId},null, null);

                while (emails.moveToNext())
                {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }

                contactList.add(new ModelContact(name,phone,emailAddress));
                System.out.println(emailAddress);
               /* databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue
                (new ModelContact(name,phone,emailAddress));*/
                emails.close();
            } while (cursor.moveToNext());
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

