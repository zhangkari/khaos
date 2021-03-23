package com.class100.yunshixun

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.class100.atropos.env.context.permission.AtPermission
import com.class100.atropos.env.context.permission.PermissionCallback
import com.class100.atropos.generic.AtLog
import com.class100.atropos.generic.AtTexts
import com.class100.hades.http.HaApiCallback
import com.class100.hades.http.HaApiResponse
import com.class100.hades.http.HaHttpClient
import com.class100.khaos.*
import com.class100.khaos.req.KhReqCreateScheduled
import com.class100.khaos.req.KhReqGetMeetings
import com.class100.khaos.req.KhReqJoinMeeting
import com.class100.khaos.req.KhReqStartMeeting
import com.class100.khaos.resp.KhRespCreateScheduled
import com.class100.khaos.resp.KhRespGetMeetings
import com.class100.khaos.ysx.YsxSdkHelper
import com.class100.yunshixun.req.ReqLogin
import com.class100.yunshixun.resp.RespLogin
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var step: Int = 0

    companion object {
        private const val TAG = "MainActivity"
    }

    private val meetingStatusListener = lazy {
        KhSdkAbility.OnMeetingStatusChangedListener { status, error ->
            Log.d(TAG, "meeting status changed:" + status.value() + "," + error);
            if (status == KhSdkAbility.KhMeetingStatus.MEETING_STATUS_CONNECTING) {
                showMeetingUi()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentLayout())
        checkPermission()
    }

    private fun checkPermission() {
        AtPermission.requestPermission(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
            object : PermissionCallback {
                override fun onGrantedEntirely() {
                    init()
                }

                override fun onPermissionDenied(list: List<String>) {
                    showPermissionHint(list)
                    AtPermission.requestPermission(
                        this@MainActivity,
                        list.toTypedArray(),
                        object : PermissionCallback {
                            override fun onGrantedEntirely() {
                                init()
                            }

                            override fun onPermissionDenied(p0: MutableList<String>) {
                                showPermissionHint(p0)
                            }
                        })
                }
            })
    }

    private fun showPermissionHint(permissions: List<String>) {
        val sb = StringBuilder()
        permissions.forEach {
            sb.append(it).append("\n")
        }
        Toast.makeText(this, "请开启${sb}权限", Toast.LENGTH_SHORT).show()
    }

    private fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    private fun init() {
        setListener()
    }

    private fun doLogin(phone: String?, password: String?) {
        if (AtTexts.isEmpty(phone) || AtTexts.isEmpty(password)) {
            findViewById<View>(R.id.progressBar).visibility = View.GONE
            Toast.makeText(this, "请输入手机号和密码", Toast.LENGTH_SHORT).show()
            return;
        }

        HaHttpClient.getInstance()
            .enqueue(
                ReqLogin(phone!!, password!!),
                object : HaApiCallback<HaApiResponse<RespLogin>> {
                    override fun onSuccess(p0: HaApiResponse<RespLogin>) {
                        findViewById<View>(R.id.progressBar).visibility = View.GONE
                        if (p0.code == 0) {
                            KhPrefs.saveAppToken(p0.data.token.token)
                            initKhAbilitySdk()
                        } else {
                            Toast.makeText(this@MainActivity, p0.msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(p0: Int, p1: String) {
                        findViewById<View>(R.id.progressBar).visibility = View.GONE
                        Toast.makeText(this@MainActivity, p1, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    @SuppressLint("SetTextI18n")
    private fun setListener() {
        findViewById<SwitchCompat>(R.id.check_scheduled_meeting).setOnCheckedChangeListener { _, checked ->
            findViewById<View>(R.id.layout_user).visibility =
                if (checked) View.VISIBLE else View.GONE
        }

        findViewById<View>(R.id.btn_sign_in).setOnClickListener {
            findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
            val phone = findViewById<EditText>(R.id.et_mobile_phone).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            doLogin(phone, password)
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

        findViewById<View>(R.id.btn_profile).setOnClickListener {
            val tvProfile = findViewById<TextView>(R.id.tv_profile)
            val profile = KhSdkManager.getInstance().sdk?.userProfile
            tvProfile.text = "name: ${profile?.name}, id:  ${profile?.userId}"
        }

        findViewById<View>(R.id.btn_sign_out).setOnClickListener {
            KhSdkManager.getInstance()?.sdk?.logout()
        }
    }

    private fun initKhAbilitySdk() {
        KhSdkManager.getInstance().load(object : KhSdkAbility.OnSdkInitializeListener {
            override fun onInitialized(sdk: KhAbsSdk) {
                step = 1
                AtLog.d(TAG, "initSDK ok", "++++++")
                progressBar.visibility = View.GONE
                findViewById<View>(R.id.layout_sign_in).visibility = View.GONE
                findViewById<View>(R.id.layout_signed_in).visibility = View.VISIBLE

                KhSdkManager.getInstance().sdk.addMeetingListener(meetingStatusListener.value)
            }

            override fun onError() {
                AtLog.d(TAG, "initSDK error", "++++++");
            }
        })
    }

    override fun onBackPressed() {
        if (step == 1) {
            findViewById<View>(R.id.layout_sign_in).visibility = View.VISIBLE
            findViewById<View>(R.id.layout_signed_in).visibility = View.GONE
            step = 0
        } else {
            super.onBackPressed()
        }
    }

    private fun showMeetingUi() {
        val intent = Intent(this, KhMeetingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        this.startActivity(intent)
    }

    override fun onDestroy() {
        if (KhSdkManager.getInstance().sdk != null) {
            KhSdkManager.getInstance().sdk.removeMeetingListener(meetingStatusListener.value)
        }
        super.onDestroy()
    }
}