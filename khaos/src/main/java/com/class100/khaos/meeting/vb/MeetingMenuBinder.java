package com.class100.khaos.meeting.vb;

import android.widget.TextView;

import com.class100.khaos.R;
import com.class100.khaos.meeting.vm.MeetingMenuItem;

import org.karic.smartadapter.ViewBinder;

public class MeetingMenuBinder extends ViewBinder<MeetingMenuItem> {
    public MeetingMenuBinder() {
        super(R.layout.kh_vb_menu_item);
    }

    @Override
    protected void bindData(MeetingMenuItem data) {
        TextView tv = find(R.id.tv_item);
        tv.setText(data.name);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, data.icon, 0, 0);

        view.setTag(data);
        view.setOnClickListener(listenerInfo.onClickListener);
    }
}
