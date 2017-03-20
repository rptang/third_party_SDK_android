package com.project.rptang.android.activity;

import android.app.Activity;
import android.os.Bundle;

import com.project.rptang.android.R;
import com.zhy.autolayout.AutoLayoutActivity;

public class BaseActivity extends AutoLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
