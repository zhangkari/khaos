package com.class100.khaos;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class KhSdkConstants {
    public static final String SDK_YSX = "sdk_ysx";

    @StringDef({SDK_YSX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface KhSdkChannel {

    }

    public static class InitParameters {
        public final String appKey;
        public final String appSecret;

        public InitParameters(String appKey, String appSecret) {
            this.appKey = appKey;
            this.appSecret = appSecret;
        }
    }
}