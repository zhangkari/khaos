package com.class100.khaos;

import com.class100.atropos.AtAbility;

public interface KhSdkAbility extends AtAbility {
    void startMeeting(KhStartMeetingConfig config);

    void joinMeeting(KhJoinMeetingConfig config);

    void sendMeetingInvite();
}
