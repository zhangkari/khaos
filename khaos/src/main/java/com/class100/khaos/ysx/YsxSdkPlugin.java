package com.class100.khaos.ysx;

import com.chinamobile.ysx.YSXError;
import com.chinamobile.ysx.YSXSdk;
import com.chinamobile.ysx.YSXSdkAuthenticationListener;
import com.chinamobile.ysx.YSXSdkInitializeListener;
import com.chinamobile.ysx.auther.LoginResult;
import com.chinamobile.ysx.auther.YSXLoginResultListener;
import com.class100.atropos.generic.AtLog;
import com.class100.atropos.generic.AtTexts;
import com.class100.hades.http.HaApiCallback;
import com.class100.hades.http.HaApiResponse;
import com.class100.hades.http.HaHttpClient;
import com.class100.khaos.KhAbsSdk;
import com.class100.khaos.ysx.internal.request.ReqKhSdkToken;
import com.class100.khaos.ysx.internal.response.RespKhSdkToken;

public class YsxSdkPlugin extends KhAbsSdk {
    private static final String TAG = "YsxSdkPlugin";

    // TODO Initialize int libAtNative
    private static final String APP_KEY = "Iratlr8ZCaVgyPJ5O8xcaNzSUYcEMFd9y1nm";
    private static final String APP_SECRET = "ft7jnlQj28pqasbbBqTlRdR1LdbzUaqabgIv";

    @Override
    public void load() {
        YSXSdk sdk = YSXSdk.getInstance();
        sdk.initSDK(env._app, env._app, APP_KEY, APP_SECRET, true, new YSXSdkInitializeListener() {
            @Override
            public void onYSXSdkInitializeResult(int errorCode, int internalErrorCode) {
                AtLog.d(TAG, "Init ysx sdk result", "errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);
                if (errorCode != YSXError.SUCCESS) {
                    AtLog.d(TAG, "Init ysxSDK. Error", "");
                    if (listener != null) {
                        listener.onError();
                    }
                    return;
                }

                AtLog.d(TAG, "Init ysxSDK successfully", "");
                loginSdk();
            }
        });

        sdk.addYsxAuthenticationListener(new YSXSdkAuthenticationListener() {
            @Override
            public void onYsxSDKLoginResult(long l) {
                if (l == 0) {
                    if (listener != null) {
                        listener.onInitialized(YsxSdkPlugin.this);
                    }
                } else {
                    if (listener != null) {
                        listener.onError();
                    }
                }
            }

            @Override
            public void onYsxSDKLogoutResult(long l) {

            }

            @Override
            public void onYsxIdentityExpired() {

            }
        });
    }

    private String getPreviousToken() {
        return TokenHelper.getToken();
    }

    private void requestSdkToken(final HaApiCallback<String> listener) {
        if (listener == null) {
            return;
        }
        String token = getPreviousToken();
        if (!AtTexts.isEmpty(token)) {
            listener.onSuccess(token);
        }
        HaHttpClient.getInstance()
                .enqueue(new ReqKhSdkToken("15110036167"), new HaApiCallback<HaApiResponse<RespKhSdkToken>>() {
                    @Override
                    public void onError(int code, String message) {
                        listener.onError(code, message);
                    }

                    @Override
                    public void onSuccess(HaApiResponse<RespKhSdkToken> resp) {
                        AtLog.d(TAG, "requestSdkToken", "token:" + resp.data.token);
                        // todo
                        TokenHelper.saveToken(resp.data.token, 0);
                        listener.onSuccess(resp.data.token);
                    }
                });
    }

    private void loginSdk() {
        requestSdkToken(new HaApiCallback<String>() {
            @Override
            public void onError(int code, String message) {
                AtLog.d(TAG, "requestSdkToken", "failed: code:" + code + ", message:" + message);
                if (listener != null) {
                    listener.onError();
                }
            }

            @Override
            public void onSuccess(String content) {
                loginSdkByToken(content);
            }
        });
    }

    private void loginSdkByToken(String token) {
        YSXSdk.getInstance().loginByToken(token, APP_KEY, APP_SECRET, new YSXLoginResultListener() {
            @Override
            public void onLoginResult(LoginResult result) {
                AtLog.d(TAG, "loginSdkByToken", "token:" + result.getSdktoken());
            }
        });
    }

    @Override
    public void unload() {

    }

    public void enable() {

    }

    public void disable() {

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
}
