package com.txtled.gp_a012.model.operate;

import android.app.Activity;


import com.txtled.gp_a012.bean.Song;

import java.util.ArrayList;

import io.reactivex.Flowable;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public interface OperateHelper {
    void requestPermissions(Activity activity, String[] permissions, OnPermissionsListener permissionsListener);

    Flowable<ArrayList<Song>> scanMusic(Activity activity);

    interface OnPermissionsListener {
        void onSuccess(String name);

        void onFailure();

        void onAskAgain();
    }
}
