package com.class100.khaos;

import com.class100.atropos.env.context.AtContextAbility;

import java.util.HashSet;
import java.util.Set;

public abstract class KhAbsSdk extends AtContextAbility implements KhSdkAbility {
    protected OnSdkInitializeListener initializeListener;
    protected Set<OnIMMessageListener> imMessageListeners;
    protected Set<OnMeetingStatusChangedListener> meetingListeners;
    protected boolean useIMSdk = false;

    public KhAbsSdk() {
        imMessageListeners = new HashSet<>();
        meetingListeners = new HashSet<>();
    }

    @Override
    public void setInitializeListener(OnSdkInitializeListener listener) {
        this.initializeListener = listener;
    }

    @Override
    public void addIMMessageListener(OnIMMessageListener listener) {
        imMessageListeners.add(listener);
    }

    @Override
    public void removeIMMessageListener(OnIMMessageListener listener) {
        imMessageListeners.remove(listener);
    }

    @Override
    public void useIMSDk(boolean isUse) {
        this.useIMSdk = isUse;
    }

    @Override
    public void addMeetingListener(OnMeetingStatusChangedListener listener) {
        meetingListeners.add(listener);
    }

    @Override
    public void removeMeetingListener(OnMeetingStatusChangedListener listener) {
        meetingListeners.remove(listener);
    }
}