package com.example.smser;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;

public class AlertReciever extends BroadcastReceiver {

    Context mcontext;
    String text , no;
    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext = context;
        text = intent.getStringExtra("text");
        no = intent.getStringExtra("no");

        if(checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(no , null , text , null , null);
            Toast t = Toast.makeText(context , "Message Sent" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER | Gravity.BOTTOM , 0 , 0);
            t.show();
        }
        else{
            Toast t = Toast.makeText(context , "Permission Denied" , Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER | Gravity.BOTTOM , 0 , 0);
            t.show();
        }
    }

    public Boolean checkPermission(String perm)
    {
        int check = ContextCompat.checkSelfPermission(mcontext , perm);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
