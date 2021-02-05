package com.class100.khaos;

import android.app.Activity;

import com.class100.atropos.AtAbility;

public interface KhSdkAbility extends AtAbility {
    void startMeeting(Activity context, KhStartMeetingConfig config);

    void joinMeeting(Activity context, KhJoinMeetingConfig config);

    void leaveMeeting();

    void concludeMeeting();

    void sendMeetingInvite(KhSendInviteConfig config, KhSendInviteListener listener);

    void replyMeetingInvite(KhReplyInviteConfig config, KhReplyInviteListener listener);

    void createScheduledMeeting(KhCreateScheduledConfig config, KhCreateScheduledListener listener);
}
