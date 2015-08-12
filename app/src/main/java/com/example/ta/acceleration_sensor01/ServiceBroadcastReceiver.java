/*
* クラスの内容：activityから投げられるintentを受け取るserviceにおけるBroadcastReceiver
*               そのintentを受け取った場合、activityが起動しているというフラグを立てる
*/

package com.example.ta.acceleration_sensor01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceBroadcastReceiver extends BroadcastReceiver {

    /*レシーバーの処理----------------------------------------------------------------------------*/

    public void onReceive(Context context,Intent intent){
        if(intent.getBooleanExtra("activityFlag",false)) {

            //activityからのintentを受け取った時の処理
            boolean activityFlag = intent.getBooleanExtra("movingFlag", false);
            AccelerationService accelerationService = (AccelerationService) context;
            accelerationService.activityFlag = activityFlag;
        }
    }

    /*--------------------------------------------------------------------------------------------*/

}

