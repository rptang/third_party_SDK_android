package com.project.rptang.android.activity;

import android.app.Activity;
import android.os.Bundle;

import com.project.rptang.android.R;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
