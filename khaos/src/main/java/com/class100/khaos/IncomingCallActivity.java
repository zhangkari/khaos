package com.class100.khaos;import android.app.Activity;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.Intent;import android.content.res.AssetFileDescriptor;import android.media.AudioManager;import android.media.MediaPlayer;import android.os.Bundle;import android.os.Handler;import android.os.Vibrator;import android.view.View;import android.widget.Button;import android.widget.Toast;import com.chinamobile.ysx.YSXIMAction;import com.chinamobile.ysx.YSXJoinMeetingOptions;import com.chinamobile.ysx.YSXJoinMeetingParams;import com.chinamobile.ysx.YSXMeetingError;import com.chinamobile.ysx.YSXMeetingService;import com.chinamobile.ysx.YSXMeetingSettingsHelper;import com.chinamobile.ysx.YSXMeetingStatus;import com.chinamobile.ysx.YSXMessageListener;import com.chinamobile.ysx.YSXSdk;import com.chinamobile.ysx.YSXStartMeetingOptions;import com.chinamobile.ysx.YSXStartMeetingParams4NormalUser;import com.chinamobile.ysx.bean.Result;import com.chinamobile.ysx.iminterface.InviteMeeting;import com.chinamobile.ysx.responselistener.ResponseListenerCommon;import java.io.IOException;public class IncomingCallActivity extends Activity {    private MediaPlayer mediaPlayer;    private boolean playBeep;    private static final float BEEP_VOLUME = 50f;    private boolean vibrate;    private static final long VIBRATE_DURATION = 200L;    Handler mHandler = new Handler();    KhIMMessage inviteMeeting;    Button btnReject;    Button btnAnswer;    private View.OnClickListener mOnclickListener = new View.OnClickListener() {        @Override        public void onClick(View v) {            int id = v.getId();            if (id == R.id.btnReject) {                if (inviteMeeting.getAction() == YSXIMAction.ACTION_JOINMEETING) {                    YSXSdk.getInstance().getMeetingService().postInviteAnswer(inviteMeeting.getMeetingId(), "" + inviteMeeting.getMeetingNo(), 0, new ResponseListenerCommon<Result>() {                        @Override                        public void onFailure(Result result) {                        }                        @Override                        public void onResponse(Result o) {                            IncomingCallActivity.this.finish();                            if (mediaPlayer != null) {                                mediaPlayer.stop();                            }                        }                    });                } else {                    IncomingCallActivity.this.finish();                }            } else if (id == R.id.btnAnswer) {                if (mediaPlayer != null) {                    mediaPlayer.stop();                }                if (inviteMeeting.getAction() == YSXIMAction.ACTION_JOINMEETING)                    joinMeeting();                else                    startMeeting();            }        }    };    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.layout_incoming_call);        inviteMeeting = (KhIMMessage) getIntent().getSerializableExtra("khIMMessage");        btnReject = findViewById(R.id.btnReject);        btnAnswer = findViewById(R.id.btnAnswer);        btnReject.setOnClickListener(mOnclickListener);        btnAnswer.setOnClickListener(mOnclickListener);        initPlaySound();        playRingSoundAndVibrate();    }    /**     * When the beep has finished playing, rewind to queue up another one.     */    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {        public void onCompletion(MediaPlayer mediaPlayer) {            mediaPlayer.seekTo(0);        }    };    private void initPlaySound() {        playBeep = true;        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {            playBeep = false;        }        vibrate = true;        if (playBeep && mediaPlayer == null) {            // The volume on STREAM_SYSTEM is not adjustable, and users found it            // too loud,            // so we now play on the music stream.            setVolumeControlStream(AudioManager.STREAM_MUSIC);            mediaPlayer = new MediaPlayer();            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);            mediaPlayer.setOnCompletionListener(beepListener);            mediaPlayer.setLooping(true);            AssetFileDescriptor file = getResources().openRawResourceFd(                    R.raw.play);            try {                mediaPlayer.setDataSource(file.getFileDescriptor(),                        file.getStartOffset(), file.getLength());                file.close();                mediaPlayer.setVolume(audioService.getStreamVolume(AudioManager.STREAM_SYSTEM), audioService.getStreamVolume(AudioManager.STREAM_SYSTEM));                mediaPlayer.prepare();            } catch (IOException e) {                mediaPlayer = null;            }        }    }    private void playRingSoundAndVibrate() {        if (playBeep && mediaPlayer != null) {            mediaPlayer.start();        }        if (vibrate) {            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);            vibrator.vibrate(VIBRATE_DURATION);        }    }    public void joinMeeting() {        YSXSdk sdk = YSXSdk.getInstance();        YSXMeetingService meetingService = sdk.getMeetingService();        YSXJoinMeetingOptions opts = new YSXJoinMeetingOptions();        opts.no_meeting_end_message = true;        YSXMeetingSettingsHelper meetingSettingsHelper = sdk.getMeetingSettingsHelper();        meetingSettingsHelper.setMuteMyMicrophoneWhenJoinMeeting(inviteMeeting.isDisableAudio());        meetingSettingsHelper.setTurnOffMyVideoWhenJoinMeeting(inviteMeeting.isDisableVideo());        meetingSettingsHelper.setAutoConnectVoIPWhenJoinMeeting(true);        YSXJoinMeetingParams params = new YSXJoinMeetingParams();        params.meetingNo = "" + inviteMeeting.getMeetingNo();        params.displayName = "YSX_SDK";        params.password = inviteMeeting.getMeetingId();        //第四个参数 meetingType普通会议加入请传0  华为高清请传1        int rec = meetingService.joinMeetingWithParams(IncomingCallActivity.this,                inviteMeeting.getMeetingId(),                inviteMeeting.getMeetingType(), params, opts, new YSXMessageListener() {                    @Override                    public void onCallBack(final int i, final String s) {                        if (i == YSXMeetingError.MEETING_ERROR_MEETING_WILL_FINISH) {//                            Intent intent = new Intent(getApplicationContext(), DialogTranslucentActivity.class);//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//                            startActivity(intent);                            //试用账号即将退出                        }                        runOnUiThread(new Runnable() {                            @Override                            public void run() {                                Toast.makeText(IncomingCallActivity.this, i + ":" + s, Toast.LENGTH_SHORT).show();                            }                        });                /*if (i == YSXMeetingError.MEETING_ERROR_LIMIT) {                    //试用账号限制两次入会c                    runOnUiThread(new Runnable() {                        @Override                        public void run() {                            Toast.makeText(IncomingCallActivity.this, i + ":" + s, Toast.LENGTH_SHORT).show();                        }                    });                }                if (i == YSXMeetingError.ERROR_LIMIT_INMEETING_USER) {                    //人数超过上限                    runOnUiThread(new Runnable() {                        @Override                        public void run() {                            Toast.makeText(IncomingCallActivity.this, i + ":" + s, Toast.LENGTH_SHORT).show();                        }                    });                }*/                        IncomingCallActivity.this.finish();                    }                });    }    /**     * 开启这个会议     */    private void startMeeting() {        YSXSdk ysxSDK = YSXSdk.getInstance();        if (!ysxSDK.isInitialized()) {            Toast.makeText(this, "YsxSDK has not been initialized successfully", Toast.LENGTH_LONG).show();            return;        }        final YSXMeetingService meetingService = ysxSDK.getMeetingService();        if (meetingService.getMeetingStatus() != YSXMeetingStatus.MEETING_STATUS_IDLE) {            new AlertDialog.Builder(this)                    .setMessage("Do you want to leave current meeting and start another?")                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                        @Override                        public void onClick(DialogInterface dialog, int which) {                            meetingService.leaveCurrentMeeting(false);                        }                    })                    .setNegativeButton("No", new DialogInterface.OnClickListener() {                        @Override                        public void onClick(DialogInterface dialog, int which) {                        }                    })                    .show();            return;        }        YSXStartMeetingOptions opts = new YSXStartMeetingOptions();//		opts.no_meeting_end_message = true;//		opts.no_titlebar = true;//没有上方标题//		opts.no_bottom_toolbar = true;//没有会议界面下方的按钮//		opts.no_share = true;//没有会议界面的分享按钮//		opts.meeting_views_options = YSXMeetingViewsOptions.NO_BUTTON_SHARE;//可以设置会议界面隐藏哪个按钮，具体可以跟进去看参数//        opts.no_audio = false;//是否自动连接音频 true为不自动连接//        opts.no_video = false;//是否自动连接视频 true为不自动连接        YSXStartMeetingParams4NormalUser params = new YSXStartMeetingParams4NormalUser();        params.meetingNo = "" + inviteMeeting.getMeetingNo();        YSXMeetingSettingsHelper ysxMeetingSettingsHelper = ysxSDK.getMeetingSettingsHelper();        ysxMeetingSettingsHelper.setAutoConnectVoIPWhenJoinMeeting(true);        meetingService.startMeetingWithParams(this,                inviteMeeting.getMeetingId(),                inviteMeeting.getMeetingType(),                "" + inviteMeeting.getMeetingNo(),                params, opts, new YSXMessageListener() {                    @Override                    public void onCallBack(final int i, final String s) {                        if (i == YSXMeetingError.MEETING_ERROR_MEETING_WILL_FINISH) {//                            Intent intent = new Intent(getApplicationContext(), DialogTranslucentActivity.class);//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//                            startActivity(intent);                            //试用账号即将退出                        }                        if (i == YSXMeetingError.MEETING_ERROR_LIMIT) {                            //试用账号限制两次入会                            runOnUiThread(new Runnable() {                                @Override                                public void run() {                                    Toast.makeText(IncomingCallActivity.this, i + ":" + s, Toast.LENGTH_SHORT).show();                                }                            });                        }                        IncomingCallActivity.this.finish();                    }                });    }}