package com.example.tkitson.roomrover;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ControllerActivity extends Activity {
    private BluetoothDevice device;
    private BluetoothGatt gatt;

    private HashMap<UUID,BluetoothGattCharacteristic> characteristicHashMap =
            new HashMap<>();

    //from:
    //https://stackoverflow.com/questions/23393010/how-to-send-data-over-a-bluetooth-low-energy-ble-link
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            //Connection established
            if (status == BluetoothGatt.GATT_SUCCESS
                    && newState == BluetoothProfile.STATE_CONNECTED) {


                //based on answer to:
                //https://stackoverflow.com/questions/47887029/bluetooth-low-energy-ble-how-to-get-uuids-of-service-characteristic-and-des

                //Discover services
                gatt.discoverServices();
                for (BluetoothGattService service: gatt.getServices()){
                    for (BluetoothGattCharacteristic characteristic: service.getCharacteristics()){
                        if (!characteristicHashMap.containsKey(characteristic.getUuid()))
                            characteristicHashMap.put(characteristic.getUuid(), characteristic);
                    }
                }


            } else if (status == BluetoothGatt.GATT_SUCCESS
                    && newState == BluetoothProfile.STATE_DISCONNECTED) {

                //Handle a disconnect event

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            //Now we can start reading/writing characteristics

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_layout);


        device = (BluetoothDevice) getIntent()
                .getParcelableExtra(BluetoothDevice.class.getName());
        gatt = device.connectGatt(this,false,gattCallback);


        TextView textView = findViewById(R.id.text);
        OnArrowClickListener textViewUpdater = new OnArrowClickListener(textView);

        //setup the arrow buttons
        Button up_arrow_button = findViewById(R.id.up_arrow_button);
        up_arrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothGattCharacteristic characteristic =
                        characteristicHashMap.get(new UUID(0x19b10000e8f2L,
                                0x4f6cd104768a1214L));
                characteristic.setValue(2, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            }
        });

        /*
        int[] button_ids = {R.id.left_arrow_button,
                R.id.right_arrow_button,
                R.id.up_arrow_button,
                R.id.down_arrow_button};
        for (int i = 0; i < button_ids.length; i++){
            Button button = findViewById(button_ids[i]);
            button.setOnClickListener(textViewUpdater);
        }*/
    }
}
