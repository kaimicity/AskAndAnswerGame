package com.example.sunhantao.partycompetitor;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class GlobalBlueSocket extends Application {
    BluetoothSocket globalBlueSocket = null;

    public void setGlobalBlueSocket(BluetoothSocket globalBlueSocket){
        this.globalBlueSocket = globalBlueSocket;
    }
    public BluetoothSocket getGlobalBlueSocket(){
        return globalBlueSocket;
    }
}
