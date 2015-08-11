package com.example.ta.acceleration_sensor01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ta on 2015/08/11.
 */
public class ActivityBroadcastReceiver extends BroadcastReceiver{

    float da_x,da_y,da_z;

    public void onReceive(Context context,Intent intent){
        if(intent.getBooleanExtra("serviceFlag",false)) {
            da_x = intent.getFloatExtra("da_x", 0.0f);
            da_y = intent.getFloatExtra("da_y", 0.0f);
            da_z = intent.getFloatExtra("da_z", 0.0f);

            MainActivity mainActivity = (MainActivity) context;
            mainActivity.SetText(da_x, da_y, da_z);
        }
    }

}
