package com.class100.yunshixun

import android.app.Application
import com.class100.atropos.AtAbilityManager
import com.class100.hades.http.HaRequestDispatcher
import com.class100.khaos.KhSdkConstants
import com.class100.khaos.KhSdkManager

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AtAbilityManager.initialize(this, BuildConfig.DEBUG)
        HaRequestDispatcher.switchEnv(HaRequestDispatcher.ENV_QA)
        KhSdkManager.initSdk(KhSdkConstants.SDK_YSX, KhSdkConstants.InitParameters("2za6NoKQ7OXTNIkLVHbE7G62EyRY7tuMmgX1", "UEJbH1OnQZdVOALJqtjR2IYfpQxqWZKGzmhN"))
    }
}