package com.class100.khaos.ysx;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.chinamobile.ysx.YSXError;
import com.chinamobile.ysx.YSXImConfig;
import com.chinamobile.ysx.YSXInMeetingService;
import com.chinamobile.ysx.YSXInMeetingVideoController;
import com.chinamobile.ysx.YSXInstantMeetingOptions;
import com.chinamobile.ysx.YSXJoinMeetingOptions;
import com.chinamobile.ysx.YSXJoinMeetingParams;
import com.chinamobile.ysx.YSXMeetingService;
import com.chinamobile.ysx.YSXMeetingSettingsHelper;
import com.chinamobile.ysx.YSXMeetingStatus;
import com.chinamobile.ysx.YSXMessageListener;
import com.chinamobile.ysx.YSXSdk;
import com.chinamobile.ysx.YSXSdkAuthenticationListener;
import com.chinamobile.ysx.YSXStartMeetingOptions;
import com.chinamobile.ysx.YSXStartMeetingParams4NormalUser;
import com.chinamobile.ysx.auther.bean.YSXUser;
import com.chinamobile.ysx.bean.Result;
import com.chinamobile.ysx.bean.ScheduledMeetingInfo;
import com.chinamobile.ysx.bean.YSXMeetingInfo;
import com.chinamobile.ysx.bean.YSXMeetingInfoSimple;
import com.chinamobile.ysx.bean.YSXMeetingList;
import com.chinamobile.ysx.http.Enviroment;
import com.chinamobile.ysx.iminterface.IMOflineLinePushConfig;
import com.chinamobile.ysx.iminterface.ImConnectionListener;
import com.chinamobile.ysx.responselistener.ResponseListenerCommon;
import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtLog;
import com.class100.atropos.generic.AtTexts;
import com.class100.hades.http.HaApiCallback;
import com.class100.hades.http.HaApiResponse;
import com.class100.hades.http.HaHttpClient;
import com.class100.khaos.KhAbsSdk;
import com.class100.khaos.KhIMMessage;
import com.class100.khaos.KhSdkConstants;
import com.class100.khaos.KhSdkListener;
import com.class100.khaos.KhUserProfile;
import com.class100.khaos.meeting.KhMeetingContract;
import com.class100.khaos.req.KhReqCreateScheduled;
import com.class100.khaos.req.KhReqDeleteMeeting;
import com.class100.khaos.req.KhReqGetMeetingInfo;
import com.class100.khaos.req.KhReqGetMeetings;
import com.class100.khaos.req.KhReqJoinMeeting;
import com.class100.khaos.req.KhReqReplyInvite;
import com.class100.khaos.req.KhReqSendInvite;
import com.class100.khaos.req.KhReqStartMeeting;
import com.class100.khaos.req.KhReqUpdateMeeting;
import com.class100.khaos.resp.KhRespCreateScheduled;
import com.class100.khaos.resp.KhRespDeleteMeeting;
import com.class100.khaos.resp.KhRespGetMeetingInfo;
import com.class100.khaos.resp.KhRespGetMeetings;
import com.class100.khaos.resp.KhRespReplyInvite;
import com.class100.khaos.resp.KhRespSendInvite;
import com.class100.khaos.resp.KhRespUpdateMeeting;
import com.class100.khaos.ysx.internal.request.ReqKhSdkToken;
import com.class100.khaos.ysx.internal.response.RespKhSdkToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class YsxSdkPlugin extends KhAbsSdk {
    private static final String TAG = "YsxSdkPlugin";

    private SdkAuthListener authListener;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void load() {
        YSXSdk sdk = YSXSdk.getInstance();
        if (sdk.isInitialized()) {
            AtLog.d(TAG, "load", "sdk is initialized");
            loginSdk();
            return;
        }
        if (useIMSdk) {
            initializeIMSdk(sdk, initParameters.appKey, initParameters.appSecret);
        }
        sdk.initSDK(env._app,
                env._app,
                initParameters.appKey,
                initParameters.appSecret,
                initParameters.enableLog,
                initParameters.isDebugEnv ? Enviroment.ENV_DEBUG : Enviroment.ENV_RELEASE,
                (errorCode, internalErrorCode) -> {
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
                });
    }

    private void initializeIMSdk(YSXSdk sdk, String appKey, String appSecret) {
        IMOflineLinePushConfig imOflineLinePushConfig = new IMOflineLinePushConfig();
        imOflineLinePushConfig.setMI_PUSH_APP_KEY(appSecret);
        imOflineLinePushConfig.setMI_PUSH_APPID(appKey);
        YSXImConfig.Builder builder = new YSXImConfig.Builder();
        builder.setImConnectionListener(new ImConnectionListener() {
            @Override
            public void onConnected() {
                AtLog.d(TAG, "initializeIMSdk", "onConnected");
            }

            @Override
            public void onDisconnected(int i) {
                AtLog.d(TAG, "initializeIMSdk", "onDisconnected");
                YSXSdk sdk12 = YSXSdk.getInstance();
                YSXMeetingService meetingService = sdk12.getMeetingService();
                meetingService.leaveCurrentMeeting(false);
            }
        }).setImMessageListener(inviteMeeting -> {
            AtLog.d(TAG, "onMessageReceived", "action:" + inviteMeeting.getAction() + ", meetingNo:" + inviteMeeting.getMeetingNo());
            if (!AtCollections.isEmpty(imMessageListeners)) {
                KhIMMessage message = YsxSdkHelper.getKhIMMessageFromInviteMeeting(inviteMeeting);
                mHandler.post(() -> {
                    for (OnIMMessageListener listener : imMessageListeners) {
                        listener.onMessageReceived(message);
                    }
                });
            }
        }).setIMOflineLinePushConfig(imOflineLinePushConfig);
        YSXImConfig ysxImConfig = builder.create();
        sdk.setYSXImConfig(ysxImConfig);
    }

    private void customizeMeetingUI(boolean customized) {
        YSXSdk.getInstance().getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(customized);
    }

    private void registerSdkAuthListener() {
        authListener = new SdkAuthListener(meetingListeners, initializeListener, this);
        YSXSdk.getInstance().addYsxAuthenticationListener(authListener);
    }

    private void requestSdkToken(final HaApiCallback<String> listener) {
        if (listener == null) {
            return;
        }

        HaHttpClient.getInstance()
                .enqueue(new ReqKhSdkToken(), new HaApiCallback<HaApiResponse<RespKhSdkToken>>() {
                    @Override
                    public void onError(int code, String message) {
                        listener.onError(code, message);
                    }

                    @Override
                    public void onSuccess(HaApiResponse<RespKhSdkToken> resp) {
                        if (resp != null && resp.code == 0 && resp.data != null && resp.data.code == 200) {
                            AtLog.d(TAG, "requestSdkToken", "token:" + resp.data.token);
                            listener.onSuccess(resp.data.token);
                        }
                    }
                });
    }

    private void loginSdk() {
        if (YSXSdk.getInstance().isLoggedIn()) {
            AtLog.d(TAG, "loginSdk", "sdk is loggedIn");
            customizeMeetingUI(true);
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
            public void onSuccess(String token) {
                loginSdkByToken(token);
            }
        });
    }

    private void loginSdkByToken(String token) {
        AtLog.d(TAG, "loginByToken", "previous token:" + token);
        registerSdkAuthListener();
        YSXSdk.getInstance().loginByToken(token, initParameters.appKey, initParameters.appSecret, result -> {
            AtLog.d(TAG, "loginSdkByToken", "new token:" + result.getSdktoken());
            if (result.code == 0) {
                customizeMeetingUI(true);
                String userId = YSXSdk.getInstance().getYSXuser().getUserId();
                String userName = YSXSdk.getInstance().getYSXuser().getUserName();
                AtLog.d(TAG, "loginSdkByToken", "userId:" + userId + ", userName:" + userName);
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
        options.no_driving_mode = true;
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
    public String getName() {
        return KhSdkConstants.SDK_YSX;
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
        joinMeeting(context, config, null);
    }

    @Override
    public void joinMeeting(Activity context, KhReqJoinMeeting config, final KhSdkListener<String> listener) {
        YSXSdk sdk = YSXSdk.getInstance();
        YSXJoinMeetingOptions opts = new YSXJoinMeetingOptions();
        opts.no_audio = !config.autoConnectAudio;
        opts.no_video = !config.autoConnectVideo;
        YSXJoinMeetingParams params = new YSXJoinMeetingParams();
        params.meetingNo = config.No;
        params.password = config.password;
        params.displayName = config.displayName;

        YSXMeetingService service = sdk.getMeetingService();
        service.joinMeetingWithParams(context, config.id, config.type, params, opts, (i, s) -> {
            AtLog.d(TAG, "joinInstantMeeting", i + " , " + s);
            if (listener != null) {
                if (i == KhMeetingStatus.MEETING_STATUS_INMEETING.value()) {
                    listener.onSuccess(s);
                } else {
                    listener.onError(i, s);
                }
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

    // todo
    @Override
    public void createScheduledMeeting(KhReqCreateScheduled config, final KhSdkListener<KhRespCreateScheduled> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.createScheduledMeeting(config.topic, config.agenda, config.password,
                YsxSdkHelper.buildParticipants(config.participants),
                String.valueOf(config.openHostVideo),
                String.valueOf(config.openParticipantsVideo),
                String.valueOf(config.duration),
                YsxSdkHelper.formatTime(config.startTime),
                YsxSdkHelper.formatTime(config.startTime - 8 * 60 * 60 * 1000),
//                config.token,
                YSXSdk.getInstance().getYSXuser().getToken(),
                new ResponseListenerCommon<ScheduledMeetingInfo>() {
                    @Override
                    public void onFailure(Result result) {
                        if (listener != null) {
                            listener.onError(result.getCode(), result.getMsg());
                        }
                    }

                    @Override
                    public void onResponse(ScheduledMeetingInfo result) {
                        if (listener != null) {
                            if (result.getCode() == 0) {
                                listener.onSuccess(adaptScheduledMeetingInfo(result));
                            } else {
                                listener.onError(result.getCode(), result.getMessage());
                            }
                        }
                    }
                }
        );
    }

    @Override
    public String getCurrentMeetingNo() {
        String No = String.valueOf(YSXSdk.getInstance().getMeetingService().getCurrentMeetingNumber());
        AtLog.d(TAG, "getCurrentMeetingNo()", "No = " + No);
        return No;
    }

    @Override
    public String getCurrentMeetingId() {
        String id = YSXSdk.getInstance().getMeetingService().getCurrentMeetingID();
        String id2 = YSXSdk.getInstance().getInMeetingService().getCurrentMeetingID();
        AtLog.d(TAG, "getCurrentMeetingId()", "Id = " + id + ", Id2 = " + id2);
        return id2;
    }

    private KhRespCreateScheduled adaptScheduledMeetingInfo(ScheduledMeetingInfo info) {
        KhRespCreateScheduled resp = new KhRespCreateScheduled();
        if (info.getData() != null) {
            resp.duration = info.getData().getDuration();
            resp.endTime = info.getData().getEndTime();
            resp.id = info.getData().getId();
            resp.meetingNo = info.getData().getMeetingNo();
            resp.meetingType = info.getData().getMeetingType();
            resp.openHostVideo = info.getData().getOpenHostVideo();
            resp.startTime = info.getData().getStartTime();
            resp.status = info.getData().getStatus();
            resp.topic = info.getData().getTopic();
        }
        return resp;
    }

    private void getMeetingInfoById(KhReqGetMeetingInfo config, final KhSdkListener<KhRespGetMeetingInfo> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.getMeetingInfoByID(config.meetingId, YsxSdkHelper.getToken(), new ResponseListenerCommon<YSXMeetingInfo>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    mHandler.post(() -> listener.onError(result.getCode(), result.getMsg()));
                }
            }

            @Override
            public void onResponse(YSXMeetingInfo result) {
                if (listener != null) {
                    KhRespGetMeetingInfo info = YsxSdkHelper.adapt(result);
                    mHandler.post(() -> {
                                if (info == null) {
                                    listener.onError(result.getCode(), "error");
                                } else {
                                    listener.onSuccess(info);
                                }
                            }

                    );

                }
            }
        });
    }

    private void getMeetingInfoByNo(KhReqGetMeetingInfo config, final KhSdkListener<KhRespGetMeetingInfo> listener) {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        service.getMeetingInfoByNo(config.meetingNo, YsxSdkHelper.getToken(), new ResponseListenerCommon<YSXMeetingInfoSimple>() {
            @Override
            public void onFailure(Result result) {
                if (listener != null) {
                    mHandler.post(() -> listener.onError(result.getCode(), result.getMsg()));
                }
            }

            @Override
            public void onResponse(YSXMeetingInfoSimple result) {
                if (listener != null) {
                    KhRespGetMeetingInfo info = YsxSdkHelper.adapt(result);
                    mHandler.post(() -> {
                                if (info == null) {
                                    listener.onError(result.getCode(), "error");
                                } else {
                                    listener.onSuccess(info);
                                }
                            }

                    );
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
    public KhMeetingStatus getMeetingStatus() {
        YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
        return adaptMeetingStatus(service.getMeetingStatus());
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
        service.listMeeting(
                YsxSdkHelper.formatTime(config.startTime),
                YsxSdkHelper.formatTime(config.endTime),
                config.status,
                config.pageNo,
                config.pageSize,
                YsxSdkHelper.getToken(),
                new ResponseListenerCommon<YSXMeetingList>() {
                    @Override
                    public void onFailure(Result result) {
                        if (listener != null) {
                            mHandler.post(() -> listener.onError(result.getCode(), result.getMsg()));
                        }
                    }

                    @Override
                    public void onResponse(YSXMeetingList resp) {
                        if (listener != null) {
                            mHandler.post(() -> listener.onSuccess(YsxSdkHelper.adapt(resp)));
                        }
                    }
                }
        );
    }

    @Override
    public KhUserProfile getUserProfile() {
        YSXUser user = YSXSdk.getInstance().getYSXuser();
        if (user == null) {
            return null;
        }
        return new KhUserProfile(user.getUserId(), user.getUserName());
    }

    private KhMeetingContract.MeetingUser buildMeetingUserById(String id) {
        KhMeetingContract.MeetingUser user = new KhMeetingContract.MeetingUser();
        user.id = id;
        YSXInMeetingService service = YSXSdk.getInstance().getInMeetingService();
        long uid = Long.parseLong(id);
        user.name = service.getUserInfoById(uid).getUserName();
        user.isHost = service.isHostUser(uid);
        return user;
    }

    private KhMeetingContract.MeetingUser buildLocalUser() {
        KhMeetingContract.MeetingUser user = new KhMeetingContract.MeetingUser();
        YSXUser sdk = YSXSdk.getInstance().getYSXuser();
        user.id = sdk.getUserId();
        user.name = sdk.getUserName();
        user.isHost = true;
        return user;
    }

    @Override
    public List<KhMeetingContract.MeetingUser> getMeetingUsers() {
        YSXInMeetingService service = YSXSdk.getInstance().getInMeetingService();
        List<Long> users = service.getInMeetingUserList();
        List<KhMeetingContract.MeetingUser> data;
        if (AtCollections.isEmpty(users)) {
            AtLog.d(TAG, "getMeetingUsers", "inMeeting list size = 0");
            data = new ArrayList<>(1);
            data.add(buildLocalUser());
        } else {
            AtLog.d(TAG, "getMeetingUsers", "inMeeting list size = " + users.size());
            data = new ArrayList<>(users.size());
            for (Long id : users) {
                data.add(buildMeetingUserById(String.valueOf(id)));
            }
        }
        return data;
    }

    @Override
    public boolean isMeetingHost() {
        return YSXSdk.getInstance().getInMeetingService().isMeetingHost();
    }

    @Override
    public boolean isHostUser(String userId) {
        return YSXSdk.getInstance().getInMeetingService().isHostUser(Long.parseLong(userId));
    }

    @Override
    public boolean isMyself(String userId) {
        return YSXSdk.getInstance().getInMeetingService().isMyself(Long.parseLong(userId));
    }

    @Override
    public void rotateLocalVideo(int degree) {
        YSXSdk.getInstance()
                .getInMeetingService()
                .getInMeetingVideoController()
                .rotateMyVideo(degree);
    }

    @Override
    public void setMeetingUserChangedListener(final OnMeetingUserChangedListener listener) {
        if (listener == null) {
            MeetingUserCallback.getInstance().clear();
            return;
        }
        MeetingUserCallback.getInstance().addListener(new MeetingUserCallback.UserEvent() {
            @Override
            public void onMeetingUserJoin(List<Long> list) {
                listener.onMeetingUserJoin(adapt(list));
            }

            @Override
            public void onMeetingUserLeave(List<Long> list) {
                listener.onMeetingUserLeave(adapt(list));
            }
        });
    }

    private List<String> adapt(List<Long> list) {
        if (AtCollections.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>(list.size());
        for (long val : list) {
            result.add(String.valueOf(val));
        }
        return result;
    }

    @Override
    public void setUserVideoStatusChangedListener(final OnUserVideoStatusChangedListener listener) {
        if (listener == null) {
            MeetingVideoCallback.getInstance().clear();
            return;
        }
        MeetingVideoCallback.getInstance().addListener(new MeetingVideoCallback.VideoEvent() {
            @Override
            public void onUserVideoStatusChanged(long userId) {
                listener.onUserVideoStatusChanged(String.valueOf(userId));
            }

            @Override
            public void onHostAskStartVideo(long userId) {
                listener.onHostAskStartVideo(String.valueOf(userId));
            }
        });

    }

    @Override
    public void setUserAudioStatusChangedListener(final OnUserAudioStatusChangedListener listener) {
        if (listener == null) {
            MeetingAudioCallback.getInstance().clear();
            return;
        }
        MeetingAudioCallback.getInstance().addListener(new MeetingAudioCallback.AudioEvent() {
            @Override
            public void onUserAudioStatusChanged(long userId) {
                listener.onUserAudioStatusChanged(String.valueOf(userId));
            }

            @Override
            public void onUserAudioTypeChanged(long userId) {
                listener.onUserAudioTypeChanged(String.valueOf(userId));
            }

            @Override
            public void onMyAudioSourceTypeChanged(int type) {
                listener.onMyAudioSourceTypeChanged(String.valueOf(type));
            }

            @Override
            public void onHostAskUnMute(long userId) {
                listener.onHostAskUnMute(String.valueOf(userId));
            }
        });
    }

    @Override
    public boolean isMyAudioMuted() {
        return YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().isMyAudioMuted();
    }

    @Override
    public boolean canUnmuteMyAudio() {
        return YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().canUnmuteMyAudio();
    }

    @Override
    public void muteMyAudio(boolean mute) {
        YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().muteMyAudio(mute);
    }

    @Override
    public boolean isAudioConnected() {
        return YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().isAudioConnected();
    }

    @Override
    public void disconnectAudio() {
        YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().disconnectAudio();
    }

    @Override
    public void connectAudioWithVoIP() {
        YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().connectAudioWithVoIP();
    }

    @Override
    public void muteAttendeeAudio(boolean mute, String userId) {
        YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().muteAttendeeAudio(mute, Long.parseLong(userId));
    }

    @Override
    public void muteAllAttendeeAudio(boolean mute) {
        YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().muteAllAttendeeAudio(mute);
    }

    @Override
    public void unmuteAllAttendeeAudio() {
        YSXSdk.getInstance().getInMeetingService().getInMeetingAudioController().unmuteAllAttendeeAudio();
    }

    @Override
    public void askAttendeeVideo(String userId) {
        YSXInMeetingVideoController controller = YSXSdk.getInstance().getInMeetingService().getInMeetingVideoController();
        controller.askAttendeeStartVideo(Long.parseLong(userId));
    }

    @Override
    public void stopAttendeeVideo(String userId) {
        YSXInMeetingVideoController controller = YSXSdk.getInstance().getInMeetingService().getInMeetingVideoController();
        controller.stopAttendeeVideo(Long.parseLong(userId));
    }

    @Override
    public void logout() {
        YSXSdk.getInstance().sdkLogout();
        YSXUser user = YSXSdk.getInstance().getYSXuser();
        if (user != null) {
            user.setUserName("");
            user.setUserId("");
        }
    }

    @Override
    public boolean isMyVideoMuted() {
        return YSXSdk.getInstance().getInMeetingService().getInMeetingVideoController().isMyVideoMuted();
    }

    @Override
    public boolean canUnmuteMyVideo() {
        return YSXSdk.getInstance().getInMeetingService().getInMeetingVideoController().canUnmuteMyVideo();
    }

    @Override
    public void muteMyVideo(boolean mute) {
        YSXSdk.getInstance().getInMeetingService().getInMeetingVideoController().muteMyVideo(mute);
    }

    static class SdkAuthListener implements YSXSdkAuthenticationListener {
        private final Collection<OnMeetingStatusChangedListener> statusChangedListeners;
        private final OnSdkInitializeListener initializeListener;
        private final YsxSdkPlugin plugin;

        public SdkAuthListener(Collection<OnMeetingStatusChangedListener> listeners, OnSdkInitializeListener listener, YsxSdkPlugin plugin) {
            statusChangedListeners = listeners;
            initializeListener = listener;
            this.plugin = plugin;
        }

        @Override
        public void onYsxSDKLoginResult(long l) {
            if (l == 0) {
                if (initializeListener != null) {
                    initializeListener.onInitialized(plugin);
                }
                registerStatusChangedListener((status, errorCode) -> {
                    for (OnMeetingStatusChangedListener listener : statusChangedListeners) {
                        listener.onMeetingStatusChanged(status, errorCode);
                    }
                });
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

        private void registerStatusChangedListener(final OnMeetingStatusChangedListener listener) {
            YSXMeetingService service = YSXSdk.getInstance().getMeetingService();
            service.addListener((ysxMeetingStatus, i, i1) -> listener.onMeetingStatusChanged(adaptMeetingStatus(ysxMeetingStatus), i));
        }
    }

    private static KhMeetingStatus adaptMeetingStatus(YSXMeetingStatus status) {
        return convertEnum(status, KhMeetingStatus.class);
    }

    private static <EnumFrom extends Enum<?>, EnumTo extends Enum<?>> EnumTo convertEnum(EnumFrom from, Class<EnumTo> to) {
        EnumTo rReturn = null;
        if (to.isEnum()) {
            EnumTo[] array = to.getEnumConstants();
            for (EnumTo enu : array) {
                if (enu.toString().equals(from.toString())) {
                    rReturn = enu;
                    break;
                }
            }
        }
        return rReturn;
    }
}
