package com.class100.khaos;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chinamobile.ysx.YSXInMeetingService;
import com.chinamobile.ysx.YSXMobileRTCVideoUnitAspectMode;
import com.chinamobile.ysx.YSXMobileRTCVideoUnitRenderInfo;
import com.chinamobile.ysx.YSXMobileRTCVideoView;
import com.chinamobile.ysx.YSXSdk;
import com.class100.atropos.env.context.permission.AtPermission;
import com.class100.atropos.env.context.permission.PermissionCallback;
import com.class100.atropos.generic.AtLog;
import com.class100.khaos.widgets.MeetingTitleView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KhMeetingActivity extends AppCompatActivity {
    private static final String TAG = "KhMeetingActivity";

    private View progressView;
    private MeetingTitleView titleView;
    private YSXMobileRTCVideoView videoView;
    private KhSdkAbility.OnMeetingStatusChangedListener meetingStatusListener;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.kh_activity_meeting);
        checkPermission();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.onResume();
        checkVideoRotation(this);
        onMeetingReady();
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        checkVideoRotation(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        videoView.onPause();
    }

    private void initListener() {
        meetingStatusListener = new KhSdkAbility.OnMeetingStatusChangedListener() {
            @Override
            public void onMeetingStatusChanged(KhSdkAbility.KhMeetingStatus status, int errorCode) {
                Log.d(TAG, "onMeetingStatusChanged:" + status);
                if (status == KhSdkAbility.KhMeetingStatus.MEETING_STATUS_INMEETING) {
                    refreshUI();
                }
            }
        };
        KhSdkManager.getInstance().getSdk().addMeetingListener(meetingStatusListener);

        KhSdkManager.getInstance().getSdk().setMeetingUserChangedListener(new KhSdkAbility.OnMeetingUserChangedListener() {
            @Override
            public void onMeetingUserJoin(List<String> list) {
                Log.d(TAG, "onMeetingUserJoin: count = " + list.size());
                onMeetingReady();
            }

            @Override
            public void onMeetingUserLeave(List<String> list) {
                Log.d(TAG, "onMeetingUserLeave: count = " + list.size());
            }
        });

        KhSdkManager.getInstance().getSdk().setUserVideoStatusChangedListener(new KhSdkAbility.OnUserVideoStatusChangedListener() {
            @Override
            public void onUserVideoStatusChanged(String userId) {
                Log.d(TAG, "onUserVideoStatusChanged:" + "userId = " + userId);
            }
        });
    }

    private void checkPermission() {
        AtPermission.checkPermission(this, new PermissionCallback() {
            @Override
            public void onGrantedEntirely() {
                init();
            }

            @Override
            public void onPermissionDenied(List<String> list) {
                showPermissionHint();
                finish();
            }
        }, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
    }

    private void showPermissionHint() {
        Toast.makeText(this, "请开启相机和麦克风权限", Toast.LENGTH_SHORT).show();
    }

    private void init() {
        titleView = findViewById(R.id.meetingTitle);
        AtLog.d(TAG, "init", "meetingNo:" + KhSdkManager.getInstance().getSdk().getCurrentMeetingNo());
        AtLog.d(TAG, "init", "meetingId:" + KhSdkManager.getInstance().getSdk().getCurrentMeetingId());

        titleView.setMeetingNo(KhSdkManager.getInstance().getSdk().getCurrentMeetingNo());

        videoView = findViewById(R.id.videoView);
        progressView = findViewById(R.id.layout_progress);
    }

    public void onMeetingReady() {
        if (KhSdkManager.getInstance().getSdk().getMeetingStatus() == KhSdkAbility.KhMeetingStatus.MEETING_STATUS_INMEETING) {
            refreshUI();
        }
    }

    private void refreshUI() {
        progressView.setVisibility(View.GONE);
        YSXInMeetingService service = YSXSdk.getInstance().getInMeetingService();
        YSXMobileRTCVideoUnitRenderInfo renderInfo = new YSXMobileRTCVideoUnitRenderInfo(25, 25, 50, 50);
        renderInfo.is_border_visible = true;
        renderInfo.aspect_mode = YSXMobileRTCVideoUnitAspectMode.VIDEO_ASPECT_ORIGINAL;
        videoView.setZOrderMediaOverlay(true);
        videoView.getVideoViewManager().addAttendeeVideoUnit(service.getMyUserID(), renderInfo);
    }

    @Override
    public void onDestroy() {
        KhSdkManager.getInstance().getSdk().removeMeetingListener(meetingStatusListener);
        super.onDestroy();
    }

    private void checkVideoRotation(Context context) {
        Display display = ((WindowManager) context.getSystemService(Service.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        KhSdkManager.getInstance().getSdk().rotateLocalVideo(rotation);
    }
}
