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
import com.chinamobile.ysx.bean.YSXMeetingList;
import com.chinamobile.ysx.responselistener.ResponseListenerCommon;
import com.class100.atropos.generic.AtLog;
import com.class100.atropos.generic.AtTexts;
import com.class100.hades.http.HaApiCallback;
import com.class100.hades.http.HaApiResponse;
import com.class100.hades.http.HaHttpClient;
import com.class100.khaos.KhAbsSdk;
import com.class100.khaos.KhSdkListener;
import com.class100.khaos.req.KhReqCreateScheduled;
import com.class100.khaos.req.KhReqDeleteMeeting;
import com.class100.khaos.req.KhReqGetMeetingInfo;
import com.class100.khaos.req.KhReqGetMeetingStatus;
import com.class100.khaos.req.KhReqGetMeetings;
import com.class100.khaos.req.KhReqJoinMeeting;
import com.class100.khaos.req.KhReqReplyInvite;
import com.class100.khaos.req.KhReqSendInvite;
import com.class100.khaos.req.KhReqStartMeeting;
import com.class100.khaos.req.KhReqUpdateMeeting;
import com.class100.khaos.resp.KhRespCreateScheduled;
import com.class100.khaos.resp.KhRespDeleteMeeting;
import com.class100.khaos.resp.KhRespGetMeetingInfo;
import com.class100.khaos.resp.KhRespGetMeetingStatus;
import com.class100.khaos.resp.KhRespGetMeetings;
import com.class100.khaos.resp.KhRespReplyInvite;
import com.class100.khaos.resp.KhRespSendInvite;
import com.class100.khaos.resp.KhRespUpdateMeeting;
import com.class100.khaos.ysx.internal.request.ReqKhSdkToken;
import com.class100.khaos.ysx.internal.response.RespKhSdkToken;

public class YsxSdkPlugin extends KhAbsSdk {
    private static final String TAG = "YsxSdkPlugin";

