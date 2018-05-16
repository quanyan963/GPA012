package com.txtled.gp_a012.model;

import android.app.Activity;

import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.model.ble.BleHelper;
import com.txtled.gp_a012.model.db.DBHelper;
import com.txtled.gp_a012.model.operate.OperateHelper;
import com.txtled.gp_a012.model.prefs.PreferencesHelper;
import com.txtled.gp_a012.utils.Constants;
import com.txtled.gp_a012.widget.listener.BleConnListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class DataManagerModel implements DBHelper,BleHelper,OperateHelper,PreferencesHelper {
    private BleHelper mBleHelper;
    private DBHelper mDBDbHelper;
    private PreferencesHelper mPreferencesHelper;
    private OperateHelper mOperateHelper;

    public DataManagerModel(BleHelper mBleHelper, DBHelper mDBDbHelper, PreferencesHelper
            mPreferencesHelper, OperateHelper mOperateHelper) {
        this.mBleHelper = mBleHelper;
        this.mDBDbHelper = mDBDbHelper;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mOperateHelper = mOperateHelper;
    }

    @Override
    public void scanBle(Activity activity, boolean isSpecified, OnScanBleListener onScanBleListener) {
        mBleHelper.scanBle(activity,isSpecified,onScanBleListener);
    }

    @Override
    public void connBle(OnConnBleListener onConnBleListener) {
        mBleHelper.connBle(onConnBleListener);
    }

    @Override
    public void writeCommand(String command) {
        mBleHelper.writeCommand(command);
    }

    @Override
    public void notifyBle() {
        mBleHelper.notifyBle();
    }

    @Override
    public void isBleConnected(BleConnListener bleListener) {
        mBleHelper.isBleConnected(bleListener);
    }

    @Override
    public void readCommand(OnReadListener readListener) {
        mBleHelper.readCommand(readListener);
    }

    @Override
    public void unRegisterConn() {
        mBleHelper.unRegisterConn();
    }

    @Override
    public void requestPermissions(Activity activity, String[] permissions, OnPermissionsListener permissionsListener) {
        mOperateHelper.requestPermissions(activity,permissions,permissionsListener);
    }

    @Override
    public Flowable<ArrayList<Song>> scanMusic(Activity activity) {
        return mOperateHelper.scanMusic(activity);
    }

    @Override
    public void insertMusic(Song song) {
        mDBDbHelper.insertMusic(song);
    }

    @Override
    public void deleteAllSong() {
        mDBDbHelper.deleteAllSong();
    }

    @Override
    public List<Song> getMusicInfoList() {
        return mDBDbHelper.getMusicInfoList();
    }

    @Override
    public void insertFlame(Flame flame) {
        mDBDbHelper.insertFlame(flame);
    }

    @Override
    public void updateFlame(String type, int value) {
        mDBDbHelper.updateFlame(type,value);
    }

    @Override
    public Flame getFlame() {
        return mDBDbHelper.getFlame();
    }

    @Override
    public int getPlayPosition() {
        return mPreferencesHelper.getPlayPosition();
    }

    @Override
    public void setPlayPosition(int position) {
        mPreferencesHelper.setPlayPosition(position);
    }

    @Override
    public void setIsFirstApp(boolean b) {
        mPreferencesHelper.setIsFirstApp(b);
    }

    @Override
    public boolean getIsFirstApp() {
        return mPreferencesHelper.getIsFirstApp();
    }

    @Override
    public void setMainVolume(int progress) {
        mPreferencesHelper.setMainVolume(progress);
    }

    @Override
    public int getMainVolume() {
        return mPreferencesHelper.getMainVolume();
    }

    @Override
    public void setInitDialog(boolean b) {
        mPreferencesHelper.setInitDialog(b);
    }

    @Override
    public boolean getInitDialog() {
        return mPreferencesHelper.getInitDialog();
    }

    @Override
    public boolean getIsCycle() {
        return mPreferencesHelper.getIsCycle();
    }

    @Override
    public void setIsCycle(boolean b) {
        mPreferencesHelper.setIsCycle(b);
    }

    @Override
    public boolean getIsRandom() {
        return mPreferencesHelper.getIsRandom();
    }

    @Override
    public void setIsRandom(boolean b) {
        mPreferencesHelper.setIsRandom(b);
    }

    public void updatePlayState(int flag) {
//        switch (flag) {
//            case Constants.FLAG_SPA:
//                if (mPreferencesHelper.getRadioPlayState()) {
//                    mPreferencesHelper.setRadioPlayState(false);
//                }
//                break;
//            case Constants.FLAG_MUSIC:
//                if (mPreferencesHelper.getRadioPlayState()) {
//                    mPreferencesHelper.setRadioPlayState(false);
//                }
//                if (mDBDbHelper.getSpa().getState()) {
//                    mDBDbHelper.updateSpa(Spa.FLAG_STATE,"0");
//                }
//                break;
//            case Constants.FLAG_RADIO:
//                if (mDBDbHelper.getSpa().getState()) {
//                    mDBDbHelper.updateSpa(Spa.FLAG_STATE,"0");
//                }
//                break;
//        }
    }
}
