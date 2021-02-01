package com.class100.yunshixun

import android.app.Application
import com.class100.atropos.AtAbilityManager

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AtAbilityManager.initialize(this, BuildConfig.DEBUG)
    }
}