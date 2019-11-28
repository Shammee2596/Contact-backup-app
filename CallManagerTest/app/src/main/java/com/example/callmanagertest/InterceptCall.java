package com.example.callmanagertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

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



    private void endCall(Context context) {
//        try {
//            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            Class<?> c = Class.forName(telephony.getClass().getName());
//
//            Method m = c.getDeclaredMethod("getITelephony");
//            m.setAccessible(true);
//
//            ITelephony telephonyService = (ITelephony) m.invoke(telephony);
//
//            // Funciona en 2.2
//            // Funciona en 2.3.3
//            telephonyService.endCall();
//
//            logManager.debug("ITelepony was used (endCall)");
//        } catch (Exception e) {
//            logManager.error("Error ending call: " + e.getMessage());
//            logManager.debug("Error ending call", e);
//        }
    }

}
