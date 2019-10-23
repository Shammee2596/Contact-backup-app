package com.example.my_contacts.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_contacts.MainActivity;
import com.example.my_contacts.R;
import com.example.my_contacts.models.ModelCalls;

import java.util.List;

public class Calls_rv_adapters extends RecyclerView.Adapter<Calls_rv_adapters.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;

    private List<ModelCalls> callsList;

    public Calls_rv_adapters(Context context, List<ModelCalls> callsList) {

        this.context = context;
        this.callsList = callsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_calls,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        // Item Call
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView name, date, duration;
        name = holder.name;
        date = holder.date;
        duration = holder.duration;

        name.setText(callsList.get(position).getName());
        date.setText(callsList.get(position).getDate());
        duration.setText(callsList.get(position).getDuration());
    }

    @Override
    public int getItemCount() {
        return callsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, date, duration;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.contact_call_name);
            date = itemView.findViewById(R.id.call_date);
            duration = itemView.findViewById(R.id.call_duration);
            button = itemView.findViewById(R.id.call_button);
        }
    }

}
