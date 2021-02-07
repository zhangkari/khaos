package com.class100.khaos.resp;

import java.util.List;

public class KhRespGetMeetingInfo {
    public String ownerId;
    public String hostName;
    public String id;
    public String topic;
    public String agenda;
    public int meetingNo;
    public String startTime;
    public String UTCStartTime;
    public String endTime;
    public boolean openHostVideo;
    public int duration;
    public int meetingType;
    public String password;
    public List<Participant> participants;

    public int status;

    public static class Participant {
        public String id;
        public String name;
        public String mobileNo;
    }
}
