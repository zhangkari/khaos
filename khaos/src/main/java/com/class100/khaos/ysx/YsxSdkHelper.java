package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXSdk;
import com.chinamobile.ysx.bean.YSXMeetingList;
import com.chinamobile.ysx.iminterface.InviteMeeting;
import com.class100.atropos.generic.AtCollections;
import com.class100.khaos.KhIMMessage;
import com.class100.khaos.resp.KhRespGetMeetings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class YsxSdkHelper {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(PATTERN, Locale.CHINESE);

    public static String getToken() {
        return YSXSdk.getInstance().getYSXuser().getToken();
    }

    public static String buildParticipants(List<String> list) {
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

    public static String formatTime(long time) {
        return FORMAT.format(new Date(time));
    }

    private static KhRespGetMeetings.Meeting adapt(YSXMeetingList.Data data) {
        if (data == null) {
            return null;
        }
        KhRespGetMeetings.Meeting meeting = new KhRespGetMeetings.Meeting();
        meeting.agenda = data.getAgenda();
        meeting.duration = data.getDuration();
        meeting.endTime = data.getEndTime();
        meeting.hostName = data.getHostName();
        meeting.id = data.getId();
        meeting.meetingNo = data.getMeetingNo();
        meeting.meetingType = data.getMeetingType();
        meeting.mobileNo = data.getMobileNo();
        meeting.openHostVideo = data.getOpenHostVideo();
        meeting.ownerId = data.getOwnerId();
        meeting.startTime = data.getStartTime();
        meeting.status = data.getStatus();
        meeting.topic = data.getTopic();

        if (!AtCollections.isEmpty(data.getParticipants())) {
            final int size = data.getParticipants().size();
            meeting.participants = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                KhRespGetMeetings.Participant bean = new KhRespGetMeetings.Participant();
                bean.email = data.getParticipants().get(i).getEmail();
                bean.id = data.getParticipants().get(i).getId();
                bean.mobileNo = data.getParticipants().get(i).getMobileNo();
                bean.name = data.getParticipants().get(i).getName();
                meeting.participants.add(bean);
            }
        }
        return meeting;
    }

    public static KhRespGetMeetings adapt(YSXMeetingList list) {
        KhRespGetMeetings result = new KhRespGetMeetings();
        if (list.getCode() != 0 || AtCollections.isEmpty(list.getData())) {
            return result;
        }
        final int size = list.getData().size();
        result.meetings = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.meetings.add(adapt(list.getData().get(i)));
        }
        return result;
    }

    public static KhIMMessage getKhIMMessageFromInviteMeeting(InviteMeeting inviteMeeting) {
        KhIMMessage khIMMessage = new KhIMMessage();
        khIMMessage.setDisableAudio(inviteMeeting.isDisableAudio());
        khIMMessage.setDisableAudio(inviteMeeting.isDisableAudio());
        khIMMessage.setMeetingEnd(inviteMeeting.isMeetingEnd());
        khIMMessage.setOwnerId(inviteMeeting.getOwnerId());
        khIMMessage.setUtcSendTime(inviteMeeting.getUTCSendTime());
        khIMMessage.setMeetingNo(inviteMeeting.getMeetingNo());
        khIMMessage.setMeetingId(inviteMeeting.getMeetingId());
        khIMMessage.setAction(inviteMeeting.getAction());
        khIMMessage.setTopic(inviteMeeting.getTopic());
        khIMMessage.setId(inviteMeeting.getId());
        khIMMessage.setStartTime(inviteMeeting.getStartTime());
        khIMMessage.setMeetingType(inviteMeeting.getMeetingType());
        khIMMessage.setSendTime(inviteMeeting.getSendTime());
        khIMMessage.setUserName(inviteMeeting.getUserName());
        khIMMessage.setHeadPath(inviteMeeting.getHeadPath());
        khIMMessage.setAnswerCode(inviteMeeting.getAnswerCode());
        khIMMessage.setMobile(inviteMeeting.getMobile());
        return khIMMessage;
    }
}
