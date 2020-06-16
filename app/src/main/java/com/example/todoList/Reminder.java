package com.example.todoList;

import android.app.AlarmManager;
import android.support.v4.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

public class Reminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private int h;
    private int m;
    private int i1;
    private String name;
    private String year;
    private String month;
    private String day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);

        Button button1 = findViewById(R.id.setReminder);
        Button button2 = findViewById(R.id.cancelReminder);

        String strDate = getIntent().getStringExtra("date");
        //item = (Item) getIntent().getSerializableExtra("item1");
        name = getIntent().getStringExtra("name");
        //String description = getIntent().getStringExtra("description");
        i1 = getIntent().getIntExtra("id", i1);
        Log.d("ABC", name);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelReminder();
            }
        });

        String[] dateList = strDate.split("-");

        year = dateList[0];
        month = dateList[1];
        day = dateList[2];

    }



    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        h = hourOfDay;
        m = minute;
        //String time = h + ":" + (m);

        setReminder(i1,name,year,month,day,h,m);
        Toast.makeText(getApplicationContext(), "Reminder Set for " + h + ":" + m + " " + "at" + " " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));

    }


    private void setReminder(int i,String name,String year,String month,String day,int hour,int minute){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        intent.putExtra("notification_id",i);
        intent.putExtra("name",name);

        PendingIntent pendingintent = PendingIntent.getBroadcast(this,i,intent,0);

        Calendar calendar = Calendar.getInstance();


        Log.d("www","year");

        calendar.set(Calendar.YEAR,Integer.parseInt(year));
        calendar.set(Calendar.MONTH,Integer.parseInt(month)-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day));

        calendar.set(Calendar.HOUR_OF_DAY,(hour));
        calendar.set(Calendar.MINUTE,(minute));
        calendar.set(Calendar.SECOND,0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingintent);
        //sendBroadcast(intent);

    }

    private void cancelReminder(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(this,1,intent,0);

        alarmManager.cancel(pendingintent);
        Toast.makeText(getApplicationContext(), "Reminder Canceled for " + h + ":" + m + " " + "at" + " " + day + "-" + month + "-" + year, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));

    }



}
