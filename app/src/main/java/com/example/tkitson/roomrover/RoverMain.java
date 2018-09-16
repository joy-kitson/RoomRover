package com.example.tkitson.roomrover;

import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RoverMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover_main);

        TextView textView = findViewById(R.id.text);
        OnArrowClickListener textViewUpdater = new OnArrowClickListener(textView);

        //setup the Bluetooth button
        Button bluetooth_button = findViewById(R.id.bluetooth_button);
        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch to the device scanning activity when the button is pressed
                Intent deviceScanIntent =
                        new Intent(RoverMain.this, DeviceScanActivity.class);
                RoverMain.this.startActivity(deviceScanIntent);
            }
        });

        //setup the arrow buttons
        Button left_arrow_button = findViewById(R.id.left_arrow_button);
        Button right_arrow_button = findViewById(R.id.right_arrow_button);

        Button up_arrow_button = findViewById(R.id.up_arrow_button);
        Button down_arrow_button = findViewById(R.id.down_arrow_button);

        left_arrow_button.setOnClickListener(textViewUpdater);
        right_arrow_button.setOnClickListener(textViewUpdater);
    }
}
