package com.class100.khaos.meeting;

import androidx.annotation.IntDef;

import com.class100.khaos.meeting.vm.MeetingMenuItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public interface KhMeetingContract {
    int cmd_leave_meeting = 100;
    int cmd_finish_meeting = 200;
    int cmd_disable_audio = 300;
    int cmd_disable_video = 400;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({cmd_leave_meeting, cmd_finish_meeting, cmd_disable_audio, cmd_disable_video})
    @interface MeetingCtrlCmd {

    }

    interface ResultCallback<T> {
        void onSuccess(T t);

        void onError(int code, String message);
    }

    interface Adapter<S, T> {
        T adapt(S src);
    }

    class MeetingUser {
        public String id;
        public String name;
        public boolean isHost;
        public boolean disableVideo;
        public boolean disableAudio;
    }

    interface IMeetingView {
        void showLoading();

        void hideLoading();

        void showError(int code, String message);

        void showMenu(List<MeetingMenuItem> menus);

        void showAttenders(List<MeetingUser> attenders);

        void showMeetingTitle(String host, String meetingNo, String duration);

        void showLeaveDialog();
    }

    interface IMeetingPresenter {
        void loadMeetingMenu();

        void loadMeetingTitle();

        void performMenuClick(@MenuConstants.MeetingMenuId int id);

        void executeControlMeeting(@MeetingCtrlCmd int cmd);

        void requestAttenders();

        void notifyUserJoin(List<String> users);

        void notifyUserLeave(List<String> users);

        void detach();
    }

    interface IMeetingModel {
        void loadMenuItems(ResultCallback<List<MeetingMenuItem>> callback);

        void queryUsers(ResultCallback<List<MeetingUser>> callback);

        void controlMeeting(@MeetingCtrlCmd int cmd);
    }
}