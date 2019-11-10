package com.example.my_contacts.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_contacts.ContactDetails;
import com.example.my_contacts.R;
import com.example.my_contacts.models.ModelContact;

import java.util.List;

public class Favourite_adapter extends RecyclerView.Adapter<Favourite_adapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;

    private List<ModelContact> contactList;

    public Favourite_adapter(Context context, List<ModelContact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }
    @NonNull
    @Override
    public Favourite_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_fav,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);


        viewHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(context, String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_LONG).show();
                String c_name=  contactList.get(viewHolder.getAdapterPosition()).getName();
                String c_number=  contactList.get(viewHolder.getAdapterPosition()).getNumber();
                String c_email=  contactList.get(viewHolder.getAdapterPosition()).getEmail();

                Intent intent = new Intent(context, ContactDetails.class);
                intent.putExtra("name",c_name);
                intent.putExtra("number",c_number);
                intent.putExtra("email",c_email);
                Log.e("msg","details is called from adapter class");
                context.startActivity(intent);
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, number;
        Button button;

        LinearLayout item_contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_contact = itemView.findViewById(R.id.item_fav);
            name = itemView.findViewById(R.id.fav_name);
            number = itemView.findViewById(R.id.fav_number);
            button = itemView.findViewById(R.id.fav_button);

        }
    }
}
