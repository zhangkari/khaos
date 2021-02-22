package com.class100.khaos;

import androidx.annotation.MainThread;
import androidx.annotation.StringDef;

import com.class100.atropos.AtAbility;
import com.class100.khaos.ysx.YsxSdkPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

public class KhSdkManager implements AtAbility {
    public static final String SDK_YSX = "sdk_ysx";

    @StringDef({SDK_YSX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface KhSdkChannel {

    }

    private static volatile KhSdkManager _instance;
    private static KhAbsSdk currentSdk;
    private static final Map<String, KhAbsSdk> cachedSdk = new HashMap<>(4);

    @MainThread
    public static void registerYsxSdk(String mobile) {
        registerSdk(SDK_YSX, new YsxSdkPlugin(mobile));
    }

    @MainThread
    public static void registerSdk(String key, KhAbsSdk sdk) {
        cachedSdk.put(key, sdk);
        if (currentSdk == null) {
            currentSdk = sdk;
        }
    }

    @MainThread
    public static void switchChannel(@KhSdkChannel String channel) {
        KhAbsSdk sdk = cachedSdk.get(channel);
        if (sdk != null) {
            if (currentSdk != null) {
                currentSdk.disable();
                currentSdk.unload();
            }
            currentSdk = sdk;
        }
    }

    public static KhSdkManager getInstance() {
        if (_instance == null) {
            synchronized (KhSdkManager.class) {
                if (_instance == null) {
                    _instance = new KhSdkManager();
                }
            }
        }
        return _instance;
    }

    @Override
    public void load() {
        if (currentSdk != null) {
            currentSdk.load();
        }
    }

    public void load(KhAbsSdk.OnSdkInitializeListener listener) {
        if (currentSdk != null) {
            currentSdk.initializeListener = listener;
            load();
        }
    }

    public KhSdkAbility getSdk() {
        return currentSdk;
    }

    @Override
    public void enable() {
        if (currentSdk != null) {
            currentSdk.enable();
        }
    }

    @Override
    public void disable() {
        if (currentSdk != null) {
            currentSdk.disable();
        }
    }

    @Override
    public void unload() {
        if (currentSdk != null) {
            currentSdk.initializeListener = null;
            currentSdk.unload();
        }
    }
}
