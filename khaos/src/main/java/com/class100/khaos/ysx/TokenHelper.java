package com.class100.khaos.ysx;

import com.class100.atropos.env.context.AtPrefs;
import com.class100.atropos.generic.AtTexts;

final class TokenHelper {
    private static final String KEY_TOKEN = "_ysx_key_token";
    private static final String KEY_EXPIRED = "_ysx_key_expired";

    public static void saveToken(String token, long expiredAt) {
        if (!AtTexts.isEmpty(token)) {
            AtPrefs.put(KEY_TOKEN, token);
            AtPrefs.put(KEY_EXPIRED, expiredAt);
        }
    }

    public static String getToken() {
        String token = AtPrefs.get(KEY_TOKEN, "");
        if (!AtTexts.isEmpty(token)) {
            if (System.currentTimeMillis() + 1000L /* 1s buffer */ < AtPrefs.get(KEY_EXPIRED, 0L)) {
                return token;
            }
        }
        return "";
    }
}
