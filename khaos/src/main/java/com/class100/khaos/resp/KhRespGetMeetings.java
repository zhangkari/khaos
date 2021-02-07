package com.class100.khaos.resp;

import java.util.List;

public class KhRespGetMeetings {
    public List<Meeting> meetings;

    public static class Meeting {
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

    public static class Participant {
        public String id;
        public String name;
        public String mobileNo;
        public String email;

        public Participant() {
        }
    }
}
