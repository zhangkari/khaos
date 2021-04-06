package com.class100.khaos;

import androidx.annotation.MainThread;

import com.class100.atropos.AtAbility;
import com.class100.khaos.ysx.YsxSdkPlugin;

import java.util.HashMap;
import java.util.Map;

public class KhSdkManager implements AtAbility {
    public static final String SDK_YSX = "sdk_ysx";

    private static volatile KhSdkManager _instance;
    private static KhAbsSdk currentSdk = new YsxSdkPlugin();
    private static final Map<String, KhAbsSdk> cachedSdk = new HashMap<>(4);
    private static final Map<String, KhSdkConstants.InitParameters> initParams = new HashMap<>(4);

    @MainThread
    public static void switchChannel(@KhSdkConstants.KhSdkChannel String channel) {
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

    public static void initSdk(@KhSdkConstants.KhSdkChannel String channel, KhSdkConstants.InitParameters params) {
        initParams.put(channel, params);
    }

    @Override
    public void load() {
        if (currentSdk != null) {
            KhSdkConstants.InitParameters params = initParams.get(currentSdk.getName());
            if (params == null) {
                throw new RuntimeException("please call initSdk() first !");
            }
            currentSdk.init(params);
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
