package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXSdk;

public class MeetingVideoCallback extends BaseCallback<MeetingVideoCallback.VideoEvent> {
    private static final String TAG = MeetingVideoCallback.class.getSimpleName();

    public interface VideoEvent extends BaseEvent {
        void onUserVideoStatusChanged(long userId);

        void onHostAskStartVideo(long userId);
    }

    private static MeetingVideoCallback instance;

    public static MeetingVideoCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingVideoCallback.class) {
                if (null == instance) {
                    instance = new MeetingVideoCallback();
                }
            }
        }
        return instance;
    }

    private MeetingVideoCallback() {
        init();
    }

    protected void init() {
        YSXSdk.getInstance().getInMeetingService().addListener(videoListener);
    }

    SimpleInMeetingListener videoListener = new SimpleInMeetingListener() {
        @Override
        public void onUserVideoStatusChanged(long userId) {
            for (VideoEvent event : callbacks) {
                event.onUserVideoStatusChanged(userId);
            }
        }

        @Override
        public void onHostAskStartVideo(long userId) {
            for (VideoEvent event : callbacks) {
                event.onHostAskStartVideo(userId);
            }
        }
    };
}
