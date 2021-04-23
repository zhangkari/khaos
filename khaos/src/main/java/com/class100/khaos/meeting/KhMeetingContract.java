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
    int cmd_enable_audio = 301;
    int cmd_disable_video = 400;
    int cmd_enable_video = 401;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({cmd_leave_meeting, cmd_finish_meeting, cmd_disable_audio, cmd_disable_video, cmd_enable_audio, cmd_enable_video})
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

    interface IAttenderView {
        void showLoading();

        void hideLoading();

        void showError(int code, String message);

        void showAttenders(List<MeetingUser> attenders);
    }

    interface IMeetingView extends IAttenderView {
        void showMenu(List<MeetingMenuItem> menus);

        void showMeetingTitle(String host, String meetingNo, String duration);

        void showLeaveDialog();

        void showAttenderDialog();
    }

    interface IAttenderPresenter {
        void requestAttenders();

        void executeControlMeeting(@MeetingCtrlCmd int cmd, String... argument);

        void detach();
    }

    interface IMeetingPresenter extends IAttenderPresenter {
        void loadMeetingMenu();

        void loadMeetingTitle();

        void performMenuClick(@MenuConstants.MeetingMenuId int id);

        void notifyUserJoin(List<String> users);

        void notifyUserLeave(List<String> users);

        void refreshMenuAudioStatus();

        void refreshMenuVideoStatus();
    }

    interface IAttenderModel {
        void queryUsers(ResultCallback<List<MeetingUser>> callback);

        void controlMeeting(@MeetingCtrlCmd int cmd, String... arguments);
    }

    interface IMeetingModel extends IAttenderModel {
        void loadMenuItems(ResultCallback<List<MeetingMenuItem>> callback);
    }
}