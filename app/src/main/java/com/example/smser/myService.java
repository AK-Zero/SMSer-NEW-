package com.example.smser;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class myService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String text = intent.getStringExtra("text");
        String no = intent.getStringExtra("no");
        int hour = intent.getIntExtra("hour" , 0);
        int minute = intent.getIntExtra("minute" , 0);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY , hour);
        c.set(Calendar.MINUTE , minute);
        c.set(Calendar.SECOND , 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this , AlertReciever.class);
        intent1.putExtra("text" , text);
        intent1.putExtra("no" , no);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext() , 1 , intent1 , PendingIntent.FLAG_UPDATE_CURRENT);

        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE , 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP , c.getTimeInMillis() , pendingIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
