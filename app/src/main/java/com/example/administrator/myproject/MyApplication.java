package com.example.administrator.myproject;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import org.xutils.x;

/**
 * Created by Administrator on 2017/8/14.
 */

public class MyApplication extends Application {

    public static Context mContext;
    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        initScreenSize();
    }


    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }
}
