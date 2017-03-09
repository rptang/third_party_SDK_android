package com.project.rptang.android.utils;

import android.util.Log;

/**
 * Created by qianhu on 1/19/2016.
 */
public class LogUtil {
    public static void error(String tag, String message, Throwable ex) {
        Log.e(tag, message, ex);
    }
    public static void error(String tag, String message) {
        Log.e(tag, message);
    }
    public static void d(String tag, String message) {
        Log.d(tag, message);
    }
    public static void warning(String tag, String message){
        Log.w(tag,message);
    }
    public static void e(String tag, String message) {
        Log.e(tag, message);
    }
}
