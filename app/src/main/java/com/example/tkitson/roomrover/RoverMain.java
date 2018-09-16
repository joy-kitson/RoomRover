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
        int[] button_ids = {R.id.left_arrow_button,
                            R.id.right_arrow_button,
                            R.id.up_arrow_button,
                            R.id.down_arrow_button};
        for (int i = 0; i < button_ids.length; i++){
            Button button = findViewById(button_ids[i]);
            button.setOnClickListener(textViewUpdater);
        }
    }
}
