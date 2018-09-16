package com.example.tkitson.roomrover;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

public class ControllerActivity extends Activity {
    private BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        device = (BluetoothDevice) getIntent()
                .getParcelableExtra(BluetoothDevice.class.getName());

    }
}
