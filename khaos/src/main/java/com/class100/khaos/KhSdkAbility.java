package com.class100.khaos;

import com.class100.atropos.AtAbility;

public interface KhSdkAbility extends AtAbility {
    void createMeeting(KhCreateInstantMeetingConfig config);

    void joinMeeting();

    void sendMeetingInvite();
}
