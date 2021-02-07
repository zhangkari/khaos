package com.class100.khaos.req;

import java.util.List;

public class KhReqCreateScheduled extends KhReqMeeting {
    public String topic;
    public String agenda;
    public String password;
    public List<String> participants;
    public boolean openHostVideo;
    public boolean openParticipantsVideo;
    public int duration; // minutes
    public long startTime; // timestamp in milliseconds
    public String token;
}
