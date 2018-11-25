package com.example.arsalansiddiq.beem.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by jellani on 9/30/2018.
 */

public class BreakService extends Service {

    private final static int NOTIFICATION_ID = 95;

    private static final String LOG_TAG = BreakService.class.getName();
    public static final String COUNTDOWN_BR = "com.example.arsalansiddiq.beem";

    Intent intent = new Intent(COUNTDOWN_BR);

    @Override
    public void onCreate() {

        final Handler handler = new Handler();
        try {
            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {

                    intent.putExtra("GetTaskList", true);
                    sendBroadcast(intent);
                    Log.i(LOG_TAG, "Handler From Server");

                }
            }, 20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle startService() if you need to
        // for exmple if you are passing data in your intent
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the notification when the service is stopped
    }
}
