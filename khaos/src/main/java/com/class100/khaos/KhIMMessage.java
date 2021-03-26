package com.class100.khaos;

import com.chinamobile.ysx.iminterface.InviteMeeting;

import java.io.Serializable;

public class KhIMMessage implements Serializable {
    private boolean DisableAudio;
    private boolean DisableVideo;
    private boolean IsMeetingEnd;
    private String OwnerId;
    private String UTCSendTime;
    private long MeetingNo;
    private String MeetingId;
    private int Action;
    private String Topic;
    private String Id;
    private String StartTime;
    private int MeetingType;
    private String SendTime;
    private String UserName;
    private String HeadPath;
    private int AnswerCode;
    private String Mobile;

    public boolean isDisableAudio() {
        return DisableAudio;
    }

    public void setDisableAudio(boolean disableAudio) {
        DisableAudio = disableAudio;
    }

    public boolean isDisableVideo() {
        return DisableVideo;
    }

    public void setDisableVideo(boolean disableVideo) {
        DisableVideo = disableVideo;
    }

    public boolean isMeetingEnd() {
        return IsMeetingEnd;
    }

    public void setMeetingEnd(boolean meetingEnd) {
        IsMeetingEnd = meetingEnd;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getUTCSendTime() {
        return UTCSendTime;
    }

    public void setUTCSendTime(String UTCSendTime) {
        this.UTCSendTime = UTCSendTime;
    }

    public long getMeetingNo() {
        return MeetingNo;
    }

    public void setMeetingNo(long meetingNo) {
        MeetingNo = meetingNo;
    }

    public String getMeetingId() {
        return MeetingId;
    }

    public void setMeetingId(String meetingId) {
        MeetingId = meetingId;
    }

    public int getAction() {
        return Action;
    }

    public void setAction(int action) {
        Action = action;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public int getMeetingType() {
        return MeetingType;
    }

    public void setMeetingType(int meetingType) {
        MeetingType = meetingType;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getHeadPath() {
        return HeadPath;
    }

    public void setHeadPath(String headPath) {
        HeadPath = headPath;
    }

    public int getAnswerCode() {
        return AnswerCode;
    }

    public void setAnswerCode(int answerCode) {
        AnswerCode = answerCode;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    @Override
    public String toString() {
        return "KhIMMessage{" +
                "DisableAudio=" + DisableAudio +
                ", DisableVideo=" + DisableVideo +
                ", IsMeetingEnd=" + IsMeetingEnd +
                ", OwnerId='" + OwnerId + '\'' +
                ", UTCSendTime='" + UTCSendTime + '\'' +
                ", MeetingNo=" + MeetingNo +
                ", MeetingId='" + MeetingId + '\'' +
                ", Action=" + Action +
                ", Topic='" + Topic + '\'' +
                ", Id='" + Id + '\'' +
                ", StartTime='" + StartTime + '\'' +
                ", MeetingType=" + MeetingType +
                ", SendTime='" + SendTime + '\'' +
                ", UserName='" + UserName + '\'' +
                ", HeadPath='" + HeadPath + '\'' +
                ", AnswerCode=" + AnswerCode +
                ", Mobile='" + Mobile + '\'' +
                '}';
    }


    public static KhIMMessage fromInviteMeeting(InviteMeeting inviteMeeting) {
        KhIMMessage khIMMessage = new KhIMMessage();
        khIMMessage.setDisableAudio(inviteMeeting.isDisableAudio());
        khIMMessage.setDisableAudio(inviteMeeting.isDisableAudio());
        khIMMessage.setMeetingEnd(inviteMeeting.isMeetingEnd());
        khIMMessage.setOwnerId(inviteMeeting.getOwnerId());
        khIMMessage.setUTCSendTime(inviteMeeting.getUTCSendTime());
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
        return null;
    }
}
