package com.example.ta.acceleration_sensor01;

import android.os.Debug;
import android.util.Log;

/**
 * Created by ta on 2015/08/05.
 */
public class CalcAcceleration {

    private static final String TAG = "CalcAcceleration";

    boolean CheckAcceleration(float da_x,float da_y,float da_z){
        float dF = (float)Math.sqrt((da_x * da_x) + (da_y * da_y) + (da_z * da_z));
        if(dF > 5.0f) {
            if (da_x < -5.0f) {
                Log.d(TAG, "Shake To The LEFT:" + da_x);
                return true;
            }
            if (da_x > 5.0f) {
                Log.d(TAG, "Shake To The RIGHT:" + da_x);
                return true;
            }
        }
        return false;
    }

}
