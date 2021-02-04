package com.class100.khaos;

import androidx.annotation.NonNull;

import com.class100.atropos.env.context.AtContextAbility;

public abstract class KhAbsSdk extends AtContextAbility implements KhSdkAbility {
    protected OnSdkInitializedListener listener;

    public void setInitializeListener(OnSdkInitializedListener listener) {
        this.listener = listener;
    }

    @Override
    public void createMeeting() {

    }

    @Override
    public void joinMeeting() {

    }

    @Override
    public void sendMeetingInvite() {

    }

    @Override
    public void load() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void unload() {

    }

    public interface OnSdkInitializedListener {
        void onInitialized(@NonNull KhAbsSdk sdk);

        void onError();
    }
}
