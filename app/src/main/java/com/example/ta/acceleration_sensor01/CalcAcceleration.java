package com.example.ta.acceleration_sensor01;

import android.os.Debug;
import android.util.Log;

/**
 * Created by ta on 2015/08/05.
 */
public class CalcAcceleration {

    private static final String TAG = "CalcAcceleration";
    private static final int NO_SHAKING = 0;
    private static final int SHAKE_TO_THE_LEFT = 1;
    private static final int SHAKE_TO_THE_RIGHT = 2;
    private static final float p_threshold = 10.0f;
    private static final float n_threshold = -10.0f;

    public boolean leftShakeFlag = false;
    public boolean rightShakeFlag = false;


    public int CheckAcceleration(float da_x,float da_y,float da_z){
        /*
        if (da_x < n_threshold) {
            if(rightShakeFlag){
                rightShakeFlag = false;
                //Log.d(TAG, "Shake To The Right:" + da_x);
                return SHAKE_TO_THE_RIGHT;
            }else {
                leftShakeFlag = true;
            }
        }else if (da_x > p_threshold) {
            if(leftShakeFlag){
                leftShakeFlag = false;
                //Log.d(TAG, "Shake To The Left:" + da_x);
                return SHAKE_TO_THE_LEFT;
            }else{
                rightShakeFlag = true;
            }
        }
        return NO_SHAKING;
        */
        if (da_x < n_threshold) {
            return SHAKE_TO_THE_LEFT;
        }else if (da_x > p_threshold) {
            return SHAKE_TO_THE_RIGHT;
        }
        return NO_SHAKING;
    }

}
