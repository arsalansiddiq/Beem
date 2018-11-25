package com.example.arsalansiddiq.beem.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.arsalansiddiq.beem.R;

/**
 * Created by jellani on 9/30/2018.
 */

public class MyService extends Service {
    private final static int NOTIFICATION_ID = 95;

    private static final String LOG_TAG = MyService.class.getName();
    public static final String COUNTDOWN_BR = "com.example.arsalansiddiq.beem";

    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;

    public static long TIME;

    private long remainigTime;
    private boolean isStop;

    private NotificationManager mNotificationManager;

    public MyService() { super(); }

    @Override
    public void onCreate() {
        // Initialize notification
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        String ns = Context.NOTIFICATION_SERVICE;

        Log.i(LOG_TAG, "Starting Timer..." + TIME);
        countDownTimer = new CountDownTimer(TIME, 1000) {
            @Override
            public void onTick(long l) {
                Log.i(LOG_TAG, "Countdown seconds remaining: " + l / 1000);
                remainigTime = l;
                intent.putExtra("GetTaskList", false);
                intent.putExtra("countDown", l);
                sendBroadcast(intent);


                if (TIME == 0) {
                    stopForeground(true);
                    stopSelf();
                    countDownTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                Log.i(LOG_TAG, "Timer finished");
            }
        };
        countDownTimer.start();
        // Build your notification here
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setSmallIcon(R.mipmap.beemlogo);
        mBuilder.setContentTitle("MyService");
        mBuilder.setContentText("Time Left " + remainigTime);

        // Launch notification
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

//    void startSerivces() {
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//
////        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(R.mipmap.beemlogo, "Time Left", remainigTime);
////        notificationManager.notify(NOTIFICATION_ID, notification);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle startService() if you need to
        // for exmple if you are passing data in your intent
        isStop = intent.getBooleanExtra("isStop", false);
        Log.i("isstop", String.valueOf(isStop));
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();
        // Remove the notification when the service is stopped
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
}
