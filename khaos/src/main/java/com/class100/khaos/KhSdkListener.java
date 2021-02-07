package com.class100.khaos;

public interface KhSdkListener<T> {
    void onSuccess(T result);

    void onError(int code, String message);
}