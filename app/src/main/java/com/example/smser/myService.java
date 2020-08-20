package com.example.smser;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class myService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
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

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SMSer")
                .setContentText("SMS scheduled at " + hour + ":" + minute + " Hours...")
                .setSmallIcon(R.drawable.ic_textsms_black_24dp)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        
        

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY , hour);
        c.set(Calendar.MINUTE , minute);
        c.set(Calendar.SECOND , 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(this , AlertReciever.class);
        intent1.putExtra("text" , text);
        intent1.putExtra("no" , no);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext() , 1 , intent1 , PendingIntent.FLAG_UPDATE_CURRENT);

        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE , 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP , c.getTimeInMillis() , pendingIntent1);
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
