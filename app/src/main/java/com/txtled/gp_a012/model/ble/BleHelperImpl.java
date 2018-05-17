package com.txtled.gp_a012.model.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.utils.BleUtils;
import com.txtled.gp_a012.utils.Utils;
import com.txtled.gp_a012.widget.listener.BleConnListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class BleHelperImpl implements BleHelper {
    public static final String TAG = BleHelperImpl.class.getSimpleName();
    public static final int DURATION = 10000;
    public static final int TIMES = 3;
    private BluetoothClient mBleClient;
    private SearchRequest mRequest;
    private BleConnectOptions mOptions;
    private String mAddress;
    private UUID mServiceUUID;
    private UUID mSendCharacterUUID;
    private UUID mNotifyCharacterUUID;
    private BleConnectStatusListener listener;
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothSocket socket;

    @Inject
    public BleHelperImpl() {
        mBleClient = new BluetoothClient(MyApplication.getInstance());
        mRequest = new SearchRequest.Builder()
                .searchBluetoothLeDevice(DURATION, TIMES).build();
        mOptions = new BleConnectOptions.Builder()
                .setConnectRetry(TIMES)   // 连接如果失败重试2次
                .setConnectTimeout(DURATION)   // 连接超时5s
                .setServiceDiscoverRetry(TIMES)  // 发现服务如果失败重试2次
                .setServiceDiscoverTimeout(DURATION)  // 发现服务超时5s
                .build();
    }

    @Override
    public void scanBle(Activity activity, boolean isSpecified, OnScanBleListener onScanBleListener) {
        if (mBleClient.isBleSupported()) {
            if (mBleClient.isBluetoothOpened()) {
                if (Utils.isLocationEnable(activity)){
                    try {
                        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                        //得到BluetoothAdapter的Class对象
                        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;
                        Method method = bluetoothAdapterClass.getDeclaredMethod(
                                "getConnectionState", (Class[]) null);
                        method.setAccessible(true);
                        int state = (int) method.invoke(adapter, (Object[]) null);
                        if (state == BluetoothAdapter.STATE_CONNECTED) {
                            Set<BluetoothDevice> devices = adapter.getBondedDevices();
                            for (BluetoothDevice device : devices){
                                if (device.getName().contains(BleUtils.BLE_NAME)) {
                                    @SuppressLint("PrivateApi")
                                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod(
                                            "isConnected", (Class[]) null);
                                    method.setAccessible(true);
                                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (
                                            Object[]) null);
                                    if (isConnected) {
                                        Utils.Logger(TAG,"phone connected ble mac:",device.getAddress());
                                        BluetoothDevice romoteDevice = adapter.getRemoteDevice(device.getAddress());
                                        socket = romoteDevice
                                                .createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
                                        connSppBle(socket,onScanBleListener);
                                        //ble蓝牙连接
                                        //searchBleByAddress(device.getAddress(), onScanBleListener);
                                    }
                                }
                            }
                        }
                        else {
                            //是否需要连接到指定的设备
                            if (isSpecified) {
                                onScanBleListener.onDisOpenDevice();
                            } else {
                                //searchBleByAddress("", onScanBleListener);
                            }
                        }
                    } catch (NoSuchMethodException | IllegalAccessException |
                            InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //spp蓝牙连接异常
                    }
                }else {
                    onScanBleListener.onDisOpenGPS();
                }
            }else {
                onScanBleListener.onDisOpenBle();
            }
        }else {
            onScanBleListener.onDisSupported();
        }
    }

    private void connSppBle(BluetoothSocket bluetoothSocket, OnScanBleListener onScanBleListener) {
        try {
            bluetoothSocket.connect();
            onScanBleListener.onSuccess();
            //Toast.makeText(this, "connect success", Toast.LENGTH_SHORT).show();
        } catch (IOException e2) {
            e2.printStackTrace();
            onScanBleListener.onScanFailure();
            //Toast.makeText(this, "connect failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchBleByAddress(final String address, final OnScanBleListener onScanBleListener) {
        mBleClient.search(mRequest, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                onScanBleListener.onStart();
            }

            @Override
            public void onDeviceFounded(final SearchResult device) {
                Utils.Logger(TAG,"scan ble name",device.getName());
                Utils.Logger(TAG,"scan ble mac",device.getAddress());

                if (address.substring(0,7).equals(device.getAddress().
                        substring(0,7))){
                    mBleClient.stopSearch();

                    mAddress = device.getAddress();
                    onScanBleListener.onSuccess();
                }


//                if (!TextUtils.isEmpty(address)) {
//                    if (device.getAddress().equalsIgnoreCase(getMacAddress(address))) {
//                        mBleClient.stopSearch();
//                        mAddress = device.getAddress();
//                        onScanBleListener.onSuccess();
//                    }
//                } else if (device.getName().contains(BleUtils.BLE_NAME)) {
//                    mBleClient.stopSearch();
//                    mAddress = device.getAddress();
//                    onScanBleListener.onSuccess();
//                }
            }

            @Override
            public void onSearchStopped() {
                onScanBleListener.onScanFailure();
            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

    private String getMacAddress(String macAddress) {
        String oldChar = macAddress.substring(macAddress.length() - 5, macAddress.length() - 3);
        String newChar = Integer.toHexString(Integer.parseInt(oldChar, 16) + 1);
        Utils.Logger(TAG,"newMac",macAddress.replace(oldChar, newChar));
        return macAddress.replace(oldChar, newChar);
    }

    @Override
    public void connBle(final OnConnBleListener onConnBleListener) {
        mBleClient.stopSearch();
        mBleClient.connect(mAddress, mOptions, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    List<BleGattService> serviceList = profile.getServices();
                    for (BleGattService service : serviceList) {
                        Utils.Logger("service","UUID",service.getUUID().toString());
//                        List<BleGattCharacter> characters = service.getCharacters();
//                        for (BleGattCharacter character : characters) {
//                            //save uuid
//                            Log.d("service", "Permission: "+character.getPermissions());
//                            Log.d("service", "Property: "+character.getProperty());
//                            Log.d("service", "Uuid: "+character.getUuid());
//                            }
//                        }
                        if (service.getUUID().toString().contains(BleUtils.SERVICE)) {
                            List<BleGattCharacter> characters = service.getCharacters();
                            for (BleGattCharacter character : characters) {
                                //save uuid
//                                Log.d("service", "Permission: "+character.getPermissions());
//                                Log.d("service", "Property: "+character.getProperty());
//                                Log.d("service", "Descriptors: "+character.getDescriptors());
                                mServiceUUID = service.getUUID();
                                mSendCharacterUUID = character.getUuid();
                                mNotifyCharacterUUID = character.getUuid();
                                onConnBleListener.onSuccess();
//                                if (character.getUuid().toString().contains(BleUtils.
//                                        SEND_CHARACTERS)) {
//                                    mSendCharacterUUID = character.getUuid();
//                                } else if (character.getUuid().toString().contains(BleUtils.
//                                        NOTIFY_CHARACTERS)) {
//                                    mNotifyCharacterUUID = character.getUuid();
//                                    onConnBleListener.onSuccess();
//                                }
                            }
                        }
                    }
                } else {
                    onConnBleListener.onFailure();
                }
            }
        });
    }

    @Override
    public void writeCommand(String command) {
        if (mServiceUUID != null && mSendCharacterUUID != null) {
            divideFrameBleSendData(command.getBytes());
        }
    }

    //分包

    private void divideFrameBleSendData(byte[] data) {
        Utils.Logger(TAG,"BLE Write Command",new String(data));
        int tmpLen = data.length;
        int start = 0;
        int end = 0;
//        while (tmpLen > 0) {
//            byte[] sendData = new byte[21];
//            if (tmpLen >= 20) {
//                end += 20;
//                sendData = Arrays.copyOfRange(data, start, end);
//                start += 20;
//                tmpLen -= 20;
//            } else {
//                end += tmpLen;
//                sendData = Arrays.copyOfRange(data, start, end);
//                tmpLen = 0;
//            }
//
//            mBleClient.write(mAddress, mServiceUUID, mSendCharacterUUID, sendData,
//                    new BleWriteResponse() {
//                        @Override
//                        public void onResponse(int code) {
//
//                        }
//                    });
//
//        }

        //spp
        try {
            OutputStream os = socket.getOutputStream();
//            byte[] osBytes = etInput.getText().toString().getBytes();
//            for (int i = 0; i < data.length; i++) {
//                if (osBytes[i] == 0x0a)
//                    n++;
//            }
//            byte[] osBytesNew = new byte[osBytes.length+n];
//            n = 0;
//            for (int i = 0; i < osBytesNew.length; i++) {
//                //mobile "\n"is 0a,modify 0d 0a then send
//                if (osBytesNew[i] == 0x0a) {
//                    osBytesNew[n] = 0x0d;
//                    n++;
//                    osBytesNew[n] = 0x0a;
//                }else {
//                    osBytesNew[n] = osBytes[i];
//                }
//                n++;
//            }
            while (tmpLen > 0) {
                byte[] sendData = new byte[21];
                if (tmpLen >= 20) {
                    end += 20;
                    sendData = Arrays.copyOfRange(data, start, end);
                    start += 20;
                    tmpLen -= 20;
                } else {
                    end += tmpLen;
                    sendData = Arrays.copyOfRange(data, start, end);
                    tmpLen = 0;
                }

                os.write(sendData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyBle() {
        if (mServiceUUID != null && mNotifyCharacterUUID != null) {
            mBleClient.notify(mAddress, mServiceUUID, mNotifyCharacterUUID, new BleNotifyResponse() {
                @Override
                public void onNotify(UUID service, UUID character, byte[] value) {
                    Utils.Logger(TAG,"BLE Notify",new String(value));
                }

                @Override
                public void onResponse(int code) {
                    Utils.Logger(TAG,"BLE Notify code",code+"");
                }
            });
        }
    }

    @Override
    public void isBleConnected(final BleConnListener bleListener) {
        listener = new BleConnectStatusListener(){

            @Override
            public void onConnectStatusChanged(String mac, int status) {
                if (status == STATUS_CONNECTED){
                    bleListener.onConn();
                }else if (status == STATUS_DISCONNECTED){
                    bleListener.onDisConn();
                }
            }
        };
        mBleClient.registerConnectStatusListener(mAddress,listener);
    }

    @Override
    public void readCommand(final OnReadListener readListener) {
//        mBleClient.read(mAddress, mServiceUUID, mSendCharacterUUID, new BleReadResponse() {
//            @Override
//            public void onResponse(int code, byte[] data) {
//                if (code == REQUEST_SUCCESS){
//                    readListener.onRead(data);
//                }
//            }
//        });

        //spp
        try {
            byte[] read = new byte[21];
            InputStream inputStream = socket.getInputStream();
            socket.getInputStream().read(read);
            readListener.onRead(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unRegisterConn() {
        mBleClient.disconnect(mAddress);
        mBleClient.unregisterConnectStatusListener(mAddress,listener);
        listener = null;

        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "socket close failed", Toast.LENGTH_SHORT).show();
        }
        return;
    }
}
