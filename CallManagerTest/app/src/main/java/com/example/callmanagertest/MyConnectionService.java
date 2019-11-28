package com.example.callmanagertest;

import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;


public class MyConnectionService extends ConnectionService {


    @Override
    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        System.out.println("++++++++++++++++++++");
        return super.onCreateIncomingConnection(connectionManagerPhoneAccount, request);
    }

}
