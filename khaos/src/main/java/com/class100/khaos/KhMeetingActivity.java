package com.class100.khaos;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.class100.atropos.generic.AtLog;
import com.class100.khaos.meeting.KhMeetingContract;
import com.class100.khaos.meeting.KhMeetingModel;
import com.class100.khaos.meeting.KhMeetingPresenter;
import com.class100.khaos.meeting.MeetingLayoutManager;
import com.class100.khaos.meeting.vb.MeetingUserBinder;
import com.class100.khaos.meeting.vm.MeetingMenuItem;
import com.class100.khaos.widgets.MeetingMenuView;
import com.class100.khaos.widgets.MeetingTitleView;

import org.jetbrains.annotations.NotNull;
import org.karic.smartadapter.SmartAdapter;

import java.util.List;

public class KhMeetingActivity extends AppCompatActivity implements KhMeetingContract.IMeetingView {
    private static final String TAG = "KhMeetingActivity";

    private View progressView;
    private MeetingMenuView menuView;
    private MeetingTitleView titleView;
    private SmartAdapter smartAdapter;
    private MeetingLayoutManager layoutManager;
    private KhSdkAbility.OnMeetingStatusChangedListener meetingStatusListener;
    private KhMeetingContract.IMeetingPresenter presenter;

    @Override
    public void onCreate(Bundle bundle) {
        AtLog.d(TAG, "onCreate", "");
        super.onCreate(bundle);
        setContentView(R.layout.kh_activity_meeting);
        init();
    }

    private void init() {
        presenter = new KhMeetingPresenter(this, new KhMeetingModel());
        initView();
        initListener();
        presenter.loadMeetingMenu();
        presenter.requestAttenders();
    }

    private void initView() {
        titleView = findViewById(R.id.meetingTitle);
        RecyclerView recyclerView = findViewById(R.id.rv_users);
        progressView = findViewById(R.id.layout_progress);

        menuView = findViewById(R.id.meeting_menu);

        smartAdapter = new SmartAdapter();
        smartAdapter.register(KhMeetingContract.MeetingUser.class, new MeetingUserBinder());
        layoutManager = new MeetingLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(smartAdapter);

        checkVideoRotation(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        AtLog.d(TAG, "onNewIntent", "");
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        checkVideoRotation(this);
    }

    private void initListener() {
        meetingStatusListener = new KhSdkAbility.OnMeetingStatusChangedListener() {
            @Override
            public void onMeetingStatusChanged(KhSdkAbility.KhMeetingStatus status, int errorCode) {
                Log.d(TAG, "onMeetingStatusChanged:" + status);
                if (status == KhSdkAbility.KhMeetingStatus.MEETING_STATUS_INMEETING) {
                    presenter.loadMeetingTitle();
                }
            }
        };
        KhSdkManager.getInstance().getSdk().addMeetingListener(meetingStatusListener);

        KhSdkManager.getInstance().getSdk().setMeetingUserChangedListener(new KhSdkAbility.OnMeetingUserChangedListener() {
            @Override
            public void onMeetingUserJoin(List<String> list) {
                Log.d(TAG, "onMeetingUserJoin: count = " + list.size());
                presenter.notifyUserJoin(list);
            }

            @Override
            public void onMeetingUserLeave(List<String> list) {
                Log.d(TAG, "onMeetingUserLeave: count = " + list.size());
                presenter.notifyUserLeave(list);
            }
        });

        KhSdkManager.getInstance().getSdk().setUserVideoStatusChangedListener(new KhSdkAbility.OnUserVideoStatusChangedListener() {
            @Override
            public void onUserVideoStatusChanged(String userId) {
                Log.d(TAG, "onUserVideoStatusChanged:" + "userId = " + userId);
                updateVideoStatus();
            }
        });

        KhSdkManager.getInstance().getSdk().setUserAudioStatusChangedListener(new KhSdkAbility.OnUserAudioStatusChangedListener() {
            @Override
            public void onUserAudioStatusChanged(String userId) {
                Log.d(TAG, "onUserAudioStatusChanged:" + "userId = " + userId);
                if (KhSdkManager.getInstance().getSdk().isMyself(userId)){
                    updateAudioStatus();
                }
            }

            @Override
            public void onUserAudioTypeChanged(String userId) {
                Log.d(TAG, "onUserAudioTypeChanged:" + "userId = " + userId);
                if (KhSdkManager.getInstance().getSdk().isMyself(userId)){
                    updateAudioStatus();
                }
            }

            @Override
            public void onMyAudioSourceTypeChanged(int type) {
                Log.d(TAG, "onMyAudioSourceTypeChanged:" + "type = " + type);
            }
        });
    }

    private void updateVideoStatus() {
        KhSdkAbility sdkAbility = KhSdkManager.getInstance().getSdk();
        if (sdkAbility.isMyVideoMuted()) {
            menuView.updateVideo(R.drawable.kh_ic_video_off);
        } else {
            menuView.updateVideo(R.drawable.kh_ic_video_on);
        }
    }

    private void updateAudioStatus(){
        KhSdkAbility sdkAbility = KhSdkManager.getInstance().getSdk();
        if (sdkAbility.isAudioConnected()) {
            if (sdkAbility.isMyAudioMuted()) {
                menuView.updateAudio(R.drawable.kh_ic_audio_off);
            } else {
                menuView.updateAudio(R.drawable.kh_ic_audio_on);
            }
        } else {
            menuView.updateAudio(R.drawable.kh_ic_no_audio);
        }
    }

    @Override
    public void onDestroy() {
        AtLog.d(TAG, "onDestroy", "");
        KhSdkManager.getInstance().getSdk().leaveMeeting();
        KhSdkManager.getInstance().getSdk().removeMeetingListener(meetingStatusListener);
        presenter.detach();
        super.onDestroy();
    }

    private void checkVideoRotation(Context context) {
        Display display = context.getDisplay();
        int rotation = display.getRotation();
        KhSdkManager.getInstance().getSdk().rotateLocalVideo(rotation);
    }

    @Override
    public void showLoading() {
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressView.setVisibility(View.GONE);
    }

    @Override
    public void showError(int code, String message) {
        Toast.makeText(this, code + "\n" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMenu(List<MeetingMenuItem> menus) {
        menuView.setMenuItem(menus);
        menuView.setOnMenuItemClickListener(new MeetingMenuView.OnMenuItemClickListener() {
            @Override
            public void onItemClick(MeetingMenuItem item) {
                presenter.performMenuClick(item.id);
            }
        });
    }

    @Override
    public void showAttenders(List<KhMeetingContract.MeetingUser> users) {
        AtLog.d(TAG, "showAttenders", "users:" + users.size());
        layoutManager.setItemCount(users.size());
        smartAdapter.refreshData(users, false);
    }

    @Override
    public void showMeetingTitle(String host, String meetingNo, String duration) {
        titleView.setMeetingHost(host);
        titleView.setMeetingNo(meetingNo);
        titleView.setMeetingElapsed(duration);
    }

    @Override
    public void showLeaveDialog() {
        new LeaveMeetingDialog().setOnDecideListener(new LeaveMeetingDialog.OnDecideListener() {
            @Override
            public void onLeave() {
                presenter.executeControlMeeting(KhMeetingContract.cmd_leave_meeting);
                finish();
            }

            @Override
            public void onFinish() {
                presenter.executeControlMeeting(KhMeetingContract.cmd_finish_meeting);
                finish();
            }
        }).show(getSupportFragmentManager(), TAG);
    }

    @Override
    public void onBackPressed() {
        showLeaveDialog();
    }
}
