package com.class100.khaos.meeting;

import com.class100.atropos.generic.AtCollections;
import com.class100.khaos.KhSdkAbility;
import com.class100.khaos.KhSdkManager;
import com.class100.khaos.R;
import com.class100.khaos.meeting.vm.MeetingMenuItem;

import java.util.List;

public class KhMeetingPresenter extends KhAttenderPresenter implements KhMeetingContract.IMeetingPresenter {
    private KhMeetingContract.IMeetingView view;
    private final KhMeetingContract.IMeetingModel model;

    private List<MeetingMenuItem> cachedMenus;

    public KhMeetingPresenter(KhMeetingContract.IMeetingView view, KhMeetingContract.IMeetingModel model) {
        super(view, model);
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadMeetingMenu() {
        model.loadMenuItems(new KhMeetingContract.ResultCallback<List<MeetingMenuItem>>() {
            @Override
            public void onSuccess(List<MeetingMenuItem> meetingMenuItems) {
                if (view != null) {
                    cachedMenus = meetingMenuItems;
                    view.showMenu(meetingMenuItems);
                }
            }

            @Override
            public void onError(int code, String message) {
                if (view != null) {
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
                refreshMenuAudioStatus();
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
                refreshMenuVideoStatus();
                if (KhSdkManager.getInstance().getSdk().isMyVideoMuted()) {
                    if (KhSdkManager.getInstance().getSdk().canUnmuteMyVideo()) {
                        KhSdkManager.getInstance().getSdk().muteMyVideo(false);
                    }
                } else {
                    KhSdkManager.getInstance().getSdk().muteMyVideo(true);
                }
                break;

            case MenuConstants.menu_attender:
                if (view != null) {
                    view.showAttenderDialog();
                }
                break;

            case MenuConstants.menu_chat:
                // todo
                // no-pmd
                //
                break;

            case MenuConstants.menu_placement:
                // no-pmd
                // todo
                //
                break;

            default:
                break;
        }
    }

    @Override
    public void notifyUserJoin(List<String> users) {
        requestAttenders();
    }

    @Override
    public void notifyUserLeave(List<String> users) {
        requestAttenders();
    }

    private MeetingMenuItem findMenuItemById(int id) {
        if (AtCollections.isEmpty(cachedMenus)) {
            return null;
        }

        MeetingMenuItem item = null;
        for (MeetingMenuItem i : cachedMenus) {
            if (i.id == id) {
                item = i;
                break;
            }
        }
        return item;
    }

    @Override
    public void refreshMenuAudioStatus() {
        MeetingMenuItem item = findMenuItemById(MenuConstants.menu_audio);
        if (item == null) {
            return;
        }
        KhSdkAbility sdkAbility = KhSdkManager.getInstance().getSdk();
        if (sdkAbility.isAudioConnected()) {
            if (sdkAbility.isMyAudioMuted()) {
                item.icon = R.drawable.kh_ic_audio_off;
            } else {
                item.icon = R.drawable.kh_ic_audio_on;
            }
        } else {
            item.icon = R.drawable.kh_ic_no_audio;
        }

        if (view != null) {
            view.showMenu(cachedMenus);
        }
    }

    @Override
    public void refreshMenuVideoStatus() {
        MeetingMenuItem item = findMenuItemById(MenuConstants.menu_camera);
        if (item == null) {
            return;
        }

        KhSdkAbility sdkAbility = KhSdkManager.getInstance().getSdk();
        if (sdkAbility.isMyVideoMuted()) {
            item.icon = R.drawable.kh_ic_video_off;
        } else {
            item.icon = R.drawable.kh_ic_video_on;
        }
        if (view != null) {
            view.showMenu(cachedMenus);
        }
    }

    @Override
    public void detach() {
        view = null;
    }
}
