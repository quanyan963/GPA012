package com.txtled.gp_a012.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;


import com.txtled.gp_a012.application.MyApplication;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class PreferencesHelperImpl implements PreferencesHelper {
    private static final String SP_NAME = "my_sp";
    private SharedPreferences mSharedPreferences;
    public static final String PLAY_POSITION = "play_position";
    public static final String IS_CYCLE = "is_cycle";
    public static final String IS_RANDOM = "is_random";
    public static final String RADIO_VALUE = "radio_value";
    public static final String RADIO_PLAY_STATE = "radio_play_state";
    public static final String IS_FIRST_APP = "is_first_app";
    public static final String FIRST_ALARM = "first_alarm";
    public static final String SECOND_ALARM = "second_alarm";
    public static final String MAIN_VOLUME = "main_volume";
    public static final String ALARM_POSITION = "alarm_position";
    public static final String INIT_DIALOG = "init_dialog";

    @Inject
    public PreferencesHelperImpl() {
        mSharedPreferences = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.
                MODE_PRIVATE);
    }

    @Override
    public int getPlayPosition() {
        return mSharedPreferences.getInt(PLAY_POSITION, 0);
    }

    @Override
    public void setPlayPosition(int position) {
        mSharedPreferences.edit().putInt(PLAY_POSITION, position).apply();
    }

    @Override
    public void setIsFirstApp(boolean b) {
        mSharedPreferences.edit().putBoolean(IS_FIRST_APP,b).apply();
    }

    @Override
    public boolean getIsFirstApp() {
        return mSharedPreferences.getBoolean(IS_FIRST_APP,true);
    }

    @Override
    public void setMainVolume(int progress) {
        mSharedPreferences.edit().putInt(MAIN_VOLUME,progress).apply();
    }

    @Override
    public int getMainVolume() {
        return mSharedPreferences.getInt(MAIN_VOLUME,0);
    }

    @Override
    public void setInitDialog(boolean b) {

    }

    @Override
    public boolean getInitDialog() {
        return false;
    }

    @Override
    public boolean getIsCycle() {
        return mSharedPreferences.getBoolean(IS_CYCLE,true);
    }

    @Override
    public void setIsCycle(boolean b) {
        mSharedPreferences.edit().putBoolean(IS_CYCLE,b).apply();
    }

    @Override
    public boolean getIsRandom() {
        return mSharedPreferences.getBoolean(IS_RANDOM,false);
    }

    @Override
    public void setIsRandom(boolean b) {
        mSharedPreferences.edit().putBoolean(IS_RANDOM,b).apply();
    }
}
