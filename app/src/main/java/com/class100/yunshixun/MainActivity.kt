package com.class100.yunshixun

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.class100.atropos.generic.AtLog
import com.class100.hades.http.HaApiCallback
import com.class100.hades.http.HaApiResponse
import com.class100.hades.http.HaHttpClient
import com.class100.khaos.KhAbsSdk
import com.class100.khaos.KhSdkManager
import com.class100.khaos.ysx.internal.request.ReqKhSdkToken
import com.class100.khaos.ysx.internal.response.RespKhSdkToken

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
        findViewById<View>(R.id.btn_get_token).setOnClickListener {

        }
    }

    private fun initKhAbilitySdk() {
        KhSdkManager.getInstance().load(object : KhAbsSdk.OnSdkInitializedListener {
            override fun onInitialized(sdk: KhAbsSdk) {
                AtLog.d(TAG, "initSDK ok", "++++++")
            }

            override fun onError() {
                AtLog.d(TAG, "initSDK error", "++++++");
            }
        })
    }
}