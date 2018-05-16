package com.txtled.gp_a012.utils;

import android.Manifest;
import android.media.AudioManager;

/**
 * Created by Mr.Quan on 2018/4/18.
 */

public class Constants {
    public static final String RB_ID = "rb_id";
    public static String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE };
    public static final int LIMIT_DURATION = 10 * 1000;
    public static final int STREAM_TYPE = AudioManager.STREAM_SYSTEM ;
    public static final String MUSIC_ALBUM_URI = "content://media/external/audio/albumart";
    public static final String WEEK = "week";
    public static final String ALL_WEEK = "1111111";
    public static final String NONE_WEEK = "0000000";
    public static final String WORK_DAY = "0111110";
    public static final String WEEKEND_DAY = "1000001";
    public static final int FIRST_ALARM = 0;
    public static final int SECOND_ALARM = 1;
    public static final int WEEK_SPA = 2;
    public static final int FLAG_SPA = 100;
    public static final int FLAG_MUSIC = 200;
    public static final int FLAG_RADIO = 300;
    public static final String TIMER_NEVER = "00min";

    public static final String LIGHT_STATUE = "1";
    public static final String LIGHT = "2";
    public static final String POWER = "3";
    public static final String SPEED = "4";
    public static final String TO_MUSIC = "5";
}
