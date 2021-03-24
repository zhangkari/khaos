package com.class100.khaos.meeting;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class MenuConstants {
    public static final int menu_audio = 100;
    public static final int menu_camera = 200;
    public static final int menu_chat = 300;
    public static final int menu_attender = 400;
    public static final int menu_placement = 500;
    public static final int menu_exit = 600;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({menu_audio, menu_camera, menu_chat,
            menu_attender, menu_placement, menu_exit})
    @interface MeetingMenuId {

    }
}