    // TODO Initialize int libNativeAtropos
    private static final String APP_KEY = "FOwvZJf5DjpizygZahOH9hgyciQmgOsXR5eC";
    private static final String APP_SECRET = "IQGrn2cvKiEdfPd44lOAof0fVUovoIZW0FMr";

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
        return YsxSdkHelper.getToken();
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
                .enqueue(new ReqKhSdkToken("56812345678"), new HaApiCallback<HaApiResponse<RespKhSdkToken>>() {
                    @Override
                    public void onError(int code, String message) {
                        listener.onError(code, message);
                    }

                    @Override
                    public void onSuccess(HaApiResponse<RespKhSdkToken> resp) {
                        AtLog.d(TAG, "requestSdkToken", "token:" + resp.data.token);
                        // todo
                        YsxSdkHelper.saveToken(resp.data.token, 0);
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

    private void startInstantMeeting(Activity context, KhReqStartMeeting config) {
        String participants = YsxSdkHelper.buildParticipants(config.participants);
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

    private void startScheduledMeeting(Activity context, KhReqStartMeeting config) {
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
    public void startMeeting(Activity context, KhReqStartMeeting config) {
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
    public void joinMeeting(Activity context, KhReqJoinMeeting config) {
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
    public void sendMeetingInvite(KhReqSendInvite config, final KhSdkListener<KhRespSendInvite> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.sendInvite(config.meetingId, config.userId, config.token, new ResponseListenerCommon<KhRespSendInvite>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhRespSendInvite result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void replyMeetingInvite(KhReqReplyInvite config, final KhSdkListener<KhRespReplyInvite> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.postInviteAnswer(config.meetingId, config.meetingNo, config.answer, new ResponseListenerCommon<KhRespReplyInvite>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhRespReplyInvite result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void createScheduledMeeting(KhReqCreateScheduled config, final KhSdkListener<KhRespCreateScheduled> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.createScheduledMeeting(config.topic, config.agenda, config.password,
                YsxSdkHelper.buildParticipants(config.participants),
                String.valueOf(config.openHostVideo),
                String.valueOf(config.openParticipantsVideo),
                String.valueOf(config.duration),
                YsxSdkHelper.formatTime(config.startTime),
                "utc start tme",
                config.token,
                new ResponseListenerCommon<KhRespCreateScheduled>() {
                    @Override
                    public void onFailure(Result result) {
                        if (listener != null) {
                            listener.onError(result.getCode(), result.getMsg());
                        }
                    }

                    @Override
                    public void onResponse(KhRespCreateScheduled result) {
                        if (listener != null) {
                            listener.onSuccess(result);
                        }
                    }
                }
        );
    }

    private void getMeetingInfoById(KhReqGetMeetingInfo config, final KhSdkListener<KhRespGetMeetingInfo> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.getMeetingInfoByID(config.meetingId, config.token, new ResponseListenerCommon<KhRespGetMeetingInfo>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhRespGetMeetingInfo result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    private void getMeetingInfoByNo(KhReqGetMeetingInfo config, final KhSdkListener<KhRespGetMeetingInfo> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.getMeetingInfoByNo(config.meetingNo, config.token, new ResponseListenerCommon<KhRespGetMeetingInfo>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhRespGetMeetingInfo result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void getMeetingInfo(KhReqGetMeetingInfo config, KhSdkListener<KhRespGetMeetingInfo> listener) {
        if (AtTexts.isEmpty(config.meetingId) && AtTexts.isEmpty(config.meetingNo)) {
            listener.onError(404, "meetingId and meetingNo both are empty !");
            return;
        }
        if (!AtTexts.isEmpty(config.meetingId)) {
            getMeetingInfoById(config, listener);
        } else {
            getMeetingInfoByNo(config, listener);
        }
    }

    @Override
    public void getMeetingStatus(KhReqGetMeetingStatus config, final KhSdkListener<KhRespGetMeetingStatus> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.getMeetingStatusByMeetingId(config.meetingId, config.token, new ResponseListenerCommon<KhRespGetMeetingStatus>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhRespGetMeetingStatus result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void updateMeeting(KhReqUpdateMeeting config, final KhSdkListener<KhRespUpdateMeeting> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.updateMeeting(config.meetingId, config.topic, config.agenda,
                YsxSdkHelper.buildParticipants(config.participants),
                String.valueOf(config.openHostVideo),
                String.valueOf(config.openParticipantsVideo),
                String.valueOf(config.duration),
                YsxSdkHelper.formatTime(config.startTime),
                YsxSdkHelper.formatTime(config.UTCStartTime),
                config.token,
                new ResponseListenerCommon<KhRespUpdateMeeting>() {
                    @Override
                    public void onFailure(Result result) {
                        if (listener != null) {
                            listener.onError(result.getCode(), result.getMsg());
                        }
                    }

                    @Override
                    public void onResponse(KhRespUpdateMeeting result) {
                        if (listener != null) {
                            listener.onSuccess(result);
                        }
                    }
                }
        );
    }

    @Override
    public void deleteMeeting(KhReqDeleteMeeting config, final KhSdkListener<KhRespDeleteMeeting> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.deleteMeeting(config.meetingId, config.token, new ResponseListenerCommon<KhRespDeleteMeeting>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    listener.onError(result.getCode(), result.getMsg());
                }
            }

            @Override
            public void onResponse(KhRespDeleteMeeting result) {
                if (listener != null) {
                    listener.onSuccess(result);
                }
            }
        });
    }

    @Override
    public void getMeetings(KhReqGetMeetings config, final KhSdkListener<KhRespGetMeetings> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.listMeeting(YsxSdkHelper.formatTime(config.startTime),
                YsxSdkHelper.formatTime(config.endTime),
                config.status,
                config.pageNo,
                config.pageSize,
                config.token,
                new ResponseListenerCommon<YSXMeetingList>() {
                    @Override
                    public void onFailure(Result result) {
                        if (listener != null) {
                            listener.onError(result.getCode(), result.getMsg());
                        }
                    }

                    @Override
                    public void onResponse(YSXMeetingList resp) {
                        if (listener != null) {
                            listener.onSuccess(YsxSdkHelper.adapt(resp));
                        }
                    }
                }
        );
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
