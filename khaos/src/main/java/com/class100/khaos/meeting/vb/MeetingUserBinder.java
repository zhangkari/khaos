package com.class100.khaos.meeting.vb;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.chinamobile.ysx.YSXMobileRTCVideoUnitAspectMode;
import com.chinamobile.ysx.YSXMobileRTCVideoView;
import com.class100.khaos.R;
import com.class100.khaos.meeting.KhMeetingContract;
import com.class100.khaos.meeting.MeetingLayoutManager;

import org.karic.smartadapter.ViewBinder;

import us.zoom.sdk.MobileRTCVideoUnitRenderInfo;

public class MeetingUserBinder extends ViewBinder<KhMeetingContract.MeetingUser> {
    public MeetingUserBinder() {
        super(R.layout.kh_vb_meeting_user);
    }

    @Override
    public void onCreate(ViewGroup parent) {
        super.onCreate(parent);
        resizeView(parent);
    }

    @Override
    protected void bindData(KhMeetingContract.MeetingUser data) {
        YSXMobileRTCVideoView videoView = find(R.id.videoView);
        long userId = Long.parseLong(data.id);
        videoView.getVideoViewManager().removeAttendeeVideoUnit(userId);
        MobileRTCVideoUnitRenderInfo info = new MobileRTCVideoUnitRenderInfo(0, 0, 100, 100);
        videoView.setZOrderMediaOverlay(true);
        info.is_border_visible = false;
        info.is_username_visible = false;
        info.aspect_mode = YSXMobileRTCVideoUnitAspectMode.VIDEO_ASPECT_ORIGINAL;
        videoView.getVideoViewManager().addAttendeeVideoUnit(userId, info);
        setText(R.id.tv_name, data.name);
    }

    private void resizeView(ViewGroup parent) {
        if (!(parent instanceof RecyclerView)) {
            return;
        }
        RecyclerView rv = (RecyclerView) parent;
        RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
        if (!(layoutManager instanceof MeetingLayoutManager)) {
            return;
        }
        MeetingLayoutManager manager = (MeetingLayoutManager) layoutManager;
        manager.resizeChild(view, rv.getHeight());
    }
}
