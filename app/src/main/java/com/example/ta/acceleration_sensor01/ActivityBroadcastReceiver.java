/*
* クラスの内容：serviceから投げられるintentを受け取るactivityにおけるBroadcastReceiver
*               そのintentを受け取った時に,加速度の変化量をactivityに表示する
*/

package com.example.ta.acceleration_sensor01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActivityBroadcastReceiver extends BroadcastReceiver{

    /*変数宣言*/
    private float da_x,da_y,da_z;

    /*レシーバーの処理----------------------------------------------------------------------------*/

    public void onReceive(Context context,Intent intent){
        if(intent.getBooleanExtra("serviceFlag",false)) {
            
            //serviceからのintentを受け取った時に行う処理
            da_x = intent.getFloatExtra("da_x", 0.0f);
            da_y = intent.getFloatExtra("da_y", 0.0f);
            da_z = intent.getFloatExtra("da_z", 0.0f);

            MainActivity mainActivity = (MainActivity) context;
            mainActivity.SetText(da_x, da_y, da_z);
        }
    }

    /*--------------------------------------------------------------------------------------------*/

}
