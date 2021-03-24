package com.class100.khaos.meeting;

import com.class100.khaos.meeting.vm.MeetingMenuItem;

import java.util.List;

public class KhMeetingPresenter implements KhMeetingContract.IMeetingPresenter {
    private KhMeetingContract.IMeetingView view;
    private final KhMeetingContract.IMeetingModel model;

    public KhMeetingPresenter(KhMeetingContract.IMeetingView view, KhMeetingContract.IMeetingModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadMeetingMenu() {
        model.loadMenuItems(new KhMeetingContract.ResultCallback<List<MeetingMenuItem>>() {
            @Override
            public void onSuccess(List<MeetingMenuItem> meetingMenuItems) {
                if (view != null) {
                    view.hideLoading();
                    view.showMenu(meetingMenuItems);
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
    public void performMenuClick(int id) {
        switch (id) {
            case MenuConstants.menu_exit:
                view.showLeaveDialog();
                break;
        }
    }

    @Override
    public void executeControlMeeting(int cmd) {

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
