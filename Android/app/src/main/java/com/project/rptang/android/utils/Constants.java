package com.project.rptang.android.utils;

import android.text.TextUtils;

/**
 * Android
 * com.project.rptang.android.utils
 * Constants
 * <p>
 * Created by Stiven on 2017/3/9.
 * Copyright © 2017 ZYZS-TECH. All rights reserved.
 */

public class Constants {

    public static String USERNAME = "";

    public static String getLastwords(String srcText, String p) {
        try {
            String[] array = TextUtils.split(srcText, p);
            int index = (array.length - 1 < 0) ? 0 : array.length - 1;
            return array[index];
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
