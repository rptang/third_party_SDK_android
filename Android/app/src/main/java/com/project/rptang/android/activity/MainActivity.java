package com.project.rptang.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.project.rptang.android.R;
import com.project.rptang.android.voip.VoipSDKHelper;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECVoIPCallManager;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private Button btn_call_john;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化操作
        initialization();
        initView();
        addEventListener();

    }

    private void initialization() {

        VoipSDKHelper.init(this, ECInitParams.LoginMode.FORCE_LOGIN);
    }

    private void initView() {
        btn_call_john = (Button) findViewById(R.id.btn_call_john);
    }

    private void addEventListener(){
        btn_call_john.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String mCurrentCallId = ECDevice.getECVoIPCallManager().makeCall(
//                        ECVoIPCallManager.CallType.VOICE, "18094286271");
//
//                Log.d(TAG, "onClick: "+mCurrentCallId);
                startActivity(new Intent(MainActivity.this,VoIPCallActivity.class));
            }
        });
    }
}
