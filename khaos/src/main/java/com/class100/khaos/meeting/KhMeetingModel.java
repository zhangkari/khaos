package com.class100.khaos.meeting;

import com.chinamobile.ysx.YSXInMeetingService;
import com.chinamobile.ysx.YSXSdk;
import com.class100.atropos.generic.AtCollections;
import com.class100.khaos.KhSdkManager;

import java.util.ArrayList;
import java.util.List;

public class KhMeetingModel implements KhMeetingContract.IMeetingModel {
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
