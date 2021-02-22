package com.class100.khaos.req;

import java.util.List;

public class KhReqStartMeeting extends KhReqMeeting {
    public String topic;
    public String agenda;
    public int category;    // 0 instant meeting, 1 scheduled meeting
    public List<String> participants;
    public boolean autoConnectAudioJoined;
    public boolean autoMuteMicrophoneJoined;
    public boolean autoConnectVideoJoined;
}
