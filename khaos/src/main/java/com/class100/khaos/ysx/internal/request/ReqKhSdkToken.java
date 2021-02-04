package com.class100.khaos.ysx.internal.request;

import com.class100.hades.http.HaApiRequest;
import com.class100.khaos.ysx.internal.KhUrlRegister;

public class ReqKhSdkToken extends HaApiRequest {
    private final String mobile;

    public ReqKhSdkToken(String mobile) {
        this.mobile = mobile;
    }

    @Override
    protected String getUrl() {
        return KhUrlRegister.URL_GET_TOKEN + "/" + mobile;
    }
}
