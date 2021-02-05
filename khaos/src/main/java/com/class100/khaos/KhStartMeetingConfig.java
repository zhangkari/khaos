package com.class100.khaos;

import java.util.List;

public class KhStartMeetingConfig extends KhMeetingConfig {
    public String topic;
    public String agenda;
    public int category;    // 0 instant meeting, 1 scheduled meeting
    public List<String> participants;
    public boolean autoConnectAudioJoined;
    public boolean autoMuteMicrophoneJoined;
    public boolean autoConnectVideoJoined;
}
