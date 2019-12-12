package com.example.contactapp_v3.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp_v3.R;
import com.example.contactapp_v3.listener.OnContactDetailsListener;
import com.example.contactapp_v3.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Contact> contactList;
    final OnContactDetailsListener detailsListener;
    Drawable mDrawable;

    public ContactAdapter(Context context, List<Contact> contactList,
                          OnContactDetailsListener detailsListener) {
        this.context = context;
        this.contactList = contactList;
        this.detailsListener = detailsListener;
        mDrawable = ContextCompat.getDrawable(context, R.drawable.circle);

    }

    public void setContactList(List<Contact> contactList1){
        contactList = contactList1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_contacts,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // Item Call
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                String number1 = viewHolder.number.getText().toString();
                Toast.makeText(context, "Enable call phone", Toast.LENGTH_LONG).show();

                intentCall.setData(Uri.parse("tel:"+number1));

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Enable call phone in the settings", Toast.LENGTH_LONG).show();
                }else {
                    context.startActivity(intentCall);
                }
            }
        });

        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact currentContact = contactList.get(viewHolder.getAdapterPosition());
                detailsListener.onContactDetails(currentContact);
                Log.e("msg","details is called from adapter class");
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView name, number;
        name = holder.name;
        number = holder.number;

        name.setText(contactList.get(position).getName());
        number.setText(contactList.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, number;
        Button button;

        LinearLayout item_contact;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_contact = itemView.findViewById(R.id.contact_item1);
            name = itemView.findViewById(R.id.contact_name);
            number = itemView.findViewById(R.id.number);
            button = itemView.findViewById(R.id.contact_button);

        }
    }
}
