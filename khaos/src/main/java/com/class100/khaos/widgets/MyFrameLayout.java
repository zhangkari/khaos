package com.class100.khaos.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.class100.khaos.R;

public class MyFrameLayout  extends FrameLayout {
    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (hasFocus()){
            View view = getFocusedChild();
            if (view==null){
                Log.e("MyFrameLayout","has focus null");
            }else {
                Log.e("MyFrameLayout","has focus = "+getFocusedChild().toString());
            }
        }else {
            Log.e("MyFrameLayout","no focus");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
            if (findViewById(R.id.meeting_menu).hasFocus()){
                findViewById(R.id.rv_users).requestFocus();
                return true;
            }
        }else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                RecyclerView recyclerView = findViewById(R.id.rv_users);
                View childFocused = recyclerView.getFocusedChild();
                if (childFocused!=null){
                    if (childFocused.focusSearch(FOCUS_LEFT)==null){
                        findViewById(R.id.meeting_menu).requestFocus();
                        return true;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
