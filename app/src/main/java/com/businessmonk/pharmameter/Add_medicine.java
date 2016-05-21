package com.businessmonk.pharmameter;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Add_medicine extends AppCompatActivity {
    private Spinner spinner1;
    TinyDB tinyDB ;
    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;
    EditText medName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder2);
        addListenerOnSpinnerItemSelection();
        tinyDB = new TinyDB(getApplicationContext());
        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);
        medName = (EditText)findViewById(R.id.meedName);
        // set current time into output textview
    }
    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void ok(View view) {
        switch (tinyDB.getInt("med_count")){
            case 0:
                Reminder.interval.add("Once a day");
            case 1:
                Reminder.interval.add("Twice a day");
            case 2:
                Reminder.interval.add("3 time a day");
            case 3:
                Reminder.interval.add("4 times a day");
        }
        Reminder.med_name.add(medName.getText().toString());
        Log.e("hiii",Reminder.med_name.get(1));
        this.finish();
    }


    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
            LinearLayout myLayout = (LinearLayout) findViewById(R.id.frequency);
            Log.e("iddd",pos+"");
            if(findViewById(Integer.valueOf(1))==null) {
                tinyDB.putInt("med_count",pos);
                for(int i = 0 ;i<=pos;i++) {

                    Log.e("added","add without deleete btn "+(i+1)+"");
                    final Button myButton = new Button(getApplicationContext());
                    myButton.setId(Integer.valueOf(i+1));
                    myButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT));
                    ;
                    myButton.setText("Click to set Time");
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog(TIME_DIALOG_ID);
                            updateTime(hour, minute,myButton);
                        }
                    });
                    myLayout.addView(myButton);
                }
            }else{
                for(int i = tinyDB.getInt("med_count") ; i >=0 ;i--) {
                    Log.e("removed","remove btn "+(i+1)+"");
                    ((ViewManager) findViewById(Integer.valueOf(i+1)).getParent()).removeView(findViewById(Integer.valueOf(i+1)));
                }
                tinyDB.putInt("med_count",pos);
                for(int i = 0 ;i<=pos;i++) {
                    Log.e("added","add after delete btn "+(i+1)+"");
                    final Button myButton = new Button(getApplicationContext());
                    myButton.setId(Integer.valueOf(i + 1));
                    myButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT));
                    ;
                    myButton.setText("Click to set Time");
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tinyDB.putInt("clicked",view.getId());
                            showDialog(TIME_DIALOG_ID);
                            updateTime(hour, minute,myButton);
                        }
                    });
                    myLayout.addView(myButton);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;


        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins,Button m) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        m.setText(aTime);
    }
}
