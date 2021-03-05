package com.class100.khaos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class KhExitDialog extends DialogFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        return inflater.inflate(getLayoutId(), parent, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        init();
    }

    private int getLayoutId() {
        return R.layout.kh_dlg_exit;
    }

    private void init() {

    }
}
