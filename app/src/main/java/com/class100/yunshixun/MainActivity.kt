package com.class100.yunshixun

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.class100.atropos.generic.AtLog
import com.class100.khaos.KhAbsSdk
import com.class100.khaos.req.KhReqJoinMeeting
import com.class100.khaos.KhSdkManager
import com.class100.khaos.req.KhReqStartMeeting
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
            val config = KhReqStartMeeting()
//            config.id = UUID.randomUUID().toString()
            config.No = et_meeting_no.text.toString()
            config.topic = et_meeting_topic.text.toString()
            config.autoConnectAudio = true
            config.autoConnectVideo = true
            config.autoConnectAudioJoined = true
            config.autoConnectVideoJoined = true
            config.autoMuteMicrophoneJoined = false
            config.participants = listOf("15928695284");
            config.category = if (check_scheduled_meeting.isChecked) 1 else 0
            KhSdkManager.getInstance().sdk.startMeeting(this, config)
        }

        findViewById<View>(R.id.btn_join_meeting).setOnClickListener {
            val config = KhReqJoinMeeting()
            config.No = et_meeting_no.text.toString()
            config.id = et_meeting_no.text.toString()
            config.autoConnectAudio = true
            config.autoConnectVideo = true
            config.displayName = "RedMi 6 Pro"
            KhSdkManager.getInstance().sdk.joinMeeting(this, config)
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