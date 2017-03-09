package com.project.rptang.android.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import com.project.rptang.android.utils.Constants;
import com.project.rptang.android.utils.ECNotificationManager;
import com.project.rptang.android.utils.LogUtil;
import com.project.rptang.android.voip.VoIPCallHelper;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;

public class ECVoIPBaseActivity extends BaseActivity implements VoIPCallHelper.OnCallEventNotifyListener {

    private static final String TAG = "ECVoIPBaseActivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(init()) {
            return ;
        }

        if(mCallType == null) {
            mCallType = ECVoIPCallManager.CallType.VOICE;
        }

        initProwerManager();
    }

    private boolean init() {
        if(getIntent()==null){
            return true;
        }
        mIntent=getIntent();
        mIncomingCall = !(getIntent().getBooleanExtra(EXTRA_OUTGOING_CALL, false));
        mCallType = (ECVoIPCallManager.CallType) getIntent().getSerializableExtra(ECDevice.CALLTYPE);

        if(mIncomingCall) {
            // 透传信息
            String[] infos = getIntent().getExtras().getStringArray(ECDevice.REMOTE);
            if (infos != null && infos.length > 0) {
                for (String str : infos) {
                    if (str.startsWith(KEY_TEL)) {
                        mPhoneNumber = Constants.getLastwords(str, "=");
                    } else if (str.startsWith(KEY_NAME)) {
                        mCallName = Constants.getLastwords(str, "=");
                    }
                }
            }
        }

//        if(!VoIPCallHelper.mHandlerVideoCall && mCallType == ECVoIPCallManager.CallType.VIDEO) {
//            VoIPCallHelper.mHandlerVideoCall = true;
//            Intent mVideoIntent = new Intent(this , VideoActivity.class);
//            mVideoIntent.putExtras(getIntent().getExtras());
//            mVideoIntent.putExtra(VoIPCallActivity.EXTRA_OUTGOING_CALL , false);
//            startActivity(mVideoIntent);
//            super.finish();
//            return true;
//        }
        return false;

    }

    /**
     * 收到的VoIP通话事件通知是否与当前通话界面相符
     * @return 是否正在进行的VoIP通话
     */
    protected boolean isEqualsCall(String callId) {
        return (!TextUtils.isEmpty(callId) && callId.equals(mCallId));
    }

    /**
     * 是否需要做界面更新
     * @param callId
     * @return
     */
    protected boolean needNotify(String callId) {
        return !(isFinishing() || !isEqualsCall(callId));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        enterIncallMode();
        VoIPCallHelper.setOnCallEventNotifyListener(this);
        ECNotificationManager.cancelCCPNotification(ECNotificationManager.CCP_NOTIFICATOIN_ID_CALLING);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseWakeLock();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(VoIPCallHelper.isHoldingCall()) {
            ECNotificationManager.showCallingNotification(mCallType);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onViewAccept(ECCallControlUILayout controlPanelView, ImageButton view) {
//        if(controlPanelView != null) {///
//            controlPanelView.setControlEnable(false);
//        }
//        VoIPCallHelper.acceptCall(mCallId);
//        mCallControlUIView.setCallDirect(ECCallControlUILayout.CallLayout.INCALL);
//        mCallHeaderView.setCallTextMsg(R.string.ec_voip_calling_accepting);
//
//    }
//
//    @Override
//    public void onViewRelease(ECCallControlUILayout controlPanelView, ImageButton view) {
//        if(controlPanelView != null) {
//            controlPanelView.setControlEnable(false);
//        }
//        VoIPCallHelper.releaseCall(mCallId);
//    }
//
//
//    @Override
//    public void onViewReject(ECCallControlUILayout controlPanelView, ImageButton view) {
//        if(controlPanelView != null) {
//            controlPanelView.setControlEnable(false);
//        }
//        VoIPCallHelper.rejectCall(mCallId);
//    }

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
    public void onCallReleasedUP(String callId) {

    }

//    @Override
//    public void onVideoRatioChanged(VideoRatio videoRatio) {
//
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        LogUtil.error("tag""setintent");
        ECHandlerHelper.removeCallbacksRunnOnUI(OnCallFinish);
        setIntent(intent);
//        setIntent(sIntent);
        if(init()) {
            return ;
        }

        if(mCallType == null) {
            mCallType = ECVoIPCallManager.CallType.VOICE;
        }
    }

    @Override
    public void finish() {
        ECHandlerHelper.postDelayedRunnOnUI(OnCallFinish, 3000);
    }
    public void hfFinish() {
        ECHandlerHelper.postDelayedRunnOnUI(OnCallFinish, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do nothing.
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 延时关闭界面
     */
    final Runnable OnCallFinish = new Runnable() {
        public void run() {
            ECVoIPBaseActivity.super.finish();
        }
    };

//    @Override
//    public void sendDTMF(char c) {
//
//        VoipSDKHelper.getVoIPCallManager().sendDTMF(mCallId, c);
//    }


    /**
     * 初始化资源
     */
    protected void initProwerManager() {
        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP , "CALL_ACTIVITY#" + super.getClass().getName());
        mKeyguardManager = ((KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE));
    }

    /**
     * 释放资源
     */
    protected void releaseWakeLock() {
        try {
            if (this.mWakeLock.isHeld()) {
                if (this.mKeyguardLock != null) {
//                    this.mKeyguardLock.reenableKeyguard();
                    this.mKeyguardLock = null;
                }
                this.mWakeLock.release();
            }
            return;
        } catch (Exception e) {
            LogUtil.error(TAG, e.toString());
        }
    }
}
