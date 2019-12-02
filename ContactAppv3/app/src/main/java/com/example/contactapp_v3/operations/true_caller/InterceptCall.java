package com.example.contactapp_v3.operations.true_caller;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.contactapp_v3.MainActivity;
import com.example.contactapp_v3.R;
import com.example.contactapp_v3.models.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InterceptCall extends BroadcastReceiver {
    public int cnt = 0;
    public int cnt2 = 0;
    public int maxCnt = 0;
    AtomicInteger atomicInteger = new AtomicInteger(0);
    TelephonyManager telephony;
    ValueEventListener listener;
    int YOURAPP_NOTIFICATION_ID = 1234567890;
    NotificationManager mNotificationManager;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (telephony != null) return;
        telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        telephony.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, final String incomingNumber) {
                String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if (state1.equals(TelephonyManager.EXTRA_STATE_IDLE)
                        || state1.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    try {
                        MainActivity.referenceTrueCaller.child(incomingNumber).removeEventListener(listener);

                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
                if (state1.equals(TelephonyManager.EXTRA_STATE_RINGING) && state == 1 && maxCnt >= cnt) {
                    maxCnt = cnt;
                    cnt++;

                    atomicInteger.set(1);
                    Toast.makeText(context, "Ringing - " + cnt, Toast.LENGTH_SHORT).show();
                    mNotificationManager = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);
                    final List<String> names = new ArrayList<>();

                    System.out.println("incomingNumber : " + cnt + " " + incomingNumber);
                    listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            names.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String name = postSnapshot.getValue(String.class);
                                names.add(name);
                            }
                            System.out.println(names);
                            showNotification(notificationManager, context, incomingNumber, names);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    MainActivity.referenceTrueCaller.child(incomingNumber).addValueEventListener(listener);

                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void showNotification(NotificationManagerCompat compat, Context context, String number, List<String> names) {
        String allNames = "";
        for (String name: names.subList(0, Math.min(names.size(), 3))){
            allNames += name + "\n";
        }
        Notification notification = new NotificationCompat.Builder(context, "channel1")
                .setSmallIcon(R.drawable.ic_person)
                .setContentTitle(number)
                .setContentText(allNames)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        compat.notify(1, notification);

    }
}
