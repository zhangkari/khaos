package com.class100.khaos.ysx;

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
import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtLog;
import com.class100.atropos.generic.AtTexts;
import com.class100.hades.http.HaApiCallback;
import com.class100.hades.http.HaApiResponse;
import com.class100.hades.http.HaHttpClient;
import com.class100.khaos.KhAbsSdk;
import com.class100.khaos.KhJoinMeetingConfig;
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

    @Override
    public void load() {
        YSXSdk sdk = YSXSdk.getInstance();
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
        YSXSdk.getInstance().addYsxAuthenticationListener(new YSXSdkAuthenticationListener() {
            @Override
            public void onYsxSDKLoginResult(long l) {
                if (l == 0) {
                    if (initializeListener != null) {
                        initializeListener.onInitialized(YsxSdkPlugin.this);
                    }
                } else {
                    if (initializeListener != null) {
                        initializeListener.onError();
                    }
                }
            }

            @Override
            public void onYsxSDKLogoutResult(long l) {

            }

            @Override
            public void onYsxIdentityExpired() {

            }
        });
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
        YSXSdk.getInstance().loginByToken(token, APP_KEY, APP_SECRET, new YSXLoginResultListener() {
            @Override
            public void onLoginResult(LoginResult result) {
                AtLog.d(TAG, "loginSdkByToken", "token:" + result.getSdktoken());
                registerSdkAuthListener();
            }
        });
    }

    @Override
    public void unload() {

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

    @Override
    public void startMeeting(KhStartMeetingConfig config) {
        String participants = buildParticipants(config.participants);
        YSXMeetingSettingsHelper helper = YSXSdk.getInstance().getMeetingSettingsHelper();
        helper.setAutoConnectVoIPWhenJoinMeeting(config.autoConnectAudioJoined);
        helper.setMuteMyMicrophoneWhenJoinMeeting(config.autoMuteMicrophoneJoined);
        helper.setTurnOffMyVideoWhenJoinMeeting(!config.autoEnableVideoJoined);
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();

        if (config.category == 0) {
            YSXInstantMeetingOptions options = new YSXInstantMeetingOptions();
            options.no_video = !config.autoConnectVideo;
            service.startInstantMeeting(env._app, config.topic, config.agenda, participants, options, new YSXMessageListener() {
                @Override
                public void onCallBack(int i, String s) {
                    AtLog.d(TAG, "startInstantMeeting", i + " , " + s);
                }
            });
        } else if (config.category == 1) {
            YSXStartMeetingOptions opts = new YSXStartMeetingOptions();
            opts.no_audio = !config.autoConnectAudio;
            opts.no_video = !config.autoConnectVideo;
            YSXStartMeetingParams4NormalUser params = new YSXStartMeetingParams4NormalUser();
            params.meetingNo = config.No;
            service.startMeetingWithParams(env._app, config.id, config.type, config.No, params, opts, new YSXMessageListener() {
                @Override
                public void onCallBack(int i, String s) {
                    AtLog.d(TAG, "startScheduledMeeting", i + " , " + s);
                }
            });
        }
    }

    @Override
    public void joinMeeting(KhJoinMeetingConfig config) {
        YSXSdk sdk = YSXSdk.getInstance();
        YSXJoinMeetingOptions opts = new YSXJoinMeetingOptions();
        opts.no_audio = config.autoConnectAudio;
        opts.no_video = config.autoConnectVideo;
        YSXJoinMeetingParams params = new YSXJoinMeetingParams();
        params.meetingNo = config.No;
        params.password = config.password;
        params.displayName = config.displayName;

        YSXMeetingService service = sdk.getMeetingService();
        service.joinMeetingWithParams(env._app, config.id, config.type, params, opts, new YSXMessageListener() {
            @Override
            public void onCallBack(int i, String s) {
                AtLog.d(TAG, "joinInstantMeeting", i + " , " + s);
            }
        });
    }

    @Override
    public void sendMeetingInvite() {

    }
}
