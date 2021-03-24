package com.class100.khaos.meeting;

import java.util.List;

public interface KhMeetingContract {
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

        void showAttenders(List<MeetingUser> attenders);
    }

    interface IMeetingPresenter {
        void requestAttenders();

        void notifyUserJoin(List<String> users);

        void notifyUserLeave(List<String> users);

        void detach();
    }

    interface IMeetingModel {
        void queryUsers(ResultCallback<List<MeetingUser>> callback);
    }
}
