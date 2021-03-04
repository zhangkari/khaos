package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXSdk;

import java.util.List;

public class MeetingUserCallback extends BaseCallback<MeetingUserCallback.UserEvent> {
    public interface UserEvent extends BaseEvent {
        void onMeetingUserJoin(List<Long> list);

        void onMeetingUserLeave(List<Long> list);
    }

    static MeetingUserCallback instance;

    private MeetingUserCallback() {
        init();
    }

    protected void init() {
        YSXSdk.getInstance().getInMeetingService().addListener(userListener);
    }

    public static MeetingUserCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingUserCallback.class) {
                if (null == instance) {
                    instance = new MeetingUserCallback();
                }
            }
        }
        return instance;
    }

    SimpleInMeetingListener userListener = new SimpleInMeetingListener() {
        @Override
        public void onMeetingUserJoin(List<Long> list) {
            for (UserEvent event : callbacks) {
                event.onMeetingUserJoin(list);
            }
        }

        @Override
        public void onMeetingUserLeave(List<Long> list) {
            for (UserEvent event : callbacks) {
                event.onMeetingUserLeave(list);
            }
        }
    };
}