package com.class100.khaos.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.class100.khaos.R;

public class MeetingTitleView extends FrameLayout {
    public MeetingTitleView(@NonNull Context context) {
        this(context, null);
    }

    public MeetingTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeetingTitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.kh_view_meeting_title, this);
    }
}
