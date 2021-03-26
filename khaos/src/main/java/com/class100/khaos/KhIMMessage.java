package com.class100.khaos;

import com.chinamobile.ysx.iminterface.InviteMeeting;

import java.io.Serializable;

public class KhIMMessage implements Serializable {
    private boolean disableAudio;
    private boolean disableVideo;
    private boolean isMeetingEnd;
    private String ownerId;
    private String utcSendTime;
    private long meetingNo;
    private String meetingId;
    private int action;
    private String topic;
    private String id;
    private String startTime;
    private int meetingType;
    private String sendTime;
    private String userName;
    private String headPath;
    private int answerCode;
    private String mobile;

    public boolean isDisableAudio() {
        return disableAudio;
    }

    public void setDisableAudio(boolean disableAudio) {
        this.disableAudio = disableAudio;
    }

    public boolean isDisableVideo() {
        return disableVideo;
    }

    public void setDisableVideo(boolean disableVideo) {
        this.disableVideo = disableVideo;
    }

    public boolean isMeetingEnd() {
        return isMeetingEnd;
    }

    public void setMeetingEnd(boolean meetingEnd) {
        isMeetingEnd = meetingEnd;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getUtcSendTime() {
        return utcSendTime;
    }

    public void setUtcSendTime(String utcSendTime) {
        this.utcSendTime = utcSendTime;
    }

    public long getMeetingNo() {
        return meetingNo;
    }

    public void setMeetingNo(long meetingNo) {
        this.meetingNo = meetingNo;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(int meetingType) {
        this.meetingType = meetingType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public int getAnswerCode() {
        return answerCode;
    }

    public void setAnswerCode(int answerCode) {
        this.answerCode = answerCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    @Override
    public String toString() {
        return "KhIMMessage{" +
                "disableAudio=" + disableAudio +
                ", disableVideo=" + disableVideo +
                ", isMeetingEnd=" + isMeetingEnd +
                ", ownerId='" + ownerId + '\'' +
                ", utcSendTime='" + utcSendTime + '\'' +
                ", meetingNo=" + meetingNo +
                ", meetingId='" + meetingId + '\'' +
                ", action=" + action +
                ", topic='" + topic + '\'' +
                ", id='" + id + '\'' +
                ", startTime='" + startTime + '\'' +
                ", meetingType=" + meetingType +
                ", sendTime='" + sendTime + '\'' +
                ", userName='" + userName + '\'' +
                ", headPath='" + headPath + '\'' +
                ", answerCode=" + answerCode +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
