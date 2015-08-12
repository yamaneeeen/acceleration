/*
* アプリの動作：activityにおいて加速度センサーのスイッチを入れると、
*               serviceで加速度センサーの読み取りを開始する。
*               android端末を左右に振るとトーストでどちらに振ったか表示する。
*               activityを終了しても、スイッチがオンであればserviceは終了せず、
*               センサーの読み取りをし続ける。
*/

/*
* クラスの内容：serviceから加速度の値を受け取り、activity内に表示する。
*               加速度センサーのオンオフを切り替える
 */


package com.example.ta.acceleration_sensor01;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.text.NumberFormat;
import java.util.List;



public class MainActivity extends AppCompatActivity
        implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{

    /*定義宣言*/
    public static final String TAG = "MainActivity";

    /*変数宣言*/
    private boolean sensorFlag;
    private TextView textView1, textView2, textView3;
    private Button button1;
    private Switch switch1;
    private ActivityBroadcastReceiver activityBroadcastReceiver;
    private Intent sendIntent;
    private NumberFormat format;

    /*activityのライフサイクルにおける処理--------------------------------------------------------*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初期化
        SetView();
        sendIntent = new Intent(TAG);
        sendIntent.putExtra("activityFlag", true);
        sensorFlag = false;
        format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
    }

    protected void onResume(){
        super.onResume();

        //AccelerationServiceが起動中か確認
        CheckServiceMoving();

        //intentに起動中であるというフラグを追加してintentを投げる
        sendIntent.putExtra("movingFlag", true);
        sendBroadcast(sendIntent);

        //serviceから投げられるintentを受け取るようにしてレシーバーと登録する
        activityBroadcastReceiver = new ActivityBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AccelerationService.TAG);
        registerReceiver(activityBroadcastReceiver, intentFilter);
    }

    protected void onPause(){
        super.onPause();

        //intentにactivityが停止しているというフラグを追加してintentを投げる
        sendIntent.putExtra("movingFlag",false);
        sendBroadcast(sendIntent);
    }


    protected void onDestroy() {
        super.onDestroy();

        //レシーバーの登録解除
        unregisterReceiver(activityBroadcastReceiver);
    }

    /*--------------------------------------------------------------------------------------------*/


    /*ボタンやスイッチの処理----------------------------------------------------------------------*/
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button1:
                finish();
                break;
        }
    }

    //スイッチが切り替わった時に呼ばれるメソッド
    public void onCheckedChanged(CompoundButton compoundButton,boolean isChecked){
        //Log.d(TAG, "Button is " + isChecked);

        //スイッチがオンになったらserviceを起動、オフになったら停止
        Intent intent = new Intent(getApplication(),AccelerationService.class);
        if(isChecked == true) {
            startService(intent);
        }else{
            stopService(intent);
        }
    }

    /*--------------------------------------------------------------------------------------------*/

    /*activityのメソッド（何も変更なし）----------------------------------------------------------*/

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

    /*--------------------------------------------------------------------------------------------*/

    /*自作メソッド--------------------------------------------------------------------------------*/

    //viewのリンクとボタンのlistenerの設定
    private void SetView(){
        setContentView(R.layout.activity_main);
        textView1 = (TextView)findViewById(R.id.textView2);
        textView2 = (TextView)findViewById(R.id.textView3);
        textView3 = (TextView)findViewById(R.id.textView4);

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(this);

        switch1 = (Switch)findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);
    }

    //serviceで加速度センサーから受け取った値を表示する
    public void SetText(float da_x,float da_y,float da_z){
        textView1.setText("da(x)/dt = " + format.format(da_x));
        textView2.setText("da(y)/dt = "+format.format(da_y));
        textView3.setText("da(z)/dt = " + format.format(da_z));
    }

    //起動中のserviceを取得してlistに代入
    //そのlistの中にAccelerationServiceが入っているようだったら
    //スイッチをオン、そうでなければオフ
    private void CheckServiceMoving(){
        int i = 0;
        ActivityManager activityManager =
                (ActivityManager)getSystemService(ACTIVITY_SERVICE);

        //listに起動中のserviceを代入
        List<ActivityManager.RunningServiceInfo> list =
                activityManager.getRunningServices(Integer.MAX_VALUE);
        Log.d(TAG, AccelerationService.class.getName());

        //listのチェック
        while(i <= list.size()-1){
            Log.d(TAG,list.get(i).service.getClassName());

            //listの中のserviceの名前(String)がAccelerationServiceであるなら
            //serviceが起動中であるというフラグを立てる
            if(AccelerationService.class.getName().equalsIgnoreCase
                    (list.get(i).service.getClassName())) {
                Log.d(TAG, "Service  Is Moving");
                sensorFlag = true;
                break;
            }
            i++;
        }

        //もしフラグが立っているなら、スイッチをオンにする
        //立っていないならオフに
        switch1.setChecked(sensorFlag);
    }

    /*--------------------------------------------------------------------------------------------*/

}
