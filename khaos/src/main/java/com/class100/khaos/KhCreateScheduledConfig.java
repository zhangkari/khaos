package com.class100.khaos;

import java.util.List;

public class KhCreateScheduledConfig extends KhMeetingConfig {
    public String topic;
    public String agenda;
    public List<String> participants;
    public boolean openHostVideo;
    public boolean openParticipantsVideo;
    public int duration; // minutes
    public long startTime; // timestamp in seconds
    public String token;
}
