package com.class100.khaos.meeting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

public class MeetingLayoutManager extends GridLayoutManager {
    private static final int DEFAULT_SPAN_COUNT = 3;
    private int mItemCount;

    public MeetingLayoutManager(Context context) {
        super(context, DEFAULT_SPAN_COUNT);
    }

    public MeetingLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setItemCount(int size) {
        mItemCount = size;
        if (size < DEFAULT_SPAN_COUNT) {
            if (size < 1) {
                size = 1;
            }
            setSpanCount(size);
        } else {
            setSpanCount(DEFAULT_SPAN_COUNT);
        }
    }

    public void resizeChild(@NonNull View view, int height) {
        int rows = Math.abs(mItemCount - 1) / DEFAULT_SPAN_COUNT + 1;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height / rows;
        view.setLayoutParams(params);
    }
}
