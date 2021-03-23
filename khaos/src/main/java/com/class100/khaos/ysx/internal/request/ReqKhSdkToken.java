package com.class100.khaos.ysx.internal.request;

import com.class100.hades.http.HaApiRequest;
import com.class100.khaos.KhPrefs;
import com.class100.khaos.ysx.internal.KhUrlRegister;

import java.util.Map;

public class ReqKhSdkToken extends HaApiRequest {
    @Override
    protected String getUrl() {
        return KhUrlRegister.URL_GET_TOKEN.replaceAll("[{](.*)[}]", "yunshixun");
    }

    @Override
    protected void buildHeaders(Map<String, String> map) {
        super.buildHeaders(map);
        map.put("Authorization", KhPrefs.getAppToken());
    }
}
