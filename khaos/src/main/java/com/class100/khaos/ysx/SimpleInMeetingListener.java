package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXInMeetingAudioController;
import com.chinamobile.ysx.YSXInMeetingChatMessage;
import com.chinamobile.ysx.YSXInMeetingEventHandler;
import com.chinamobile.ysx.YSXInMeetingServiceListener;

import java.util.List;

public abstract class SimpleInMeetingListener implements YSXInMeetingServiceListener {
    @Override
    public void onMeetingNeedPasswordOrDisplayName(boolean b, boolean b1, YSXInMeetingEventHandler inMeetingEventHandler) {

    }

    @Override
    public void onWebinarNeedRegister() {

    }

    @Override
    public void onJoinWebinarNeedUserNameAndEmail(YSXInMeetingEventHandler inMeetingEventHandler) {

    }

    @Override
    public void onMeetingNeedColseOtherMeeting(YSXInMeetingEventHandler inMeetingEventHandler) {

    }

    @Override
    public void onMeetingFail(int i, int i1) {

    }

    @Override
    public void onMeetingLeaveComplete(long l) {

    }

    @Override
    public void onMeetingUserJoin(List<Long> list) {

    }

    @Override
    public void onMeetingUserLeave(List<Long> list) {

    }

    @Override
    public void onMeetingUserUpdated(long l) {

    }

    @Override
    public void onMeetingHostChanged(long l) {

    }

    @Override
    public void onMeetingCoHostChanged(long l) {

    }

    @Override
    public void onActiveVideoUserChanged(long var1) {

    }

    @Override
    public void onActiveSpeakerVideoUserChanged(long var1) {

    }

    @Override
    public void onSpotlightVideoChanged(boolean b) {

    }

    @Override
    public void onUserVideoStatusChanged(long l) {

    }

    @Override
    public void onMicrophoneStatusError(YSXInMeetingAudioController.YSXMobileRTCMicrophoneError mobileRTCMicrophoneError) {

    }

    @Override
    public void onUserAudioStatusChanged(long l) {

    }

    @Override
    public void onUserAudioTypeChanged(long l) {

    }

    @Override
    public void onMyAudioSourceTypeChanged(int i) {

    }

    @Override
    public void onLowOrRaiseHandStatusChanged(long l, boolean b) {

    }

    @Override
    public void onMeetingSecureKeyNotification(byte[] bytes) {

    }

    @Override
    public void onChatMessageReceived(YSXInMeetingChatMessage inMeetingChatMessage) {

    }

    @Override
    public void onUserNetworkQualityChanged(long l) {

    }

    @Override
    public void onSilentModeChanged(boolean b) {

    }

    @Override
    public void onHostAskUnMute(long l) {

    }

    @Override
    public void onHostAskStartVideo(long l) {

    }

    @Override
    public void onFreeMeetingReminder(boolean b, boolean b1, boolean b2) {

    }
}
