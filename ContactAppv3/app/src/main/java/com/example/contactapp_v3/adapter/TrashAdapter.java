package com.example.contactapp_v3.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp_v3.R;
import com.example.contactapp_v3.listener.OnContactRestoreListener;
import com.example.contactapp_v3.models.Contact;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Contact> contactList;
    private OnContactRestoreListener restoreListener;

    public TrashAdapter (Context context, List<Contact> contactList,
                         OnContactRestoreListener restoreListener) {
        this.context = context;
        this.contactList = contactList;
        this.restoreListener = restoreListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_trash,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact currentContact = contactList.get(viewHolder.getAdapterPosition());
                Toast.makeText(context, currentContact.getName(), Toast.LENGTH_LONG).show();
                restoreListener.onRestoreClick(currentContact);
            }
        });
        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact currentContact = contactList.get(viewHolder.getAdapterPosition());
                new RestoreDialogFragment(context, currentContact);
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

    class RestoreDialogFragment extends DialogFragment {

        private Context context;
        private Contact contact;

        RestoreDialogFragment(Context context, Contact contact){
            this.context = context;
            this.contact = contact;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure to restore contact?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            restoreListener.onRestoreClick(contact);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }
}
