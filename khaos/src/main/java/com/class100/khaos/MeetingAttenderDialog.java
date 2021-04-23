package com.class100.khaos;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.class100.khaos.meeting.KhAttenderPresenter;
import com.class100.khaos.meeting.KhMeetingContract;
import com.class100.khaos.meeting.KhMeetingModel;
import com.class100.khaos.meeting.vb.MenuAttenderBinder;

import org.jetbrains.annotations.NotNull;
import org.karic.smartadapter.SmartAdapter;

import java.util.List;

public class MeetingAttenderDialog extends DialogFragment implements KhMeetingContract.IAttenderView {
    private final SmartAdapter adapter = new SmartAdapter();
    private final KhMeetingContract.IAttenderPresenter presenter = new KhAttenderPresenter(this, new KhMeetingModel());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        return inflater.inflate(R.layout.kh_dialog_meeting_attender, parent, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        resetStyle();
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        init();
        presenter.requestAttenders();
    }

    private void init() {
        MenuAttenderBinder binder = new MenuAttenderBinder();
        binder.setOnCheckedChangedListener((CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) -> {
            KhMeetingContract.MeetingUser user = (KhMeetingContract.MeetingUser) buttonView.getTag();
            if (buttonView.getId() == R.id.cb_video) {
                if (isChecked) {
                    presenter.executeControlMeeting(KhMeetingContract.cmd_enable_video, user.id);
                } else {
                    presenter.executeControlMeeting(KhMeetingContract.cmd_disable_video, user.id);
                }
            } else if (buttonView.getId() == R.id.cb_audio) {
                if (isChecked) {
                    presenter.executeControlMeeting(KhMeetingContract.cmd_enable_audio, user.id);
                } else {
                    presenter.executeControlMeeting(KhMeetingContract.cmd_disable_audio, user.id);
                }
            }
        });
        adapter.register(KhMeetingContract.MeetingUser.class, binder);
        RecyclerView recyclerView = getView().findViewById(R.id.rv_attenders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
        window.setBackgroundDrawableResource(R.drawable.kh_bg_meeting_attender_dialog);
        WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();
        attrs.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.3f);
        window.setAttributes(attrs);
        dialog.setCancelable(true);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(int code, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAttenders(List<KhMeetingContract.MeetingUser> attenders) {
        adapter.setData(attenders);
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
    }
}