package com.example.callmanagertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    ConnectionReceiver receiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                                Manifest.permission.PROCESS_OUTGOING_CALLS,
                                Manifest.permission.READ_CALL_LOG, Manifest.permission.MANAGE_OWN_CALLS
                        }, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(receiver, intentFilter);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        unregisterReceiver(receiver);
//    }
//
//   // @OnClick(R.id.button)
    public void someMethod(View view) {
        TelephonyManager telephonyManager = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        String phone = telephonyManager.getLine1Number();
        Toast.makeText(MainActivity.this, phone, Toast.LENGTH_LONG).show();
    }
}
