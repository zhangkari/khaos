package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXInMeetingShareController;
import com.chinamobile.ysx.YSXSdk;

public class MeetingShareCallback extends BaseCallback<MeetingShareCallback.ShareEvent> {

    public interface ShareEvent extends BaseEvent {
        void onShareActiveUser(long userId);

        void onShareUserReceivingStatus(long userId);
    }

    static MeetingShareCallback instance;

    private MeetingShareCallback() {
        init();
    }

    protected void init() {
        YSXSdk.getInstance().getInMeetingService().getInMeetingShareController().addListener(shareListener);
    }

    public static MeetingShareCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingShareCallback.class) {
                if (null == instance) {
                    instance = new MeetingShareCallback();
                }
            }
        }
        return instance;
    }

    YSXInMeetingShareController.YSXInMeetingShareListener shareListener = new YSXInMeetingShareController.YSXInMeetingShareListener() {
        @Override
        public void onShareActiveUser(long userId) {
            for (ShareEvent event : callbacks) {
                event.onShareActiveUser(userId);
            }
        }

        @Override
        public void onShareUserReceivingStatus(long userId) {
            for (ShareEvent event : callbacks) {
                event.onShareUserReceivingStatus(userId);
            }
        }
    };
}
