package com.project.rptang.android.utils;

import android.app.Application;
import android.util.Log;

/**
 * Android
 * com.project.rptang.android.utils
 * BaseApplication
 * <p>
 * Created by Stiven on 2017/3/9.
 * Copyright © 2017 ZYZS-TECH. All rights reserved.
 */

public class BaseApplication extends Application{

    private static BaseApplication instance;

    /**
     * 单例，返回一个实例
     * @return
     */
    public static BaseApplication getInstance() {
        if (instance == null) {
            Log.w("","[BaseApplication] instance is null.");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
