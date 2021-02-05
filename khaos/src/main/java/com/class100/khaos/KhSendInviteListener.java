package com.class100.khaos;

public interface KhSendInviteListener {
    void onSuccess(KhSendInviteResult result);

    void onError(int code, String message);
}
