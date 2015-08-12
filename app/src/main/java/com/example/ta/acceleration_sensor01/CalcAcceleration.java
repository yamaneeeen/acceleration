/*
* クラスの内容：加速度センサーの値を計算する。
*               センサーの精度をよくするならここを変更する。
*/

package com.example.ta.acceleration_sensor01;

public class CalcAcceleration {

    /*定義宣言*/
    private static final String TAG = "CalcAcceleration";
    private static final int NO_SHAKING = 0;
    private static final int SHAKE_TO_THE_LEFT = 1;
    private static final int SHAKE_TO_THE_RIGHT = 2;
    private static final float p_threshold = 10.0f;
    private static final float n_threshold = -10.0f;

    /*変数宣言*/
    public boolean leftShakeFlag = false;
    public boolean rightShakeFlag = false;

    /*自作メソッド--------------------------------------------------------------------------------*/

    //serviceから加速度の変化量を受け取って、それを使ってどのような挙動をしたか判定する
    //精度をよくするにはここを変更
    public int CheckAcceleration(float da_x,float da_y,float da_z){
        if (da_x < n_threshold) {
            return SHAKE_TO_THE_LEFT;
        }else if (da_x > p_threshold) {
            return SHAKE_TO_THE_RIGHT;
        }
        return NO_SHAKING;
    }

    /*--------------------------------------------------------------------------------------------*/

}
