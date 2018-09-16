package com.example.tkitson.roomrover;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//based on example from:
//https://developer.android.com/guide/topics/connectivity/bluetooth-le
public class DeviceScanActivity extends ListActivity {
    private ArrayList<BluetoothDevice> devices;

    private ArrayList<String> stringDevices;

    private BluetoothAdapter bluetoothAdapter;
    private boolean isScanning;
    private Handler handler;
    private DeviceScanAdapter deviceScanAdapter;
    private DeviceStringAdapter deviceStringAdapter;

    private ListView listView;


    private class DeviceStringAdapter extends ArrayAdapter<String> {
        static final int resource = R.layout.bt_row_layout;

        public DeviceStringAdapter(@NonNull Context context, int resource) {
            super(context, resource, stringDevices);
        }

        @Override
        public void add(@Nullable String object) {
            super.add(object);
            Toast toast = Toast.makeText(DeviceScanActivity.this, "adding device", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(resource, parent, false);
            TextView nameView = (TextView)rowView.findViewById(R.id.bt_text_view);
            nameView.setText(getItem(position));
            return rowView;
        }
    }


    private class DeviceScanAdapter extends ArrayAdapter<BluetoothDevice> {
        static final int resource = R.layout.bt_row_layout;

        public DeviceScanAdapter(@NonNull Context context, int resource) {
            super(context, resource, devices);
        }

        @Override
        public void add(@Nullable BluetoothDevice object) {
            super.add(object);
            Toast toast = Toast.makeText(DeviceScanActivity.this, "adding device", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(resource, parent, false);
            TextView nameView = (TextView)rowView.findViewById(R.id.bt_text_view);
            nameView.setText(getItem(position).getName());
            return rowView;
        }
    }






    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        handler = new Handler();

        Toast toast = Toast.makeText(this, "creating DeviceScanActivity", Toast.LENGTH_SHORT);
        toast.show();
        setContentView(R.layout.activity_device_scan);
        devices = new ArrayList<>();
        //deviceScanAdapter = new DeviceScanAdapter(this, R.layout.bt_row_layout);

        stringDevices = new ArrayList<>();
        stringDevices.add("Hello");
        stringDevices.add("there!");
        deviceStringAdapter = new DeviceStringAdapter(this, R.layout.bt_row_layout);

        listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(deviceStringAdapter);
        deviceStringAdapter.notifyDataSetChanged();

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        scanForLeDevice(true);


    }

    private void scanForLeDevice(final boolean enable){
        final BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //deviceScanAdapter.add(device);
                        //deviceScanAdapter.notifyDataSetChanged();
                        deviceStringAdapter.add("Here is a thing");
                        deviceStringAdapter.notifyDataSetChanged();
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
