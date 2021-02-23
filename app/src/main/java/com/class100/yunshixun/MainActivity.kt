package com.class100.yunshixun

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.class100.atropos.generic.AtLog
import com.class100.atropos.generic.AtTexts
import com.class100.khaos.KhAbsSdk
import com.class100.khaos.KhSdkListener
import com.class100.khaos.KhSdkManager
import com.class100.khaos.req.KhReqCreateScheduled
import com.class100.khaos.req.KhReqGetMeetings
import com.class100.khaos.req.KhReqJoinMeeting
import com.class100.khaos.req.KhReqStartMeeting
import com.class100.khaos.resp.KhRespCreateScheduled
import com.class100.khaos.resp.KhRespGetMeetings
import com.class100.khaos.ysx.YsxSdkHelper
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
        setListener()
    }

    private fun setListener() {
        findViewById<SwitchCompat>(R.id.check_scheduled_meeting).setOnCheckedChangeListener { _, checked ->
            findViewById<View>(R.id.layout_user).visibility =
                if (checked) View.VISIBLE else View.GONE
        }

        findViewById<View>(R.id.btn_sign_in).setOnClickListener {
            findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
            initKhAbilitySdk()
        }

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
            config.participants = listOf(et_user1.text.toString(), et_user2.text.toString());
            config.category = if (check_scheduled_meeting.isChecked) 1 else 0
            KhSdkManager.getInstance().sdk.startMeeting(this, config)
        }

        findViewById<View>(R.id.btn_create_schedule).setOnClickListener {
            val config = KhReqCreateScheduled()
            config.No = et_meeting_no.text.toString()
            config.agenda = "Agenda-Welcome";
            config.topic = et_meeting_topic.text.toString()
            config.duration = 8 * 60 // 8 * 60 min
            config.startTime = System.currentTimeMillis() + 2 * 60 * 1000;
            config.autoConnectAudio = true
            config.autoConnectVideo = true
            config.token = YsxSdkHelper.getToken()
            config.participants = listOf(et_user1.text.toString(), et_user2.text.toString());
            KhSdkManager.getInstance().sdk.createScheduledMeeting(config, object :
                KhSdkListener<KhRespCreateScheduled> {
                override fun onSuccess(result: KhRespCreateScheduled?) {
                    AtLog.d(TAG, "createScheduled", " success: meetingNo: ${result?.meetingNo}")
                }

                override fun onError(code: Int, message: String?) {
                    AtLog.d(TAG, "createScheduled", "failed: message:$message")
                }
            })
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

        findViewById<View>(R.id.btn_query_meetings).setOnClickListener {
            val config = KhReqGetMeetings()
            config.status = 1;
            config.token = YsxSdkHelper.getToken()
            KhSdkManager.getInstance().sdk.getMeetings(
                config,
                object : KhSdkListener<KhRespGetMeetings> {
                    override fun onSuccess(result: KhRespGetMeetings) {
                        AtLog.d(
                            TAG,
                            "queryMeetings",
                            " success: meetings: ${result.meetings?.size}"
                        )
                    }

                    override fun onError(code: Int, message: String?) {
                        AtLog.d(TAG, "queryMeetings", "failed: message:$message")
                    }
                })
        }
    }

    private fun initKhAbilitySdk() {
        val mobile: String = findViewById<EditText>(R.id.et_mobile_phone).text.toString()
        if (AtTexts.isEmpty(mobile)) {
            Toast.makeText(this, "Please input mobile phone", Toast.LENGTH_SHORT).show()
            return
        }

        KhSdkManager.registerYsxSdk(mobile)
        KhSdkManager.getInstance().load(object : KhAbsSdk.OnSdkInitializeListener {
            override fun onInitialized(sdk: KhAbsSdk) {
                AtLog.d(TAG, "initSDK ok", "++++++")
                progressBar.visibility = View.GONE
                findViewById<View>(R.id.layout_sign_in).visibility = View.GONE
                findViewById<View>(R.id.layout_signed_in).visibility = View.VISIBLE
            }

            override fun onError() {
                AtLog.d(TAG, "initSDK error", "++++++");
            }
        })
    }
}