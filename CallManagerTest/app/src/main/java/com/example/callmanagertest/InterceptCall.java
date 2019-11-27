package com.example.callmanagertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class InterceptCall extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {
            Toast.makeText(context, "Ringing", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                System.out.println("incomingNumber : "+incomingNumber);
                Toast.makeText(context, incomingNumber, Toast.LENGTH_LONG).show();
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }
}
