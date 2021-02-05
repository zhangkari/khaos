package com.class100.khaos;

public interface KhReplyInviteListener {
    void onSuccess(KhReplyInviteResult result);

    void onError(int code, String message);
}
