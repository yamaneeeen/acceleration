package com.example.ta.acceleration_sensor01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ta on 2015/08/11.
 */
public class ServiceBroadcastReceiver extends BroadcastReceiver {



    public void onReceive(Context context,Intent intent){
        if(intent.getBooleanExtra("activityFlag",false)) {
            boolean activityFlag = intent.getBooleanExtra("movingFlag", false);
            AccelerationService accelerationService = (AccelerationService) context;
            accelerationService.activityFlag = activityFlag;
        }
    }

}

