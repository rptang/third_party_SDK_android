package com.project.rptang.android.voip;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.project.rptang.android.activity.VoIPCallActivity;
import com.project.rptang.android.utils.BaseApplication;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.VoipMediaChangedInfo;

/**
 * Android
 * com.project.rptang.android.voip
 * VoipSDKHelper
 * <p>
 * Created by Stiven on 2017/3/9.
 * Copyright © 2017 ZYZS-TECH. All rights reserved.
 */

public class VoipSDKHelper implements ECDevice.InitListener, ECDevice.OnECDeviceConnectListener, ECDevice.OnLogoutListener {

    private static final String TAG = "VoipSDKHelper";

    private static VoipSDKHelper sInstance;
    //
    private boolean mKickOff = false;
    //
    private ECInitParams.LoginMode mMode = ECInitParams.LoginMode.FORCE_LOGIN;
    private Context mContext;
    //
    private ECDevice.ECConnectState mConnect = ECDevice.ECConnectState.CONNECT_FAILED;

    public static SoftUpdate mSoftUpdate;

    public static VoipSDKHelper getInstance() {
        if (sInstance == null) {
            sInstance = new VoipSDKHelper();
        }
        return sInstance;
    }

    public static void init(Context ctx) {
        init(ctx, ECInitParams.LoginMode.AUTO);
    }

    public static void init(Context ctx, ECInitParams.LoginMode mode) {

//        getInstance().mKickOff = false;
//        getInstance().mMode = mode;
        getInstance().mContext = ctx;

        ctx = BaseApplication.getInstance().getApplicationContext();
        //判断SDK是否已经初始化
        if (!ECDevice.isInitialized()) {
           /*  initial: ECSDK 初始化接口
            * 参数：
            *     inContext - Android应用上下文对象
            *
            * 说明：示例在应用程序创建时初始化 SDK引用的是Application的上下文，
            *       开发者可根据开发需要调整。
            */

//            getInstance().mConnect = ECDevice.ECConnectState.CONNECTING;

            ECDevice.initial(ctx, getInstance());
        }
// 已经初始化成功，后续开发业务代码。
        Log.i(TAG, "初始化SDK及登陆代码完成");
    }

    @Override
    public void onInitialized() {

        // SDK已经初始化成功
        Log.i("", "初始化SDK成功");
        //设置登录参数，可分为自定义方式和通讯账号验密方式，详情点此查看>>
        ECInitParams params = ECInitParams.createParams();
        params.setUserid("18298003954");
        params.setAppKey("8a216da85aac13ff015ab0b4aae60172");
        params.setToken("722050055efb492dfdc004aa2026d3ce");
        //设置登陆验证模式：自定义登录方式
        params.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        //LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO。使用方式详见注意事项）
        params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);

        //设置通知回调监听包含登录状态监听，接收消息监听，呼叫事件回调监听和
        ECDevice.setOnDeviceConnectListener(this);

        // 语音通话状态监听，使用语音通话功能的开发者需要设置。
        ECVoIPCallManager callInterface = ECDevice.getECVoIPCallManager();
        if (callInterface != null) {
            callInterface.setOnVoIPCallListener(new ECVoIPCallManager.OnVoIPListener() {
                @Override
                public void onVideoRatioChanged(VideoRatio videoRatio) {
                }

                @Override
                public void onSwitchCallMediaTypeRequest(String s, ECVoIPCallManager.CallType callType) {
                }

                @Override
                public void onSwitchCallMediaTypeResponse(String s, ECVoIPCallManager.CallType callType) {
                }

                @Override
                public void onDtmfReceived(String s, char c) {
                }

                @Override
                public void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
                    // 处理呼叫事件回调
                    if (voipCall == null) {
                        Log.e("SDKCoreHelper", "handle call event error , voipCall null");
                        return;
                    }
                    // 根据不同的事件通知类型来处理不同的业务
                    ECVoIPCallManager.ECCallState callState = voipCall.callState;
                    switch (callState) {
                        case ECCALL_PROCEEDING:
                            Log.i("", "正在连接服务器处理呼叫请求，callid：" + voipCall.callId);
                            break;
                        case ECCALL_ALERTING:
                            Log.i("", "呼叫到达对方，正在振铃，callid：" + voipCall.callId);
                            break;
                        case ECCALL_ANSWERED:
                            Log.i("", "对方接听本次呼叫,callid：" + voipCall.callId);
                            break;
                        case ECCALL_FAILED:
                            // 本次呼叫失败，根据失败原因进行业务处理或跳转
                            Log.i("", "called:" + voipCall.callId + ",reason:" + voipCall.reason);
                            break;
                        case ECCALL_RELEASED:
                            // 通话释放[完成一次呼叫]
                            break;
                        default:
                            Log.e("SDKCoreHelper", "handle call event error , callState " + callState);
                            break;
                    }
                }

                @Override
                public void onMediaDestinationChanged(VoipMediaChangedInfo voipMediaChangedInfo) {

                }
            });
        }

        //设置接收来电事件通知Intent等，详情点此查看>>

        // 接收来电时，需要设置接收来电事件通知Intent。用于SDK回调对应的activity。
        // 呼入activity在sdk初始化的回 调onInitialized中设置。
        // 呼入界面activity、开发者需创建或修改VoIPCallActivity类,可参考demo中的
        // VoIPCallActivity.java(demo中的目录：/app/src/main/java/com/yuntongxun/ecdemo/ui/voip)
        Intent intent = new Intent(getInstance().mContext, VoIPCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getInstance().mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);

        //验证参数是否正确，登陆SDK，详情点此查看>>
        if (params.validate()) {
            // 登录函数
            ECDevice.login(params);
        }

    }

    @Override
    public void onError(Exception e) {

        //在初始化错误的方法中打印错误原因
        Log.i("", "初始化SDK失败" + e.getMessage());
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect(ECError ecError) {

    }

    @Override
    public void onConnectState(ECDevice.ECConnectState ecConnectState, ECError ecError) {

        if (ecConnectState == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (ecError.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                Log.i("", "==帐号异地登陆");
            } else {
                Log.i("", "==其他登录失败,错误码：" + ecError.errorCode);
            }
            return;
        } else if (ecConnectState == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            Log.i("", "==登陆成功");
        }

    }

    @Override
    public void onLogout() {

    }

    /**
     * 判断服务是否自动重启
     *
     * @return 是否自动重启
     */
    public static boolean isUIShowing() {
        return ECDevice.isInitialized();
    }

    /**
     * VoIP呼叫接口
     * @return
     */
    public static ECVoIPCallManager getVoIPCallManager() {
        return ECDevice.getECVoIPCallManager();
    }

    public static ECVoIPSetupManager getVoIPSetManager() {
        return ECDevice.getECVoIPSetupManager();
    }

    public static class SoftUpdate {
        public String version;
        public String desc;
        public boolean force;

        public SoftUpdate(String version, String desc, boolean force) {
            this.version = version;
            this.force = force;
            this.desc = desc;
        }
    }

    public static void setSoftUpdate(String version, String desc, boolean mode) {
        mSoftUpdate = new SoftUpdate(version, desc, mode);
    }
}
