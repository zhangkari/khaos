package com.class100.khaos;

public interface KhCreateScheduledListener {
    void onSuccess(KhReplyInviteResult result);

    void onError(int code, String message);
}
