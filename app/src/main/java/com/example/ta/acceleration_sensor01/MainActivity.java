package com.example.ta.acceleration_sensor01;

import android.app.ActivityManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{


    private TextView textView1, textView2, textView3,textView4,textView5;
    private Button button1;
    private Switch switch1;
    private float a_x,a_y,a_z,a_x1,a_y1,a_z1,da_x,da_y,da_z;
    private CalcAcceleration calcAcceleration;

    private boolean sensorFlag;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetView();

        a_x = 0.0f;   a_y = 0.0f; a_z = 0.0f;
        sensorFlag = false;
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button1:
                finish();
                break;
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton,boolean isChecked){
        Log.d(TAG, "Button is " + isChecked);
        Intent intent = new Intent(getApplication(),AccelerationService.class);
        if(isChecked == true) {
            startService(intent);
        }else{
            stopService(intent);
        }
    }


    protected void onResume(){
        super.onResume();
        int i = 0;
        ActivityManager activityManager =(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(Integer.MAX_VALUE);
        Log.d(TAG,AccelerationService.class.getName());
        while(i <= list.size()-1){
            Log.d(TAG,list.get(i).service.getClassName());
            if(AccelerationService.class.getName().equalsIgnoreCase(list.get(i).toString())) {
                Log.d(TAG, "Service  Is Moving");
                sensorFlag = true;
                break;
            }
            i++;
        }
        switch1.setChecked(sensorFlag);
    }

    protected void onPause(){
        super.onPause();
    }


    protected void onDestroy() {
        super.onDestroy();

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

    public void SetView(){
        setContentView(R.layout.activity_main);
        textView1 = (TextView)findViewById(R.id.textView2);
        textView2 = (TextView)findViewById(R.id.textView3);
        textView3 = (TextView)findViewById(R.id.textView4);

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(this);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);
    }
}
