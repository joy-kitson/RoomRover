package com.example.tkitson.roomrover;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//based on example from:
//https://developer.android.com/guide/topics/connectivity/bluetooth-le
public class DeviceScanActivity extends ListActivity {
    private ArrayList<BluetoothDevice> devices;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isScanning;
    private Handler handler;
    private DeviceScanAdapter deviceScanAdapter;

    private class DeviceScanAdapter extends ArrayAdapter<BluetoothDevice> {

        public DeviceScanAdapter(@NonNull Context context, int resource) {
            super(context, resource, devices);
        }
    }


    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Toast toast = Toast.makeText(this, "creating DeviceScanActivity", Toast.LENGTH_SHORT);
        toast.show();
        setContentView(R.layout.activity_device_scan);
        devices = new ArrayList<>();
        deviceScanAdapter = new DeviceScanAdapter(this, android.R.layout.simple_list_item_1);

    }

    private void scanForLeDevice(final boolean enable){
        final BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deviceScanAdapter.add(device);
                        deviceScanAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        if (enable) {
            //set things up so that the we've stop scanning after a while
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    bluetoothAdapter.stopLeScan(scanCallback);
                }
            }, SCAN_PERIOD);

            isScanning = true;
            bluetoothAdapter.startLeScan(scanCallback);
        } else {
            isScanning = false;
            bluetoothAdapter.stopLeScan(scanCallback);
        }
    }
}
