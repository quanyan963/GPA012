package com.txtled.gp_a012.menu.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;


import com.txtled.gp_a012.utils.BleUtils;
import com.txtled.gp_a012.utils.BluetoothTools;
import com.txtled.gp_a012.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KomoriWu on 2017/6/24.
 */

@SuppressLint("Registered")
public class ConnBleService extends Service {
    private static final int TIMER_DELAY = 5100;
    private static final int TIMER_PERIOD = 100;
    private Timer mTimer;
    private ConnBleObservable mConnBleObservable;
    public String TAG = "MainMenuActivity";
    public String actionCon;
    public BluetoothCommunThread communThread;


    private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            actionCon = intent.getAction();

            //如果匹配成功
            if (BluetoothTools.ACTION_PAIRING_SUCC.equals(actionCon)){
                Bundle mBundle = intent.getExtras();
                BluetoothDevice mBluetoothDevice = mBundle.getParcelable("Pairing_Succ");
                BluetoothClientConnThread mThread = new BluetoothClientConnThread(handler,mBluetoothDevice);
                mThread.start();
            }

            //如果键盘点击send发送数据
            if(BluetoothTools.ACTION_DATA_TO_GAME.equals(actionCon)){

//                try {
//                    String editData = (String)intent.getExtras().get("editViewData");
//
//                    byte[] bytes = editData.getBytes("gbk");
//
//                    communThread.write(bytes);
//
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                byte[] bytes = (byte[]) intent.getExtras().get("editViewData");
                communThread.write(bytes);

            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mConnBleObservable = new ConnBleObservable();
        IntentFilter controlFilter = new IntentFilter();
        controlFilter.addAction(BluetoothTools.ACTION_PAIRING_SUCC);
        controlFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);

        //注册BroadcastReceiver
        registerReceiver(controlReceiver, controlFilter);
    }

    Handler handler = new Handler()    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                //子线程socket连接成功
                case BluetoothTools.MESSAGE_CONNECT_SUCCESS:{
                    Intent mIntent = new Intent(BluetoothTools.ACTION_CONNECT_SUC);
                    sendBroadcast(mIntent);
                    communThread  = new BluetoothCommunThread(handler,(BluetoothSocket)msg.obj);
                    communThread.start();
                    break;

                    //子线程连接错误
                } case BluetoothTools.MESSAGE_CONNECT_ERROR:{
                    Intent mIntent = new Intent(BluetoothTools.ACTION_CONNECT_ERROR);
                    sendBroadcast(mIntent);
                    break;

                    //子线程读取到数据
                } case BluetoothTools.MESSAGE_READ_OBJECT: {
                    //读取到对象
                    //发送数据广播（包含数据对象）
                    byte[] readBuf = null;
                    readBuf =(byte[]) msg.obj;
                    String ss1 = new String(readBuf, 0,msg.arg1);
                    String ss = null;
                    try {
                        ss = new String(readBuf,"gbk");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG,ss1+","+ss);
                    Intent recIntent = new Intent(BluetoothTools.ACTION_RECEIVE_DATA);
                    recIntent.putExtra("recData",ss);
                    sendBroadcast(recIntent);
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new connBleBinder();
    }

    class connBleBinder extends Binder implements ConnBleInterface {

        @Override
        public void scanBle() {
            startTimer();
        }

        @Override
        public void addObserver(Observer observer) {
            mConnBleObservable.addObserver(observer);
        }
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimer != null) {
            mTimer.schedule(new ConnTimerTask(), TIMER_DELAY, TIMER_PERIOD);
        }
    }

    class ConnTimerTask extends TimerTask {
        public void run() {
            checkBluetooth();
        }
    }

    private void checkBluetooth() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            //得到BluetoothAdapter的Class对象
            Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
            Method method = null;
            method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState",
                    (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Set<BluetoothDevice> devices = adapter.getBondedDevices();

                for (BluetoothDevice device : devices) {
                    Utils.Logger("bluename","本机",device.getName());
                    Utils.Logger("bluename","本机",device.getAddress());
                    if (device.getName().contains(BleUtils.BLE_NAME)) {
                        @SuppressLint("PrivateApi")
                        Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod(
                                "isConnected", (Class[]) null);
                        method.setAccessible(true);
                        boolean isConnected = (boolean) isConnectedMethod.invoke(device, (
                                Object[]) null);
                        if (isConnected) {
                            Utils.Logger("bluename","connected",device.getAddress());
                            mConnBleObservable.notifyChanged(device.getAddress());
                            stopTimer();
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public class ConnBleObservable extends Observable {
        public void notifyChanged(String macAddress) {
            this.setChanged();
            this.notifyObservers(macAddress);
        }
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mConnBleObservable.deleteObservers();
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        mConnBleObservable.deleteObservers();
        mConnBleObservable = null;
        stopTimer();
        communThread.cancel();
        unregisterReceiver(controlReceiver);
        stopSelf();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
