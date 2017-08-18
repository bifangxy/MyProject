package com.example.administrator.myproject;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/8/14.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
