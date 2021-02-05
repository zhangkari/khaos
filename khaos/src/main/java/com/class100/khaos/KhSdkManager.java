package com.class100.khaos;

import androidx.annotation.StringDef;

import com.class100.atropos.AtAbility;
import com.class100.khaos.ysx.YsxSdkPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class KhSdkManager implements AtAbility {
    public static final String SDK_YSX = "sdk_ysx";

    @StringDef({SDK_YSX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface KhSdkChannel {

    }

    private static volatile KhSdkManager _instance;

    private KhAbsSdk sdk;

    private KhSdkManager() {
        sdk = new YsxSdkPlugin();
    }

    private KhSdkManager(@KhSdkChannel String channel) {
        if (SDK_YSX.equalsIgnoreCase(channel)) {
            sdk = new YsxSdkPlugin();
        }
    }

    public static void switchChannel(@KhSdkChannel String channel) {
        _instance.sdk.disable();
        _instance.sdk.unload();
        if (channel.equalsIgnoreCase(SDK_YSX)) {
            _instance = new KhSdkManager(channel);
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
        sdk.load();
    }

    public void load(KhAbsSdk.OnSdkInitializeListener listener) {
        sdk.initializeListener = listener;
        load();
    }

    public KhSdkAbility getSdk() {
        return sdk;
    }

    @Override
    public void enable() {
        sdk.enable();
    }

    @Override
    public void disable() {
        sdk.disable();
    }

    @Override
    public void unload() {
        sdk.initializeListener = null;
        sdk.unload();
    }
}
