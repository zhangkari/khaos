package com.class100.khaos.meeting;

import com.class100.atropos.env.context.AtRes;
import com.class100.khaos.KhSdkManager;
import com.class100.khaos.R;
import com.class100.khaos.meeting.vm.MeetingMenuItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KhMeetingModel implements KhMeetingContract.IMeetingModel {
    private List<MeetingMenuItem> buildMenuItems() {
        List<MeetingMenuItem> menus = new ArrayList<>(16);
        menus.add(new MeetingMenuItem(MenuConstants.menu_audio, AtRes.getString(R.string.kh_microphone), R.drawable.kh_ic_audio_off));
        menus.add(new MeetingMenuItem(MenuConstants.menu_camera, AtRes.getString(R.string.kh_camera), R.drawable.kh_ic_video_off));
//        menus.add(new MeetingMenuItem(MenuConstants.menu_chat, AtRes.getString(R.string.kh_chat), R.drawable.kh_ic_chat));
        menus.add(new MeetingMenuItem(MenuConstants.menu_attender, AtRes.getString(R.string.kh_attender), R.drawable.kh_ic_attender));
//        menus.add(new MeetingMenuItem(MenuConstants.menu_placement, AtRes.getString(R.string.kh_placement), R.drawable.kh_ic_placement));
        menus.add(new MeetingMenuItem(MenuConstants.menu_exit, AtRes.getString(R.string.kh_leave_meeting), R.drawable.kh_ic_exit));
        return menus;
    }

    @Override
    public void loadMenuItems(KhMeetingContract.ResultCallback<List<MeetingMenuItem>> callback) {
        if (callback == null) {
            return;
        }
        callback.onSuccess(checkMenuItem(buildMenuItems()));
    }

    private List<MeetingMenuItem> checkMenuItem(List<MeetingMenuItem> list) {
        Iterator<MeetingMenuItem> it = list.iterator();
        if (!KhSdkManager.getInstance().getSdk().isMeetingHost()) {
            while (it.hasNext()) {
                if (it.next().id == MenuConstants.menu_attender) {
                    it.remove();
                }
            }
        }
        return list;
    }

    @Override
    public void queryUsers(KhMeetingContract.ResultCallback<List<KhMeetingContract.MeetingUser>> callback) {
        if (callback == null) {
            return;
        }
        List<KhMeetingContract.MeetingUser> users = KhSdkManager.getInstance().getSdk().getMeetingUsers();
        callback.onSuccess(users);
    }

    @Override
    public void controlMeeting(int cmd, String... arguments) {
        switch (cmd) {
            case KhMeetingContract.cmd_leave_meeting:
                KhSdkManager.getInstance().getSdk().leaveMeeting();
                break;

            case KhMeetingContract.cmd_finish_meeting:
                KhSdkManager.getInstance().getSdk().concludeMeeting();
                break;

            case KhMeetingContract.cmd_disable_audio:
                KhSdkManager.getInstance().getSdk().muteAttendeeAudio(true, arguments[0]);
                break;

            case KhMeetingContract.cmd_disable_video:
                KhSdkManager.getInstance().getSdk().stopAttendeeVideo(arguments[0]);
                break;

            case KhMeetingContract.cmd_enable_audio:
                KhSdkManager.getInstance().getSdk().muteAttendeeAudio(false, arguments[0]);
                break;

            case KhMeetingContract.cmd_enable_video:
                KhSdkManager.getInstance().getSdk().askAttendeeVideo(arguments[0]);
                break;
        }
    }
}
