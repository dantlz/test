package com.pfp.parkhere;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by tshih on 10/21/16.
 */

public class MyBookingsDetailsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings_details);

        //get information passed from mybookings activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        TextView addressView = (TextView) findViewById(R.id.address);
        addressView.setTextColor(Color.BLACK);
        addressView.setText(extras.getString("ADDRESS_TEXT"));

        //set start and end times
        String times = "START: " + extras.getString("START_TIME_TEXT") + "\n"+ "END: " + extras.getString("END_TIME_TEXT");
        TextView timesView = (TextView) findViewById(R.id.start_end_time);
        timesView.setTextColor(Color.BLACK);
        //textView.setTextSize(20);

        timesView.setText(times);

        TextView nameView = (TextView) findViewById(R.id.owner_name);
        nameView.setText(extras.getString("OWNER_NAME_TEXT"));
        TextView emailView = (TextView) findViewById(R.id.owner_email);
        emailView.setText(extras.getString("OWNER_EMAIL_TEXT"));
    }
}
