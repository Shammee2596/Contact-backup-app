package com.example.my_contacts.fragments;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_contacts.R;
import com.example.my_contacts.adapters.Calls_rv_adapters;
import com.example.my_contacts.models.ModelCalls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_Calls extends Fragment {

    private View v;
    private RecyclerView recyclerView;

    public Fragment_Calls() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_calls,container,false);
        recyclerView = v.findViewById(R.id.rv_calls);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = manager;
        recyclerView.setLayoutManager(layoutManager);

        Calls_rv_adapters callsAapter = new Calls_rv_adapters(getContext(),getCallLogs());
        recyclerView.setAdapter(callsAapter);
        return v;
    }
    private List<ModelCalls> getCallLogs(){

        List<ModelCalls> list = new ArrayList<>();


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_CALL_LOG}, 1);

        }
        Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null,null,null, CallLog.Calls.DATE +" DESC");

        int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        //startManagingCursor(cursor);


        cursor.moveToFirst();
        while (cursor.moveToNext()){
            Date date1 = new Date(Long.valueOf(cursor.getString(date)));
            String time, day, month;
            String Name;

            time = (String) DateFormat.format("dd",date1);
            day = (String) DateFormat.format("EEEE",date1);
            month = (String) DateFormat.format("MMMM",date1);

            String name1 =cursor.getString(name);
            if(name1==null){
                name1 = "no name";
            }

            list.add(new ModelCalls(name1,cursor.getString(number),cursor.getString(duration),day+", "+time+" "+month+" "));

            Log.d("Mic", cursor.getString(number));
        }


        cursor.close();
        return list;
    }
}
