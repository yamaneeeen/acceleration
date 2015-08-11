package com.example.ta.acceleration_sensor01;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Created by yamada on 2015/08/07.
 */
public class AccelerationService extends Service implements SensorEventListener {

    public static final String TAG = "AccelerationService";
    private static final int NO_SHAKING = 0;
    private static final int SHAKE_TO_THE_LEFT = 1;
    private static final int SHAKE_TO_THE_RIGHT = 2;
    private static final int STOP_TIME = 1000;

    private float a_x,a_y,a_z,a_x1,a_y1,a_z1,da_x,da_y,da_z;
    private boolean onceFlag;
    private int checkFlag;
    private boolean sensorFlag;
    private CalcAcceleration calcAcceleration;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Intent sendIntent;
    public boolean activityFlag;
    private ServiceBroadcastReceiver serviceBroadcastReceiver;


    public IBinder onBind(Intent intent){
        return null;
    }

    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d(TAG,"onStartCommand");

        a_x = 0.0f;   a_y = 0.0f; a_z = 0.0f;
        onceFlag = true;
        checkFlag = NO_SHAKING;
        calcAcceleration = new CalcAcceleration();
        sendIntent = new Intent(TAG);
        sendIntent.putExtra("serviceFlag",true);

        activityFlag = true;
        serviceBroadcastReceiver = new ServiceBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MainActivity.TAG);
        registerReceiver(serviceBroadcastReceiver, intentFilter);

        //センサーマネージャの取得
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        //マネージャから加速度センサーを取得
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    public void onDestroy(){
        Log.d(TAG,"onDestroy");

        accelerometerSensor = null;
        sensorManager.unregisterListener(this);
        sensorManager = null;

        unregisterReceiver(serviceBroadcastReceiver);
    }


    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            a_x = event.values[0];
            a_y = event.values[1];
            a_z = event.values[2];
            if(onceFlag) {
                da_x = a_x;
                da_y = a_y;
                da_z = a_z;
                onceFlag = false;
            }else{
                da_x = a_x1 - a_x;
                da_y = a_y1 - a_y;
                da_z = a_z1 - a_z;
            }

            if(activityFlag) {
                sendIntent.putExtra("da_x", da_x);
                sendIntent.putExtra("da_y", da_y);
                sendIntent.putExtra("da_z", da_z);
                sendBroadcast(sendIntent);
            }

            checkFlag = calcAcceleration.CheckAcceleration(da_x,da_y,da_z);
            if(checkFlag != 0){
                DoAnything(checkFlag);
                onceFlag = true;
            }

            a_x1 = a_x;
            a_y1 = a_y;
            a_z1 = a_z;
        }
    }

    public void DoAnything(int Flag){
        sensorManager.unregisterListener(this);
        //Log.d(TAG, "Do Anything");
        if(Flag == SHAKE_TO_THE_LEFT){
            Toast.makeText(this,"端末が左に振られました！",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"端末が右に振られました！",Toast.LENGTH_SHORT).show();
        }
        Sleep(STOP_TIME);
        calcAcceleration.leftShakeFlag =false;
        calcAcceleration.rightShakeFlag = false;
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public synchronized void Sleep(long msec){
        try{
            wait(msec);
        }catch(InterruptedException e){
        }
    }

    public void onAccuracyChanged(Sensor sensor,int accuracy){
    }

}
