package com.example.contactapp_v3.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp_v3.R;
import com.example.contactapp_v3.models.Contact;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Contact> contactList;

    public TrashAdapter (Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_trash,parent,false);
        final ViewHolder viewHolder = new TrashAdapter.ViewHolder(view);

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo: restore to contact list
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


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, number;
        Button button;

        LinearLayout item_contact;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_contact = itemView.findViewById(R.id.trash_item);
            name = itemView.findViewById(R.id.contact_name1);
            number = itemView.findViewById(R.id.number1);
            button = itemView.findViewById(R.id.restore_button);

        }

    }
}
