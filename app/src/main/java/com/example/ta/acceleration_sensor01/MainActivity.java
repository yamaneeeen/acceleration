package com.example.ta.acceleration_sensor01;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView textView1, textView2, textView3,textView4,textView5;
    private Button button1;

    private float a_x,a_y,a_z,a_x1,a_y1,a_z1,da_x,da_y,da_z;
    private boolean onceFlag;
    private boolean checkFlag;

    private CalcAcceleration calcAcceleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView)findViewById(R.id.textView2);
        textView2 = (TextView)findViewById(R.id.textView3);
        textView3 = (TextView)findViewById(R.id.textView4);
        textView4 = (TextView)findViewById(R.id.textView5);
        textView5 = (TextView)findViewById(R.id.textView6);
        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(this);

        a_x = 0.0f;   a_y = 0.0f; a_z = 0.0f;
        onceFlag = true;
        checkFlag = false;
        calcAcceleration = new CalcAcceleration();

        //センサーマネージャの取得
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);

        //マネージャから加速度センサーを取得
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button1:
                finish();
                break;
        }
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

            textView1.setText("X = " + a_x );
            textView2.setText("Y = " + a_y );
            textView3.setText("Z = " + a_z );
            textView4.setText("dx = " + da_x );
            textView5.setText("dy = " + da_y );
            checkFlag = calcAcceleration.CheckAcceleration(da_x,da_y,da_z);
            a_x1 = a_x;
            a_y1 = a_y;
            a_z1 = a_z;
        }
        if(checkFlag){

            //いえええい！！

            //ここからプログラミング
            //振ったと判定されたとき何秒か止める
            //その間に何か処理を入れて動作確認

        }
    }

    public void onAccuracyChanged(Sensor sensor,int accuracy){
    }

    protected void onResume(){
        super.onResume();

        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this);
    }


    protected void onDestroy() {
        super.onDestroy();
        accelerometerSensor = null;
        sensorManager.unregisterListener(this);
        sensorManager = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
