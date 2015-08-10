package com.example.ta.acceleration_sensor01;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;

/**
 * Created by yamada on 2015/08/07.
 */
public class AccelerationService extends Service implements SensorEventListener {

    private static final String TAG = "AccelerationService";

    private float a_x,a_y,a_z,a_x1,a_y1,a_z1,da_x,da_y,da_z;
    private boolean onceFlag;
    private boolean checkFlag;
    private boolean sensorFlag;
    private CalcAcceleration calcAcceleration;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;


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
        checkFlag = false;
        calcAcceleration = new CalcAcceleration();

        //センサーマネージャの取得
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        //マネージャから加速度センサーを取得
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    public void onDestroy(){
        Log.d(TAG,"onDestroy");

        accelerometerSensor = null;
        sensorManager.unregisterListener(this);
        sensorManager = null;
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

            checkFlag = calcAcceleration.CheckAcceleration(da_x,da_y,da_z);
            a_x1 = a_x;
            a_y1 = a_y;
            a_z1 = a_z;
        }
        if(checkFlag){

            sensorManager.unregisterListener(this);
            Log.d(TAG, "Stop The sensor");
            sleep(2000);
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "Restart The sensor");
        }
    }

    public synchronized void sleep(long msec){
        try{
            wait(msec);
        }catch(InterruptedException e){
        }
    }

    public void onAccuracyChanged(Sensor sensor,int accuracy){
    }

}
