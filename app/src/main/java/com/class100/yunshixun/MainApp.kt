package com.class100.yunshixun

import android.app.Application
import com.class100.atropos.AtAbilityManager
import com.class100.hades.http.HaRequestDispatcher

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AtAbilityManager.initialize(this, BuildConfig.DEBUG)
        HaRequestDispatcher.switchEnv(HaRequestDispatcher.ENV_QA)
    }
}