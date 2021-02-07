package com.class100.khaos;

import android.app.Activity;

import com.class100.atropos.AtAbility;
import com.class100.khaos.req.KhReqDeleteMeeting;
import com.class100.khaos.req.KhReqGetMeetingInfo;
import com.class100.khaos.req.KhReqGetMeetingStatus;
import com.class100.khaos.req.KhReqJoinMeeting;
import com.class100.khaos.req.KhReqReplyInvite;
import com.class100.khaos.req.KhReqCreateScheduled;
import com.class100.khaos.req.KhReqGetMeetings;
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

public interface KhSdkAbility extends AtAbility {
    void startMeeting(Activity context, KhReqStartMeeting config);

    void joinMeeting(Activity context, KhReqJoinMeeting config);

    void leaveMeeting();

    void concludeMeeting();

    void sendMeetingInvite(KhReqSendInvite config, KhSdkListener<KhRespSendInvite> listener);

    void replyMeetingInvite(KhReqReplyInvite config, KhSdkListener<KhRespReplyInvite> listener);

    void createScheduledMeeting(KhReqCreateScheduled config, KhSdkListener<KhRespCreateScheduled> listener);

    void getMeetingInfo(KhReqGetMeetingInfo config, KhSdkListener<KhRespGetMeetingInfo> listener);

    void getMeetingStatus(KhReqGetMeetingStatus config, KhSdkListener<KhRespGetMeetingStatus> listener);

    void updateMeeting(KhReqUpdateMeeting config, KhSdkListener<KhRespUpdateMeeting> listener);

    void deleteMeeting(KhReqDeleteMeeting config, KhSdkListener<KhRespDeleteMeeting> listener);

    void getMeetings(KhReqGetMeetings config, KhSdkListener<KhRespGetMeetings> listener);
}
