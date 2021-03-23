package com.class100.khaos;

import com.class100.atropos.env.context.AtPrefs;

public final class KhPrefs {
    private static final String KEY_APP_TOKEN = "app_token";

    public static void saveAppToken(String token) {
        AtPrefs.put(KEY_APP_TOKEN, token);
    }

    public static String getAppToken() {
        return AtPrefs.get(KEY_APP_TOKEN, "");
    }
}
