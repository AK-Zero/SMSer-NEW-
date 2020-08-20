package com.example.smser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    EditText text_msg , phone_no;
    Button choosetime ;
    String text , no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermission(Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.SEND_SMS} , 1);
        }
        text_msg = findViewById(R.id.Text_Msg);
        phone_no = findViewById(R.id.Phone_No);
        choosetime = findViewById(R.id.choose_time);
        choosetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFrag();
                timepicker.show(getSupportFragmentManager() , "TimePicker");
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY , hourOfDay);
        c.set(Calendar.MINUTE , minute);
        c.set(Calendar.SECOND , 0);
        text = text_msg.getText().toString().trim();
        no = phone_no.getText().toString().trim();
        if(!text.isEmpty() && no.length()==10) {
            sendSMS(hourOfDay , minute , c);
            Toast t = Toast.makeText(MainActivity.this , "SMS scheduled at " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + " Hours..."  , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER | Gravity.BOTTOM , 0 , 0);
            t.show();
        }
        else{
            Toast t = Toast.makeText(MainActivity.this , "Enter the text and phone number first!!/nThen pick Delivery Time Again.. " , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER | Gravity.BOTTOM , 0 , 0);
            t.show();
        }
    }

//+ DateFormat.getTimeInstance(DateFormat.SHORT).format(c)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendSMS(int hour , int minute , Calendar c){
        Intent intent = new Intent(this,myService.class);
        intent.putExtra("text" , text);
        intent.putExtra("no" , no);
        intent.putExtra("hour" , hour);
        intent.putExtra("minute" , minute);
        ContextCompat.startForegroundService(this, intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(new Intent(MainActivity.this, myService.class));
            }
        },c.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()+5000);
    }
    public Boolean checkPermission(String perm)
    {
        int check = ContextCompat.checkSelfPermission(this, perm);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
