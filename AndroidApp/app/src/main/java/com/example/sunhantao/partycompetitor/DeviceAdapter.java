package com.example.sunhantao.partycompetitor;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;

public class DeviceAdapter  extends ArrayAdapter {
    private List<BluetoothDevice> devices;
    private MainActivity context;
    private int layout;
    OutputStream mOutputStream;
    ProgressDialog progressDialog;
    boolean pairing;
    private List<BluetoothGattService> mServiceList;
    public DeviceAdapter(Context cont, int textViewResourceId, List<BluetoothDevice> objects) {
        super(cont, textViewResourceId, objects);
        devices = objects;
        context = (MainActivity) cont;
        layout = textViewResourceId;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(layout, null);
        final BluetoothDevice bd = devices.get(position);
        TextView deviceInfo = view.findViewById(R.id.device_info);
        final TextView pair = view.findViewById(R.id.pair);
        final TextView connect = view.findViewById(R.id.connect);
        ConstraintLayout cl = view.findViewById(R.id.constraintLayout);
        if(bd.getBondState() == BluetoothDevice.BOND_BONDED) {
            pair.setVisibility(View.VISIBLE);
        } else
            pair.setVisibility(View.GONE);
        if(bd.getName()!=null)
            deviceInfo.setText(bd.getName() + "(" + bd.getAddress() + ")");
        else
            deviceInfo.setText(bd.getAddress());
        cl.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(bd.getBondState() == BluetoothDevice.BOND_BONDED) {
                    pairing = false;
                    progressDialog.setMessage("Connecting");
                    progressDialog.show();
                    context.cancelScan();
                    new Thread() {
                        public void run() {
                            BluetoothSocket bluetoothSocket;
                            try {
                                bluetoothSocket = bd.createInsecureRfcommSocketToServiceRecord(UUID
                                        .fromString("00001101-0000-1000-8000-00805F9B34FB"));
                                if(!bluetoothSocket.isConnected()) {
                                    bluetoothSocket.connect();
                                }
                                context.setBts(bluetoothSocket);
                                mOutputStream = bluetoothSocket.getOutputStream();
                                if (progressDialog.isShowing() && progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }}.start();
                }else{
                    pairing = true;
                    progressDialog.setMessage("Pairing");
                    progressDialog.show();
                        bd.createBond();
                        Log.e("e",""+position);
                        context.setPosi(position);
                }
            }
        });
        return view;
    }

    void setDevices(List<BluetoothDevice> d){
        this.devices = d;
        notifyDataSetChanged();
    }

    void addDevice(BluetoothDevice bd){
        devices.add(bd);
        notifyDataSetChanged();
    }
    void refresh(){
        notifyDataSetChanged();
        if (progressDialog.isShowing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    void dismissDialog(){
        if (progressDialog.isShowing() && progressDialog != null) {
            progressDialog.dismiss();
        }
    }
    boolean isPairing(){
        return pairing;
    }
}
