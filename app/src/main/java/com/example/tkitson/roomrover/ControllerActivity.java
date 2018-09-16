package com.example.tkitson.roomrover;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ControllerActivity extends Activity {
    private BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_layout);

        device = (BluetoothDevice) getIntent()
                .getParcelableExtra(BluetoothDevice.class.getName());

        TextView textView = findViewById(R.id.text);
        OnArrowClickListener textViewUpdater = new OnArrowClickListener(textView);

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
