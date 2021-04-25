package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXSdk;

public class MeetingAudioCallback extends BaseCallback<MeetingAudioCallback.AudioEvent> {

    public interface AudioEvent extends BaseEvent {
        void onUserAudioStatusChanged(long userId);

        void onUserAudioTypeChanged(long userId);

        void onMyAudioSourceTypeChanged(int type);

        void onHostAskUnMute(long userId);
    }

    static MeetingAudioCallback instance;

    private MeetingAudioCallback() {
        init();
    }

    protected void init() {
        YSXSdk.getInstance().getInMeetingService().addListener(audioListener);
    }

    public static MeetingAudioCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingAudioCallback.class) {
                if (null == instance) {
                    instance = new MeetingAudioCallback();
                }
            }
        }
        return instance;
    }

    SimpleInMeetingListener audioListener = new SimpleInMeetingListener() {
        @Override
        public void onUserAudioStatusChanged(long userId) {

            for (AudioEvent event : callbacks) {
                event.onUserAudioStatusChanged(userId);
            }
        }

        @Override
        public void onUserAudioTypeChanged(long userId) {
            for (AudioEvent event : callbacks) {
                event.onUserAudioTypeChanged(userId);
            }
        }

        @Override
        public void onMyAudioSourceTypeChanged(int type) {
            for (AudioEvent event : callbacks) {
                event.onMyAudioSourceTypeChanged(type);
            }
        }

        @Override
        public void onHostAskUnMute(long userId) {
            for (AudioEvent event : callbacks) {
                event.onHostAskUnMute(userId);
            }
        }
    };

    /*
    YSXInMeetingEventHandler ysxInMeetingEventHandler = new YSXInMeetingEventHandler() {
        @Override
        public void setMeetingNamePassword(String s, String s1) {

        }

        @Override
        public boolean endOtherMeeting() {
            return false;
        }

        @Override
        public void setRegisterWebinarInfo(String s, String s1, boolean b) {

        }
    };
    */
}
