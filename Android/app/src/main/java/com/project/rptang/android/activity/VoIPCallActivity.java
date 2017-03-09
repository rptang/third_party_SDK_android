package com.project.rptang.android.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.rptang.android.R;
import com.project.rptang.android.utils.ECNotificationManager;
import com.project.rptang.android.voip.VoIPCallHelper;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.VideoRatio;


public class VoIPCallActivity extends Activity implements View.OnClickListener, VoIPCallHelper.OnCallEventNotifyListener {

    //呼叫/接听对方头像
    private ImageView iv_call_profile;
    //呼叫状态：正在呼叫联络台，联络台正向您发起呼叫，正在通话中
    private TextView tv_call_status;
    //通话时长
    private TextView tv_holding_time;
    //关闭麦克风按钮
    private ImageView iv_close_microphone;
    //开启免提按钮
    private ImageView iv_open_speakerphone;
    //不接电话按钮
    private ImageView iv_not_answer_call;
    //接电话按钮
    private ImageView iv_answer_call;
    //停止通话/停止呼叫按钮
    private ImageView iv_stop_call;

    //通话时长整体布局，某些时候隐藏
    private LinearLayout ll_call_holding_time;
    //接听方式整体布局，某些时候隐藏
    private LinearLayout ll_answer_method;

    /**昵称*/
    public static final String EXTRA_CALL_NAME = "com.project.rptang.android.VoIP_CALL_NAME";
    /**通话号码*/
    public static final String EXTRA_CALL_NUMBER = "com.project.rptang.android.VoIP_CALL_NUMBER";
    /**呼入方或者呼出方*/
    public static final String EXTRA_OUTGOING_CALL = "com.project.rptang.android.VoIP_OUTGOING_CALL";
    /**VoIP呼叫*/
    public static final String ACTION_VOICE_CALL = "com.project.rptang.android.intent.ACTION_VOICE_CALL";
    boolean isConnect = false;
    /**通话昵称*/
    protected String mCallName;
    /**通话号码*/
    protected String mCallNumber;
    protected String mPhoneNumber;
    /**是否来电*/
    protected boolean mIncomingCall = false;
    /**呼叫唯一标识号*/
    protected String mCallId;
    /**VoIP呼叫类型（音视频）*/
    protected ECVoIPCallManager.CallType mCallType;
    /**透传号码参数*/
    private static final String KEY_TEL = "tel";
    /**透传名称参数*/
    private static final String KEY_NAME = "nickname";

    /**屏幕资源*/
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    private KeyguardManager mKeyguardManager = null;
    private PowerManager.WakeLock mWakeLock;

    private Intent mIntent;

    public  static  VoIPCallActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vo_ipcall);

        instance = this;
        initView();
        addEventListener();

        initCall();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //回掉通话状态接口
        VoIPCallHelper.setOnCallEventNotifyListener(this);
        ECNotificationManager.cancelCCPNotification(ECNotificationManager.CCP_NOTIFICATOIN_ID_CALLING);
    }

    private void initView() {

        iv_call_profile = (ImageView) findViewById(R.id.iv_call_profile);
        tv_call_status = (TextView) findViewById(R.id.tv_call_status);
        tv_holding_time = (TextView) findViewById(R.id.tv_holding_time);
        iv_close_microphone = (ImageView) findViewById(R.id.iv_close_microphone);
        iv_open_speakerphone = (ImageView) findViewById(R.id.iv_open_speakerphone);
        iv_not_answer_call = (ImageView) findViewById(R.id.iv_not_answer_call);
        iv_stop_call = (ImageView) findViewById(R.id.iv_stop_call);
        iv_answer_call = (ImageView) findViewById(R.id.iv_answer_call);

        ll_call_holding_time = (LinearLayout) findViewById(R.id.ll_call_holding_time);
        ll_answer_method = (LinearLayout) findViewById(R.id.ll_answer_method);
    }

    private void initCall(){

    }

    private void addEventListener() {

        iv_close_microphone.setOnClickListener(this);
        iv_open_speakerphone.setOnClickListener(this);
        iv_not_answer_call.setOnClickListener(this);
        iv_answer_call.setOnClickListener(this);
        iv_stop_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_microphone:
                break;
            case R.id.iv_open_speakerphone:
                break;
            case R.id.iv_not_answer_call:
                break;
            case R.id.iv_answer_call:
                break;
            case R.id.iv_stop_call:
                finish();
                break;
        }
    }

    @Override
    public void onCallProceeding(String callId) {

    }

    @Override
    public void onMakeCallback(ECError arg0, String arg1, String arg2) {

    }

    @Override
    public void onCallAlerting(String callId) {

    }

    @Override
    public void onCallAnswered(String callId) {

    }

    @Override
    public void onMakeCallFailed(String callId, int reason) {

    }

    @Override
    public void onCallReleased(String callId) {

    }

    @Override
    public void onVideoRatioChanged(VideoRatio videoRatio) {

    }
}
