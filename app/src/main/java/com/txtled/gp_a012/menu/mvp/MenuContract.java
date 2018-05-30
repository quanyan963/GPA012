package com.txtled.gp_a012.menu.mvp;

import android.app.Activity;
import android.content.Context;

import com.txtled.gp_a012.base.BasePresenter;
import com.txtled.gp_a012.base.BaseView;
import com.txtled.gp_a012.bean.Song;

import java.util.List;


/**
 * Created by KomoriWu
 * on 2017/9/18.
 */

public interface MenuContract {
    interface View extends BaseView {
        void toMainView(int flags);
        void showDrawerOpen();
        void showScanSnackBar();
        void hideScanSnackBar();
        void playMusic();
        void showPopMusicList(List<Song> musicInfoList);
        void onShowError();
        void onShowOpenBleDialog();
        void onShowOpenGPSDialog();
        void scanFailure();
        void connFailure();
        void connected();
        void showOpenDeviceDialog();
        void startAnim();
        void setBleStatue(boolean isConn);
        void showProgress();
        void hideProgress();
    }

    interface Presenter extends BasePresenter<View> {
        void setBleListener();
        void checkPermission(Activity activity);
        void checkBlePermission(boolean isSpecified,Activity activity);
        void switchNavView(int id);
        void onClick(int id,boolean isFinish);
        List<Song> getSongList();
        void unConn();
        int getPlayPosition();
        void getBleConnectedStatue(Context context);
        void volumeChange(int volume, Context context, boolean isConn);
        int getVolume();
        void checkChange(int checkId);
        void toMusic();
        void changePower(int num);
        void allData(String allData);
        void changeSwitch(int statue);
    }
}
