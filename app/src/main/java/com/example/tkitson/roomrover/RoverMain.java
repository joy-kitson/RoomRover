package com.example.tkitson.roomrover;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class RoverMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rover_main);

        TextView textView = findViewById(R.id.text);
        OnArrowClickListener textViewUpdater = new OnArrowClickListener(textView);

        Button left_arrow_button = findViewById(R.id.left_arrow_button);
        Button right_arrow_button = findViewById(R.id.right_arrow_button);

        left_arrow_button.setOnClickListener(textViewUpdater);
        right_arrow_button.setOnClickListener(textViewUpdater);
    }
}
