package com.example.sunhantao.partycompetitor;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION=10;
    ProgressDialog progressDialog;
    Button cntBtn;
    ListView deviceList;
    BluetoothAdapter mBluetoothAdapter;
    BroadcastReceiver mBluetoothReceiver;
    DeviceAdapter da;
    List<BluetoothDevice> unBondedDevices;
    List<BluetoothDevice> bondedDevices;
    List<BluetoothDevice> allDevices;
    public int posi;
    BluetoothSocket bts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        posi = 0;
        bts = null;
        cntBtn = findViewById(R.id.connect);
        deviceList = findViewById(R.id.devices);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter.isEnabled()){
            unBondedDevices = new ArrayList<>();
            cntBtn.setText("Finding");
            generateList();
        }
        unBondedDevices = new ArrayList<>();
        bondedDevices = new ArrayList<>();
        allDevices = new ArrayList<>();
        mBluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                final BluetoothDevice device;
                if(BluetoothDevice.ACTION_FOUND.equals(action) ) {
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        da.addDevice(device);
//                        unBondedDevices.add(device);
                    } else{
//                        bondedDevices.add(device);
                    }
                }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    cntBtn.setText(R.string.find);
                } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                    Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
                        showSuc();
                }
            }
        };
        IntentFilter btFilter = new IntentFilter();
        btFilter.setPriority(1000);
        btFilter.addAction(BluetoothDevice.ACTION_FOUND);
        btFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        btFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        btFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        this.registerReceiver(mBluetoothReceiver,btFilter);
        cntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBluetoothAdapter.isEnabled()) {
                    requestBluetoothPermission();
                    mBluetoothAdapter.enable();
                    progressDialog.show();
                    while(!mBluetoothAdapter.isEnabled()){
                    }
                    if (progressDialog.isShowing() && progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    cntBtn.setText(R.string.find);
                    unBondedDevices = new ArrayList<>();
                    cntBtn.setText("Finding");
                    generateList();
                } else{
                    unBondedDevices = new ArrayList<>();
                    cntBtn.setText("Finding");
                    generateList();
                }
            }
        });
    }

    private void requestBluetoothPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this, "Need bluetooth permission.",
                            Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_BLUETOOTH_PERMISSION);
            }
        }
    }

    public void generateList() {
        allDevices = new ArrayList<>();
        for(BluetoothDevice bd : mBluetoothAdapter.getBondedDevices()){
            allDevices.add(bd);
        }
        da = new DeviceAdapter(MainActivity.this, R.layout.device, allDevices);
        deviceList.setAdapter(da);
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void setPosi(int p){
        posi = p;
    }

    void showSuc() {

        final AlertDialog.Builder winnerDialog =
                new AlertDialog.Builder(MainActivity.this);
        winnerDialog.setTitle("Success!");
        winnerDialog.setMessage("Do you want to play right now?");
        winnerDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unregisterReceiver(mBluetoothReceiver);
                        ((GlobalBlueSocket) getApplication()).setGlobalBlueSocket(bts);
                        Intent intent = new Intent(MainActivity.this, SignActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        winnerDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                da.dismissDialog();
                try {
                    if(bts != null) {
                            bts.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        winnerDialog.show();
    }

    void setBts(BluetoothSocket socket){
        bts = socket;
    }

    void cancelScan(){
        mBluetoothAdapter.cancelDiscovery();
    }

}
