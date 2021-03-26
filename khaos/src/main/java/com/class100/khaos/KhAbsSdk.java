package com.class100.khaos;

import com.class100.atropos.env.context.AtContextAbility;

import java.util.HashSet;
import java.util.Set;

public abstract class KhAbsSdk extends AtContextAbility implements KhSdkAbility {
    protected OnSdkInitializeListener initializeListener;
    protected OnIMMessageListener imMessageListener;
    protected Set<OnMeetingStatusChangedListener> meetingListeners;
    protected boolean useIMSdk = false;

    public KhAbsSdk() {
        meetingListeners = new HashSet<>();
    }

    public void setInitializeListener(OnSdkInitializeListener listener) {
        this.initializeListener = listener;
    }

    public void setIMMessageListener(OnIMMessageListener listener) {
        this.imMessageListener = listener;
    }

    public void useIMSDk(boolean isUse){
        this.useIMSdk = isUse;
    }

    public void addMeetingListener(OnMeetingStatusChangedListener listener) {
        meetingListeners.add(listener);
    }

    public void removeMeetingListener(OnMeetingStatusChangedListener listener) {
        meetingListeners.remove(listener);
    }

}