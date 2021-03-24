package com.class100.khaos;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class LeaveMeetingDialog extends DialogFragment {
    private OnDecideListener listener;

    public LeaveMeetingDialog setOnDecideListener(OnDecideListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        return inflater.inflate(R.layout.kh_dialog_leave_meeting, parent, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        resetStyle();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setListener();
    }

    private void setListener() {
        View view = getView();
        if (view == null) {
            return;
        }
        view.findViewById(R.id.btn_leave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLeave();
                }
                dismiss();
            }
        });

        view.findViewById(R.id.btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFinish();
                }
                dismiss();
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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

    public interface OnDecideListener {
        void onLeave();

        void onFinish();
    }
}