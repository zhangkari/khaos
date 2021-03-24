package com.class100.khaos.meeting;

import com.chinamobile.ysx.YSXInMeetingService;
import com.chinamobile.ysx.YSXSdk;
import com.class100.atropos.env.context.AtRes;
import com.class100.atropos.generic.AtCollections;
import com.class100.khaos.KhSdkManager;
import com.class100.khaos.R;
import com.class100.khaos.meeting.vm.MeetingMenuItem;

import java.util.ArrayList;
import java.util.List;

public class KhMeetingModel implements KhMeetingContract.IMeetingModel {
    private List<MeetingMenuItem> buildMenuItems() {
        List<MeetingMenuItem> menus = new ArrayList<>(16);
        menus.add(new MeetingMenuItem(MenuConstants.menu_audio, AtRes.getString(R.string.kh_microphone), R.drawable.kh_ic_audio));
        menus.add(new MeetingMenuItem(MenuConstants.menu_camera, AtRes.getString(R.string.kh_camera), R.drawable.kh_ic_video));
        menus.add(new MeetingMenuItem(MenuConstants.menu_chat, AtRes.getString(R.string.kh_chat), R.drawable.kh_ic_chat));
        menus.add(new MeetingMenuItem(MenuConstants.menu_attender, AtRes.getString(R.string.kh_attender), R.drawable.kh_ic_attender));
        menus.add(new MeetingMenuItem(MenuConstants.menu_placement, AtRes.getString(R.string.kh_placement), R.drawable.kh_ic_placement));
        menus.add(new MeetingMenuItem(MenuConstants.menu_exit, AtRes.getString(R.string.kh_leave_meeting), R.drawable.kh_ic_exit));
        return menus;
    }

    @Override
    public void loadMenuItems(KhMeetingContract.ResultCallback<List<MeetingMenuItem>> callback) {
        if (callback == null) {
            return;
        }
        callback.onSuccess(buildMenuItems());
    }

    @Override
    public void queryUsers(KhMeetingContract.ResultCallback<List<KhMeetingContract.MeetingUser>> callback) {
        if (callback == null) {
            return;
        }
        List<String> users = KhSdkManager.getInstance().getSdk().getMeetingUsers();
        if (AtCollections.isEmpty(users)) {
            callback.onSuccess(new ArrayList<KhMeetingContract.MeetingUser>(0));
            return;
        }

        List<KhMeetingContract.MeetingUser> data = new ArrayList<>(users.size());
        for (String id : users) {
            data.add(adapt(id));
        }
        callback.onSuccess(data);
    }

    @Override
    public void controlMeeting(int cmd) {
        switch (cmd) {
            case KhMeetingContract.cmd_leave_meeting:
                KhSdkManager.getInstance().getSdk().leaveMeeting();
                break;

            case KhMeetingContract.cmd_finish_meeting:
                KhSdkManager.getInstance().getSdk().concludeMeeting();
                break;
        }
    }

    private KhMeetingContract.MeetingUser adapt(String id) {
        KhMeetingContract.MeetingUser user = new KhMeetingContract.MeetingUser();
        user.id = id;
        YSXInMeetingService service = YSXSdk.getInstance().getInMeetingService();
        long uid = Long.parseLong(id);
        user.name = service.getUserInfoById(uid).getUserName();
        user.isHost = service.isMeetingHost();
        return user;
    }
}
