package com.class100.khaos.meeting.vm;

import androidx.annotation.DrawableRes;

public class MeetingMenuItem {
    public int id;
    public String name;
    public int icon;

    public MeetingMenuItem(int id, String name, @DrawableRes int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }


}
