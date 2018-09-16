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
import java.util.UUID;

//based on example from:
//https://developer.android.com/guide/topics/connectivity/bluetooth-le
public class DeviceScanActivity extends ListActivity {
    private ArrayList<BluetoothDevice> devices;

    private BluetoothAdapter bluetoothAdapter;
    private boolean isScanning;
    private Handler handler;
    private DeviceScanAdapter deviceScanAdapter;

    private ListView listView;

    static final UUID[] SERVICE_UUIDS =
            {new UUID(0x19b10000e8f2537eL,0x4f6cd104768a1214L)};


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
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(resource, parent, false);

            TextView nameView = (TextView)rowView.findViewById(R.id.bt_text_view);
            nameView.setText(getItem(position).getAddress());
            nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //switch to the robot controlling activity when the button is pressed
                    Intent controllerIntent =
                            new Intent(DeviceScanActivity.this, ControllerActivity.class);
                    controllerIntent.putExtra(BluetoothDevice.class.getName(),
                            getItem(position));
                    DeviceScanActivity.this.startActivity(controllerIntent);
                }
            });

            this.notifyDataSetChanged();
            return rowView;
        }
    }

    // Stops scanning after 1 second.
    private static final long SCAN_PERIOD = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        handler = new Handler();

        Toast toast = Toast.makeText(this, "creating DeviceScanActivity", Toast.LENGTH_SHORT);
        toast.show();
        setContentView(R.layout.activity_device_scan);
        devices = new ArrayList<>();
        deviceScanAdapter = new DeviceScanAdapter(this, R.layout.bt_row_layout);

        listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(deviceScanAdapter);
        deviceScanAdapter.notifyDataSetChanged();

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
                        deviceScanAdapter.add(device);
                        deviceScanAdapter.notifyDataSetChanged();
                        Toast toast = Toast.makeText(DeviceScanActivity.this, "adding device", Toast.LENGTH_SHORT);
                        toast.show();
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
            bluetoothAdapter.startLeScan(SERVICE_UUIDS, scanCallback);
        } else {
            isScanning = false;
            bluetoothAdapter.stopLeScan(scanCallback);
        }
    }
}
