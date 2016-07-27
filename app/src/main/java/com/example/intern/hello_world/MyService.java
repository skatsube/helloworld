package com.example.intern.hello_world;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
public class MyService extends Service {
    private Handler hand = new Handler();
    public cpuInfo reader = new cpuInfo();
    private int Flag =0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hand.postDelayed( func, 1000);
        Flag = 1 - Flag;
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private final Runnable func= new Runnable() {
        @Override
        public void run() {
            //Log.i("CPU_VALUES_LINE","a");
            statusBarNotify();
            if(Flag == 1){
                hand.postDelayed( func, 1000);
            }

        }
    };

    private void statusBarNotify(){
        //RemoteViews views = new RemoteViews(getPackageName(), R.layout.activity_main);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("温度:CPU使用率")
                .setContentText(reader.fileRead() + reader.cpuRead(1) + "")
                .setTicker("notification is displayed");
        //

        //
        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());

    }
}
