package com.example.intern.hello_world;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {
    private Handler hand = new Handler();
    public CpuInfo reader = new CpuInfo();
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
                .setContentText(reader.fileRead() + reader.cpuRead()[1
                        ] + "%")
                .setTicker("notification is displayed");
        int mNotificationId = 001;
        //
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(contentIntent);
        //
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());

    }
}
