package com.class100.khaos;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.chinamobile.ysx.YSXMobileRTCSDKError;
import com.class100.atropos.AtAbility;
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

import java.util.List;

public interface KhSdkAbility extends AtAbility {
    @KhSdkConstants.KhSdkChannel
    String getName();

    void init(KhSdkConstants.InitParameters params);

    void startMeeting(Activity context, KhReqStartMeeting config);

    void joinMeeting(Activity context, KhReqJoinMeeting config);

    void joinMeeting(Activity context, KhReqJoinMeeting config, KhSdkListener<String> listener);

    void leaveMeeting();

    void concludeMeeting();

    void sendMeetingInvite(KhReqSendInvite config, KhSdkListener<KhRespSendInvite> listener);

    void replyMeetingInvite(KhReqReplyInvite config, KhSdkListener<KhRespReplyInvite> listener);

    void createScheduledMeeting(KhReqCreateScheduled config, KhSdkListener<KhRespCreateScheduled> listener);

    String getCurrentMeetingNo();

    String getCurrentMeetingId();

    void getMeetingInfo(KhReqGetMeetingInfo config, KhSdkListener<KhRespGetMeetingInfo> listener);

    KhMeetingStatus getMeetingStatus();

    void updateMeeting(KhReqUpdateMeeting config, KhSdkListener<KhRespUpdateMeeting> listener);

    void deleteMeeting(KhReqDeleteMeeting config, KhSdkListener<KhRespDeleteMeeting> listener);

    void getMeetings(KhReqGetMeetings config, KhSdkListener<KhRespGetMeetings> listener);

    KhUserProfile getUserProfile();

    List<KhMeetingContract.MeetingUser> getMeetingUsers();

    boolean isMeetingHost();

    boolean isHostUser(String userId);

    boolean isMyself(String userId);

    void rotateLocalVideo(int degree);

    void setMeetingUserChangedListener(OnMeetingUserChangedListener listener);

    void setUserVideoStatusChangedListener(OnUserVideoStatusChangedListener listener);

    void setUserAudioStatusChangedListener(OnUserAudioStatusChangedListener listener);

    //自己是否静音
    boolean isMyAudioMuted();

    //自己能否取消静音
    boolean canUnmuteMyAudio();

    //静音或取消静音
    void muteMyAudio(boolean mute);

    //音频是否连接
    boolean isAudioConnected();

    //断开音频连接
    void disconnectAudio();

    //连接音频
    void connectAudioWithVoIP();

    //静音某人或者取消静音某人
    void muteAttendeeAudio(boolean mute, long var2);

    //静音或者取消静音所有人
    void muteAllAttendeeAudio(boolean mute);

    //取消静音所有人
    void unmuteAllAttendeeAudio();


    void logout();

    void setInitializeListener(OnSdkInitializeListener listener);

    void addIMMessageListener(OnIMMessageListener listener);

    void removeIMMessageListener(OnIMMessageListener listener);

    void useIMSDk(boolean isUse);

    void addMeetingListener(OnMeetingStatusChangedListener listener);

    void removeMeetingListener(OnMeetingStatusChangedListener listener);

    //自己的视频是否被禁止
    boolean isMyVideoMuted();

    //是否可以关闭视频
    boolean canUnmuteMyVideo();

    //打开或者关闭自己的视频
    void muteMyVideo(boolean mute);

    interface OnSdkInitializeListener {
        void onInitialized(@NonNull KhAbsSdk sdk);

        void onError();
    }

    interface OnIMMessageListener {
        void onMessageReceived(KhIMMessage khIMMessage);
    }

    interface OnMeetingStatusChangedListener {
        void onMeetingStatusChanged(KhMeetingStatus status, int errorCode);
    }

    interface OnMeetingUserChangedListener {
        void onMeetingUserJoin(List<String> list);

        void onMeetingUserLeave(List<String> list);
    }

    interface OnUserVideoStatusChangedListener {
        void onUserVideoStatusChanged(String userId);
    }

    interface OnUserAudioStatusChangedListener {
        void onUserAudioStatusChanged(String userId);
        void onUserAudioTypeChanged(String userId);
        void onMyAudioSourceTypeChanged(int type);
    }

    enum KhMeetingStatus {
        MEETING_STATUS_IDLE(0),
        MEETING_STATUS_CONNECTING(1),
        MEETING_STATUS_WAITINGFORHOST(2),
        MEETING_STATUS_INMEETING(3),
        MEETING_STATUS_DISCONNECTING(7),
        MEETING_STATUS_RECONNECTING(8),
        MEETING_STATUS_FAILED(6),
        MEETING_STATUS_IN_WAITING_ROOM(9),
        MEETING_STATUS_WEBINAR_PROMOTE(10),
        MEETING_STATUS_WEBINAR_DEPROMOTE(11),
        MEETING_STATUS_TRIAL_END(12),
        MEETING_STATUS_UNKNOWN(13);

        private int value = 0;

        KhMeetingStatus(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    enum KhIMAction {
        ACTION_JOINMEETING(0),
        ACTION_STARTMEETING(1),
        ACTION_REFRESH_MEETINGLIST(2),
        ACTION_LEAVEMEETING(7),
        ACTION_MUTE(8),
        ACTION_UNMUTE(9),
        ACTION_INVITEANSWER_CALLBACK(12),
        ACTION_CLOUDRECORD_TRANSCODING_FINISH(26);

        private int value = 0;

        KhIMAction(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

}
