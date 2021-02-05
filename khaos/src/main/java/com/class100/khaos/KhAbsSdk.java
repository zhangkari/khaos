package com.class100.khaos;

import androidx.annotation.NonNull;

import com.class100.atropos.env.context.AtContextAbility;

import java.util.HashSet;
import java.util.Set;

public abstract class KhAbsSdk extends AtContextAbility implements KhSdkAbility {
    protected OnSdkInitializeListener initializeListener;
    protected Set<OnMeetingStatusChangedListener> meetingListeners;

    public KhAbsSdk() {
        meetingListeners = new HashSet<>();
    }

    public void setInitializeListener(OnSdkInitializeListener listener) {
        this.initializeListener = listener;
    }

    public void addMeetingListener(OnMeetingStatusChangedListener listener) {
        meetingListeners.add(listener);
    }

    public void removeMeetingListener(OnMeetingStatusChangedListener listener) {
        meetingListeners.remove(listener);
    }

    public interface OnSdkInitializeListener {
        void onInitialized(@NonNull KhAbsSdk sdk);

        void onError();
    }

    public interface OnMeetingStatusChangedListener {
        void onMeetingStatusChanged(KhMeetingStatus status, int errorCode);
    }

    public enum KhMeetingStatus {
        MEETING_STATUS_IDLE(0),
        MEETING_STATUS_CONNECTING(1),
        MEETING_STATUS_WAITINGFORHOST(2),
        MEETING_STATUS_INMEETING(3),
        MEETING_STATUS_DISCONNECTING(7),
        MEETING_STATUS_RECONNECTING(8),
        MEETING_STATUS_FAILED(6),
        MEETING_STATUS_IN_WAITING_ROOM(9),
        MEETING_STATUS_WEBINAR_PROMOTE(10),
        MEETING_STATUS_WEBINAR_DEPROMOTE(11),
        MEETING_STATUS_TRIAL_END(12),
        MEETING_STATUS_UNKNOWN(13);

        private int value = 0;

        KhMeetingStatus(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}