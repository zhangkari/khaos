package com.class100.khaos;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class KhMeetingHostAskDialog extends DialogFragment {
    private final int type; // 0 audio, 1 video
    private final Runnable confirmTask;

    public KhMeetingHostAskDialog(int type, Runnable task) {
        this.type = type;
        confirmTask = task;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        return inflater.inflate(getLayoutId(), parent, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        resetStyle();
    }

    private int getLayoutId() {
        return R.layout.kh_dlg_meeting_host_ask;
    }

    private void init() {
        View view = getView();
        if (view == null) {
            return;
        }

        TextView tvAsk = view.findViewById(R.id.tv_ask);
        tvAsk.setText(type == 0 ? R.string.kh_host_ask_cancel_mute : R.string.kh_host_ask_start_video);

        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            if (confirmTask != null) {
                confirmTask.run();
            }
            dismiss();
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
    }

    private void resetStyle() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setBackgroundDrawableResource(R.color.kh_bg_dialog);
        WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
        attrs.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.3f);
        window.setAttributes(attrs);
        dialog.setCancelable(false);
    }
}
