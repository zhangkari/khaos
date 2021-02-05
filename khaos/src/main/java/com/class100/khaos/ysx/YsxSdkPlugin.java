package com.class100.khaos.ysx;

import android.app.Activity;

import com.chinamobile.ysx.YSXError;
import com.chinamobile.ysx.YSXInstantMeetingOptions;
import com.chinamobile.ysx.YSXJoinMeetingOptions;
import com.chinamobile.ysx.YSXJoinMeetingParams;
import com.chinamobile.ysx.YSXMeetingService;
import com.chinamobile.ysx.YSXMeetingSettingsHelper;
import com.chinamobile.ysx.YSXMessageListener;
import com.chinamobile.ysx.YSXSdk;
import com.chinamobile.ysx.YSXSdkAuthenticationListener;
import com.chinamobile.ysx.YSXSdkInitializeListener;
import com.chinamobile.ysx.YSXStartMeetingOptions;
import com.chinamobile.ysx.YSXStartMeetingParams4NormalUser;
import com.chinamobile.ysx.auther.LoginResult;
import com.chinamobile.ysx.auther.YSXLoginResultListener;
import com.chinamobile.ysx.bean.Result;
import com.chinamobile.ysx.responselistener.ResponseListenerCommon;
import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtLog;
import com.class100.atropos.generic.AtTexts;
import com.class100.hades.http.HaApiCallback;
import com.class100.hades.http.HaApiResponse;
import com.class100.hades.http.HaHttpClient;
import com.class100.khaos.KhAbsSdk;
import com.class100.khaos.KhCreateScheduledConfig;
import com.class100.khaos.KhCreateScheduledListener;
import com.class100.khaos.KhJoinMeetingConfig;
import com.class100.khaos.KhReplyInviteConfig;
import com.class100.khaos.KhReplyInviteListener;
import com.class100.khaos.KhReplyInviteResult;
import com.class100.khaos.KhSendInviteConfig;
import com.class100.khaos.KhSendInviteListener;
import com.class100.khaos.KhSendInviteResult;
import com.class100.khaos.KhStartMeetingConfig;
import com.class100.khaos.ysx.internal.request.ReqKhSdkToken;
import com.class100.khaos.ysx.internal.response.RespKhSdkToken;

import java.util.List;

public class YsxSdkPlugin extends KhAbsSdk {
    private static final String TAG = "YsxSdkPlugin";

    // TODO Initialize int libAtNative
    private static final String APP_KEY = "FOwvZJf5DjpizygZahOH9hgyciQmgOsXR5eC";
    private static final String APP_SECRET = "IQGrn2cvKiEdfPd44lOAof0fVUovoIZW0FMr";
    private static final String APP_TOKEN = "HAHWVjyP9VVokyDATOvFBSX06iPGnHtL9It+OpTCDwNDvB8pNULFDR5zH0OA+EV8K5K7bH/8tiShhb07sDiXKTFn1gVrcKm9ckgc1FbHBkwYOpHrNWss/wAf0u5YoUib2zPjG6i0VvwSbQazmoZ5WCbc88Lt9ynKHRDCZaGjNetBJgJn5WnliT17KYxgRYGsFqbh90GDL7o0mY8ipVHLug==";

    private SdkAuthListener authListener;

