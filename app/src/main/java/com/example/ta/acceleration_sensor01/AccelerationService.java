package com.example.ta.acceleration_sensor01;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by yamada on 2015/08/07.
 */
public class AccelerationService extends Service {

    private static final String TAG = "AccelerationService";

    public IBinder onBind(Intent intent){
        return null;
    }

    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d(TAG,"onStartCommand");
        return START_STICKY;
    }

    public void onDestroy(){
        Log.d(TAG,"onDestroy");
    }

}
