package com.class100.khaos.resp;

import java.io.Serializable;
import java.util.List;

public class KhRespGetMeetings {
    public static final int MEETING_STATUS_WAIT = 0; //未开始
    public static final int MEETING_STATUS_MEETING = 1; //进行中
    public static final int MEETING_STATUS_END = 2; //已经结束
    public static final int MEETING_STATUS_ALL = 3; //全部

    public static final int MEETING_TYPE_AUDIO = 0; //语音会议
    public static final int MEETING_TYPE_VIDEO = 1; //视频会议
    public static final int MEETING_TYPE_SEMINAR = 2; //研讨会议

    public List<Meeting> meetings;

    public static class Meeting implements Serializable {
        public String ownerId;
        public String hostName;
        public String mobileNo;
        public String id;
        public String topic;
        public String agenda;
        public int meetingNo;
        public String startTime;
        public String endTime;
        public boolean openHostVideo;
        public int duration;
        public int status;
        public int meetingType;
        public List<Participant> participants;
    }

    public static class Participant implements Serializable{
        public String id;
        public String name;
        public String mobileNo;
        public String email;

        public Participant() {
        }
    }
}
