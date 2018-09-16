package com.example.tkitson.roomrover;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RoverMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover_main);

        //setup the Bluetooth button
        Button bluetooth_button = findViewById(R.id.bluetooth_button);
        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast toast = Toast.makeText(RoverMain.this, "switching to DeviceScanActivity", Toast.LENGTH_SHORT);
                //toast.show();

                //switch to the device scanning activity when the button is pressed
                Intent deviceScanIntent =
                        new Intent(RoverMain.this, DeviceScanActivity.class);
                RoverMain.this.startActivity(deviceScanIntent);
            }
        });
    }
}
