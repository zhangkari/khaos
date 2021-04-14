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
        KhSdkManager.initSdk(
            KhSdkConstants.SDK_YSX,
            KhSdkConstants.InitParameters(
                false,
                "FOwvZJf5DjpizygZahOH9hgyciQmgOsXR5eC",
                "IQGrn2cvKiEdfPd44lOAof0fVUovoIZW0FMr"
            )
        )
    }
}