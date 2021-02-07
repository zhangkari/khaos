package com.class100.khaos.req;

import java.util.List;

public class KhReqUpdateMeeting {
    public String meetingId;
    public String topic;
    public String agenda;
    public String token;
    public List<String> participants;
    public boolean openHostVideo;
    public boolean openParticipantsVideo;
    public int duration;  // minutes
    public long startTime; // timestamp in milliseconds
    public long UTCStartTime; // timestamp in milliseconds
}
