package com.txtled.gp_a012.model.ble;

import android.app.Activity;

import com.txtled.gp_a012.widget.listener.BleConnListener;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public interface BleHelper {
    void scanBle(Activity activity, boolean isSpecified, OnScanBleListener onScanBleListener, OnConnBleListener onConnBleListener);

    void connBle(OnConnBleListener onConnBleListener);

    void writeCommand(String command);

    void notifyBle(OnReadListener readListener);

    void isBleConnected(BleConnListener bleListener);

    void readCommand(OnReadListener readListener);

    void unRegisterConn();

    interface OnScanBleListener {
        void onStart();

        void onSuccess();

        void onScanFailure();

        void onDisOpenDevice();

        void onDisOpenBle();

        void onDisOpenGPS();

        void onDisSupported();
    }

    interface OnConnBleListener {
        void onSuccess();

        void onFailure();
    }

    interface OnReadListener{
        void onRead(byte[] data);
    }
}
