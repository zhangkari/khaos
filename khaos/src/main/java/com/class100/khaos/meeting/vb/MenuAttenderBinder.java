package com.class100.khaos.meeting.vb;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.class100.khaos.R;
import com.class100.khaos.meeting.KhMeetingContract;

import org.karic.smartadapter.ViewBinder;

public class MenuAttenderBinder extends ViewBinder<KhMeetingContract.MeetingUser> {
    public MenuAttenderBinder() {
        super(R.layout.kh_vb_meeting_menu_attender);
    }

    @Override
    public void onCreate(ViewGroup parent) {
        super.onCreate(parent);
        view.setFocusable(true);
    }

    @Override
    protected void bindData(KhMeetingContract.MeetingUser data) {
        setText(R.id.tv_phone, data.id);
        setText(R.id.tv_name, data.name);
        CheckBox video = find(R.id.cb_video);
        video.setChecked(!data.disableVideo);
        CheckBox audio = find(R.id.cb_audio);
        audio.setChecked(!data.disableAudio);
        if (data.isHost) {
            video.setVisibility(View.GONE);
            audio.setVisibility(View.GONE);
        } else {
            video.setVisibility(View.VISIBLE);
            audio.setVisibility(View.VISIBLE);
        }

        video.setTag(data);
        video.setOnCheckedChangeListener(listenerInfo.onCheckedChangeListener);
        audio.setTag(data);
        audio.setOnCheckedChangeListener(listenerInfo.onCheckedChangeListener);
    }
}