    @Override
    public void load() {
        YSXSdk sdk = YSXSdk.getInstance();
        if (sdk.isInitialized()) {
            AtLog.d(TAG, "load", "sdk is initialized");
            loginSdk();
            return;
        }

        sdk.initSDK(env._app, env._app, APP_KEY, APP_SECRET, true, new YSXSdkInitializeListener() {
            @Override
            public void onYSXSdkInitializeResult(int errorCode, int internalErrorCode) {
                AtLog.d(TAG, "Init ysx sdk result", "errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);
                if (errorCode != YSXError.SUCCESS) {
                    AtLog.d(TAG, "Init ysxSDK. Error", "");
                    if (initializeListener != null) {
                        initializeListener.onError();
                    }
                    return;
                }
                AtLog.d(TAG, "Init ysxSDK successfully", "");
                loginSdk();
            }
        });

    }

    private void registerSdkAuthListener() {
        authListener = new SdkAuthListener(initializeListener, this);
        YSXSdk.getInstance().addYsxAuthenticationListener(authListener);
    }

    private String getPreviousToken() {
        return APP_TOKEN;
//        return TokenHelper.getToken();
    }

    private void requestSdkToken(final HaApiCallback<String> listener) {
        if (listener == null) {
            return;
        }
        String token = getPreviousToken();
        if (!AtTexts.isEmpty(token)) {
            listener.onSuccess(token);
            return;
        }
        HaHttpClient.getInstance()
                .enqueue(new ReqKhSdkToken("15110036167"), new HaApiCallback<HaApiResponse<RespKhSdkToken>>() {
                    @Override
                    public void onError(int code, String message) {
                        listener.onError(code, message);
                    }

                    @Override
                    public void onSuccess(HaApiResponse<RespKhSdkToken> resp) {
                        AtLog.d(TAG, "requestSdkToken", "token:" + resp.data.token);
                        // todo
                        TokenHelper.saveToken(resp.data.token, 0);
                        listener.onSuccess(resp.data.token);
                    }
                });
    }

    private void loginSdk() {
        if (YSXSdk.getInstance().isLoggedIn()) {
            AtLog.d(TAG, "loginSdk", "sdk is loggedIn");
            if (initializeListener != null) {
                initializeListener.onInitialized(this);
            }
            return;
        }

        requestSdkToken(new HaApiCallback<String>() {
            @Override
            public void onError(int code, String message) {
                AtLog.d(TAG, "requestSdkToken", "failed: code:" + code + ", message:" + message);
                if (initializeListener != null) {
                    initializeListener.onError();
                }
            }

            @Override
            public void onSuccess(String content) {
                loginSdkByToken(content);
            }
        });
    }

    private void loginSdkByToken(String token) {
        registerSdkAuthListener();
        YSXSdk.getInstance().loginByToken(token, APP_KEY, APP_SECRET, new YSXLoginResultListener() {
            @Override
            public void onLoginResult(LoginResult result) {
                AtLog.d(TAG, "loginSdkByToken", "token:" + result.getSdktoken());
            }
        });
    }

    @Override
    public void unload() {
        if (authListener != null) {
            YSXSdk.getInstance().removeYsxAuthenticationListener(authListener);
        }
    }

    public void enable() {

    }

    public void disable() {

    }

    private String buildParticipants(List<String> list) {
        if (AtCollections.isEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    private void startInstantMeeting(Activity context, KhStartMeetingConfig config) {
        String participants = buildParticipants(config.participants);
        YSXMeetingSettingsHelper helper = YSXSdk.getInstance().getMeetingSettingsHelper();
        helper.setAutoConnectVoIPWhenJoinMeeting(config.autoConnectAudioJoined);
        helper.setMuteMyMicrophoneWhenJoinMeeting(config.autoMuteMicrophoneJoined);
        helper.setTurnOffMyVideoWhenJoinMeeting(!config.autoConnectVideoJoined);
        final YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        YSXInstantMeetingOptions options = new YSXInstantMeetingOptions();
        options.no_video = !config.autoConnectVideo;
        service.startInstantMeeting(context, config.topic, config.agenda, participants, options, new YSXMessageListener() {
            @Override
            public void onCallBack(int i, String s) {
                AtLog.d(TAG, "startInstantMeeting", "i = " + i + ", s = " + s);
            }
        });
    }

    private void startScheduledMeeting(Activity context, KhStartMeetingConfig config) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        YSXStartMeetingOptions opts = new YSXStartMeetingOptions();
        opts.no_audio = !config.autoConnectAudio;
        opts.no_video = !config.autoConnectVideo;
        YSXStartMeetingParams4NormalUser params = new YSXStartMeetingParams4NormalUser();
        params.meetingNo = config.No;
        service.startMeetingWithParams(context, config.id, config.type, config.No, params, opts, new YSXMessageListener() {
            @Override
            public void onCallBack(int i, String s) {
                AtLog.d(TAG, "startScheduledMeeting", i + " , " + s);
            }
        });
    }

    @Override
    public void startMeeting(Activity context, KhStartMeetingConfig config) {
        YSXMeetingSettingsHelper helper = YSXSdk.getInstance().getMeetingSettingsHelper();
        helper.setAutoConnectVoIPWhenJoinMeeting(config.autoConnectAudioJoined);
        helper.setMuteMyMicrophoneWhenJoinMeeting(config.autoMuteMicrophoneJoined);
        helper.setTurnOffMyVideoWhenJoinMeeting(!config.autoConnectVideoJoined);
        if (config.category == 0) {
            startInstantMeeting(context, config);
        } else if (config.category == 1) {
            startScheduledMeeting(context, config);
        }
    }

    @Override
    public void joinMeeting(Activity context, KhJoinMeetingConfig config) {
        YSXSdk sdk = YSXSdk.getInstance();
        YSXJoinMeetingOptions opts = new YSXJoinMeetingOptions();
        opts.no_audio = config.autoConnectAudio;
        opts.no_video = config.autoConnectVideo;
        YSXJoinMeetingParams params = new YSXJoinMeetingParams();
        params.meetingNo = config.No;
        params.password = config.password;
        params.displayName = config.displayName;

        YSXMeetingService service = sdk.getMeetingService();
        service.joinMeetingWithParams(context, config.id, config.type, params, opts, new YSXMessageListener() {
            @Override
            public void onCallBack(int i, String s) {
                AtLog.d(TAG, "joinInstantMeeting", i + " , " + s);
            }
        });
    }

    @Override
    public void leaveMeeting() {
        YSXSdk.getInstance().getMeetingService().leaveCurrentMeeting(false);
    }

    @Override
    public void concludeMeeting() {
        YSXSdk.getInstance().getMeetingService().leaveCurrentMeeting(true);
    }

    @Override
    public void sendMeetingInvite(KhSendInviteConfig config, final KhSendInviteListener listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.sendInvite(config.meetingId, config.userId, config.token, new ResponseListenerCommon<KhSendInviteResult>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhSendInviteResult result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void replyMeetingInvite(KhReplyInviteConfig config, final KhReplyInviteListener listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.postInviteAnswer(config.meetingId, config.meetingNo, config.answer, new ResponseListenerCommon<KhReplyInviteResult>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhReplyInviteResult result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void createScheduledMeeting(KhCreateScheduledConfig config, KhCreateScheduledListener listener) {

    }

    static class SdkAuthListener implements YSXSdkAuthenticationListener {
        private final OnSdkInitializeListener listener;
        private final YsxSdkPlugin plugin;

        public SdkAuthListener(OnSdkInitializeListener listener, YsxSdkPlugin plugin) {
            this.listener = listener;
            this.plugin = plugin;
        }

        @Override
        public void onYsxSDKLoginResult(long l) {
            if (l == 0) {
                if (listener != null) {
                    listener.onInitialized(plugin);
                }
            } else {
                if (listener != null) {
                    listener.onError();
                }
            }
        }

        @Override
        public void onYsxSDKLogoutResult(long l) {

        }

        @Override
        public void onYsxIdentityExpired() {

        }
    }
}
