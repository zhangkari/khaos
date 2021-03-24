package com.class100.khaos.meeting;

import java.util.List;

public class KhMeetingPresenter implements KhMeetingContract.IMeetingPresenter {
    private KhMeetingContract.IMeetingView view;
    private final KhMeetingContract.IMeetingModel model;

    public KhMeetingPresenter(KhMeetingContract.IMeetingView view, KhMeetingContract.IMeetingModel model) {
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
    public void notifyUserJoin(List<String> users) {
        requestAttenders();
    }

    @Override
    public void notifyUserLeave(List<String> users) {
        requestAttenders();
    }

    @Override
    public void detach() {
        view = null;
    }
}
