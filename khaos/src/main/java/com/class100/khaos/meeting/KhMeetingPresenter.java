package com.class100.khaos.meeting;

import com.class100.atropos.generic.AtCollections;
import com.class100.khaos.KhSdkAbility;
import com.class100.khaos.KhSdkManager;
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
    public void loadMeetingTitle() {
        refreshMeetingTitle();
        // todo poll meeting duration
    }

    private void refreshMeetingTitle() {
        if (view != null) {
            KhMeetingContract.MeetingUser host = pickMeetingHostUser();
            if (host == null) {
                view.showMeetingTitle("", "", "00:00");
            } else {
                view.showMeetingTitle(host.name, KhSdkManager.getInstance().getSdk().getCurrentMeetingNo(), "00:05");
            }
        }
    }

    private KhMeetingContract.MeetingUser pickMeetingHostUser() {
        List<KhMeetingContract.MeetingUser> users = KhSdkManager.getInstance().getSdk().getMeetingUsers();
        KhMeetingContract.MeetingUser host = null;
        if (!AtCollections.isEmpty(users)) {
            for (KhMeetingContract.MeetingUser u : users) {
                if (u.isHost) {
                    host = u;
                    break;
                }
            }
        }
        return host;
    }

    @Override
    public void performMenuClick(int id) {
        switch (id) {
            case MenuConstants.menu_exit:
                view.showLeaveDialog();
                break;
            case MenuConstants.menu_audio:
                KhSdkAbility sdkAbility = KhSdkManager.getInstance().getSdk();
                if (sdkAbility.isAudioConnected()) {
                    if (sdkAbility.isMyAudioMuted()) {
                        if (sdkAbility.canUnmuteMyAudio()) {
                            sdkAbility.muteMyAudio(false);
                        }
                    } else {
                        sdkAbility.muteMyAudio(true);
                    }
                } else {
                    sdkAbility.connectAudioWithVoIP();
                }
                break;
            case MenuConstants.menu_camera:
                if (KhSdkManager.getInstance().getSdk().isMyVideoMuted()) {
                    if (KhSdkManager.getInstance().getSdk().canUnmuteMyVideo()) {
                        KhSdkManager.getInstance().getSdk().muteMyVideo(false);
                    }
                } else {
                    KhSdkManager.getInstance().getSdk().muteMyVideo(true);
                }
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
