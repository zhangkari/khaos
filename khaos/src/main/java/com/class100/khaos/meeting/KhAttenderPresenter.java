package com.class100.khaos.meeting;

import java.util.List;

public class KhAttenderPresenter implements KhMeetingContract.IAttenderPresenter {
    private KhMeetingContract.IAttenderView view;
    private final KhMeetingContract.IAttenderModel model;

    public KhAttenderPresenter(KhMeetingContract.IAttenderView view, KhMeetingContract.IAttenderModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestAttenders() {
        if (view != null) {
            view.showLoading();
        }
        model.queryUsers(new KhMeetingContract.ResultCallback<List<KhMeetingContract.MeetingUser>>() {
            @Override
            public void onSuccess(List<KhMeetingContract.MeetingUser> meetingUsers) {
                if (view != null) {
                    view.hideLoading();
                    view.showAttenders(meetingUsers);
                }
            }

            @Override
            public void onError(int code, String message) {
                if (view != null) {
                    view.hideLoading();
                    view.showError(code, message);
                }
            }
        });
    }

    @Override
    public void executeControlMeeting(int cmd, String... argument) {
        model.controlMeeting(cmd, argument);
    }

    @Override
    public void detach() {
        view = null;
    }
}
