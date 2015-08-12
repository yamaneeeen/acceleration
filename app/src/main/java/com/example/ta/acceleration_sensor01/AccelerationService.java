/*
* クラスの内容：このserviceにおいて加速度センサーを登録する。
*               これにより、activityが停止している場合でも加速度センサーから値を取得できる。
*               また、受け取った値をintentに追加してMainActivityに投げる。
*/

/*
* 改良すべき点：activityとserviceとの通信をintentによって行っているが、
*               bindService()によってできるかもしれないので確認する
*/


package com.example.ta.acceleration_sensor01;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class AccelerationService extends Service implements SensorEventListener {

    /*定義宣言*/
    public static final String TAG = "AccelerationService";
    private static final int NO_SHAKING = 0;
    private static final int SHAKE_TO_THE_LEFT = 1;
    private static final int SHAKE_TO_THE_RIGHT = 2;
    private static final int STOP_TIME = 1000;

    /*変数宣言*/
    private float a_x,a_y,a_z,a_x1,a_y1,a_z1,da_x,da_y,da_z;
    private boolean onceFlag;
    private int checkFlag;
    private CalcAcceleration calcAcceleration;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Intent sendIntent;
    public boolean activityFlag;
    private ServiceBroadcastReceiver serviceBroadcastReceiver;

    /*serviceのライフサイクルの処理---------------------------------------------------------------*/

    //今回は使ってない
    public IBinder onBind(Intent intent){
        return null;
    }

    public void onCreate(){
        super.onCreate();
        //Log.d(TAG, "onCreate");
    }

    public int onStartCommand(Intent intent,int flags,int startId){
        //Log.d(TAG,"onStartCommand");

        //初期化
        a_x = 0.0f;   a_y = 0.0f; a_z = 0.0f;
        onceFlag = true;
        checkFlag = NO_SHAKING;
        calcAcceleration = new CalcAcceleration();
        sendIntent = new Intent(TAG);
        sendIntent.putExtra("serviceFlag",true);

        //activityから投げられるintentを受け取るレシーバーの設定と登録
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

        //serviceが強制終了したときの処理の仕方
        //その場合serviceを再起動させる
        return START_STICKY;
    }

    public void onDestroy(){
        Log.d(TAG,"onDestroy");

        //加速度センサーの登録解除
        accelerometerSensor = null;
        sensorManager.unregisterListener(this);
        sensorManager = null;

        //レシーバーの解除
        unregisterReceiver(serviceBroadcastReceiver);
    }

    /*--------------------------------------------------------------------------------------------*/

    /*センサーの処理------------------------------------------------------------------------------*/

    //センサーの値が変わった時の処理
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            a_x = event.values[0];
            a_y = event.values[1];
            a_z = event.values[2];

            //センサーの起動時は加速度の変化量＝加速度と置く
            //それ以外のときは変化量は前回の値との差分をとる
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

            //もしactivityが起動しているなら加速度の変化量をintentに追加してMainActivityに投げる
            if(activityFlag) {
                sendIntent.putExtra("da_x", da_x);
                sendIntent.putExtra("da_y", da_y);
                sendIntent.putExtra("da_z", da_z);
                sendBroadcast(sendIntent);
            }

            //加速度の変化量の値から端末を振ったかどうか判定して処理を行う
            checkFlag = calcAcceleration.CheckAcceleration(da_x,da_y,da_z);
            if(checkFlag != 0){
                DoAnything(checkFlag);
                onceFlag = true;
            }

            //前回の値に今回の値を代入する
            a_x1 = a_x;
            a_y1 = a_y;
            a_z1 = a_z;
        }
    }

    //センサーの精度が変わった時の処理(未使用)
    public void onAccuracyChanged(Sensor sensor,int accuracy){
    }

    /*--------------------------------------------------------------------------------------------*/

    /*自作のメソッド------------------------------------------------------------------------------*/

    //端末が降られたときの処理
    private void DoAnything(int Flag){
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

    //threadをmsec秒停止させる処理
    private synchronized void Sleep(long msec){
        try{
            wait(msec);
        }catch(InterruptedException e){
        }
    }

    /*--------------------------------------------------------------------------------------------*/

}
