package com.pfp.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import ObjectClasses.SpaceType;

public class SearchFiltersActivity extends AppCompatActivity {

    private SpaceType type;
    private EditText lowestPriceField, highestPriceField;
    private DatePicker startDatePicker, endDatePicker;
    private TimePicker startTimePicker, endTimePicker;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        //Give filters very broad default values;
        lowestPriceField = (EditText) findViewById(R.id.lowestPriceField);
        lowestPriceField.setText(0);
        highestPriceField = (EditText) findViewById(R.id.highestPriceField);
        highestPriceField.setText("1000");

        Calendar cal = Calendar.getInstance();

        startDatePicker = (DatePicker) findViewById(R.id.startDate);
        startDatePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.YEAR, 1);
        endDatePicker = (DatePicker) findViewById(R.id.endDate);
        endDatePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        startTimePicker = (TimePicker) findViewById(R.id.startTime);
        startTimePicker.setHour(cal.get(Calendar.HOUR_OF_DAY));
        startTimePicker.setMinute(cal.get(Calendar.MINUTE));

        endTimePicker = (TimePicker) findViewById(R.id.endTime);
        endTimePicker.setHour(cal.get(Calendar.HOUR_OF_DAY));
        endTimePicker.setMinute(cal.get(Calendar.MINUTE));

        confirmButton = (Button) findViewById(R.id.confirmFilterButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TYPE", type);
                intent.putExtra("LOWESTPRICE", Integer.valueOf(lowestPriceField.getText().toString()));
                intent.putExtra("HIGHESTPRICE", Integer.valueOf(highestPriceField.getText().toString()));
                intent.putExtra("STARTYEAR", startDatePicker.getYear());
                intent.putExtra("STARTMONTH", startDatePicker.getMonth());
                intent.putExtra("STARTDAY", startDatePicker.getDayOfMonth());
                intent.putExtra("STARTHOUR", startTimePicker.getHour());
                intent.putExtra("STARTMINUTE", startTimePicker.getMinute());
                intent.putExtra("ENDYEAR", endDatePicker.getYear());
                intent.putExtra("ENDMONTH", endDatePicker.getMonth());
                intent.putExtra("ENDDAY", endDatePicker.getDayOfMonth());
                intent.putExtra("ENDHOUR", endTimePicker.getHour());
                intent.putExtra("ENDMINUTE", endTimePicker.getMinute());
                setResult(12321, intent);
                finish();
            }
        });
    }

    public void onTypeRadioButtonClicked(View view){
        switch (view.getId()) {
            case R.id.compactRadioButton:
                type = SpaceType.COMPACT;
                break;
            case R.id.truckRadioButton:
                type = SpaceType.TRUCK;
                break;
            case R.id.handicapRadioButton:
                type = SpaceType.DISABLED;
                break;
        }
    }
}