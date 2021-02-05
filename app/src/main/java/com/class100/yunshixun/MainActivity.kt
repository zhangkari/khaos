package com.class100.yunshixun

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.class100.atropos.generic.AtLog
import com.class100.khaos.KhAbsSdk
import com.class100.khaos.KhSdkManager
import com.class100.khaos.KhStartMeetingConfig
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentLayout())
        init()
    }

    private fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    private fun init() {
        initKhAbilitySdk()
        setListener()
    }

    private fun setListener() {
        findViewById<View>(R.id.btn_start_meeting).setOnClickListener {
            val config = KhStartMeetingConfig()
            config.topic = "Demo"
            KhSdkManager.getInstance().sdk.startMeeting(config)
        }
    }

    private fun initKhAbilitySdk() {
        KhSdkManager.getInstance().load(object : KhAbsSdk.OnSdkInitializeListener {
            override fun onInitialized(sdk: KhAbsSdk) {
                AtLog.d(TAG, "initSDK ok", "++++++")
                progressBar.visibility = View.GONE
            }

            override fun onError() {
                AtLog.d(TAG, "initSDK error", "++++++");
            }
        })
    }
}