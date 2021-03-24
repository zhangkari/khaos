package com.class100.khaos.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.class100.khaos.R;
import com.class100.khaos.meeting.vb.MeetingMenuBinder;
import com.class100.khaos.meeting.vm.MeetingMenuItem;

import org.karic.smartadapter.SmartAdapter;

import java.util.Arrays;
import java.util.List;

public class MeetingMenuView extends FrameLayout {
    private CheckBox checkBox;
    private SmartAdapter smartAdapter;
    private RecyclerView recyclerView;
    private OnMenuItemClickListener listener;

    public MeetingMenuView(@NonNull Context context) {
        this(context, null);
    }

    public MeetingMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeetingMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MeetingMenuView setMenuItem(MeetingMenuItem... item) {
        return setMenuItem(Arrays.asList(item));
    }

    public MeetingMenuView setMenuItem(List<MeetingMenuItem> list) {
        smartAdapter.setData(list);
        return this;
    }

    public MeetingMenuView setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    private void init() {
        inflate(getContext(), R.layout.kh_view_meeting_menu, this);
        initView();
        setListener();
    }

    private void initView() {
        checkBox = findViewById(R.id.btn_expand);
        recyclerView = findViewById(R.id.rv_menus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        smartAdapter = new SmartAdapter();
        MeetingMenuBinder viewBinder = new MeetingMenuBinder();
        viewBinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    MeetingMenuItem item = (MeetingMenuItem) v.getTag();
                    listener.onItemClick(item);
                }
            }
        });
        smartAdapter.register(MeetingMenuItem.class, viewBinder);
        recyclerView.setAdapter(smartAdapter);
    }

    private void setListener() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                animateMenuPanel(isChecked);
            }
        });
    }

    private void animateMenuPanel(boolean show) {
        recyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public interface OnMenuItemClickListener {
        void onItemClick(MeetingMenuItem item);
    }
}
