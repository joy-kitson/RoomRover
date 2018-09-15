package com.example.tkitson.roomrover;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnArrowClickListener implements View.OnClickListener {
    private final TextView updateThis;
    public OnArrowClickListener(TextView view){
        this.updateThis = view;
    }

    @Override
    public void onClick(View v) {
        //we assume the view we're listening to is a button
        Button button = (Button) v;

        //and update updateThis to say that b was pressed
        updateThis.setText(String.format("%s was pressed last", button.getText()));
    }
}
