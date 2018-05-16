package com.txtled.gp_a012.start.mvp;

import android.app.Activity;


import com.txtled.gp_a012.base.RxPresenter;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.model.ble.BleHelper;
import com.txtled.gp_a012.model.operate.OperateHelper;
import com.txtled.gp_a012.utils.BleUtils;
import com.txtled.gp_a012.utils.Constants;
import com.txtled.gp_a012.utils.RxUtil;
import com.txtled.gp_a012.widget.listener.BleConnListener;

import org.reactivestreams.Subscription;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by Mr.Quan on 2018/4/14.
 */

public class StartPresenter extends RxPresenter<StartContract.View> implements StartContract.Presenter {

    public static final int DELAY = 100;
    private DataManagerModel mDataManagerModel;

    @Inject
    public StartPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void checkPermission(Activity activity) {
        this.checkPermission(true,activity);
    }

    @Override
    public void checkPermission(final boolean isSpecified, final Activity activity) {
        final String[] permissions = new String[]{Constants.permissions[0], Constants.
                permissions[1]};
        mDataManagerModel.requestPermissions(activity, permissions, new OperateHelper.
                OnPermissionsListener() {
            @Override
            public void onSuccess(String permissionName) {
                if (permissionName.equals(permissions[0])) {
                    //initSql();
                    //scanBle(activity,isSpecified);
                    view.toMainView();
                }

            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onAskAgain() {

            }
        });
    }

    @Override
    public void unConn() {
        mDataManagerModel.unRegisterConn();
    }

    private void scanBle(final Activity activity, boolean isSpecified) {
        mDataManagerModel.scanBle(activity,isSpecified, new BleHelper.OnScanBleListener() {
            @Override
            public void onStart() {
                view.startAnim();
            }

            @Override
            public void onSuccess() {
                mDataManagerModel.connBle(new BleHelper.OnConnBleListener() {
                    @Override
                    public void onSuccess() {
                        mDataManagerModel.notifyBle();
                        addSubscribe(Flowable.timer(DELAY, TimeUnit.MILLISECONDS)
                                .compose(RxUtil.<Long>rxSchedulerHelper())
                                .doOnSubscribe(new Consumer<Subscription>() {
                                    @Override
                                    public void accept(Subscription subscription) throws Exception {
                                        mDataManagerModel.isBleConnected(new BleConnListener() {
                                            @Override
                                            public void onConn() {
                                                mDataManagerModel.writeCommand(BleUtils.getBleStatue());
                                                //view.setBleStatue(true);
                                            }

                                            @Override
                                            public void onDisConn() {
                                                //view.setBleStatue(false);
                                                mDataManagerModel.writeCommand(BleUtils.getBleStatue());
                                                unConn();
                                            }
                                        });
                                        view.connected();
                                    }
                                })
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        view.toMainView();
                                    }
                                }));
                    }

                    @Override
                    public void onFailure() {
                        view.connFailure();
                    }
                });
            }

            @Override
            public void onScanFailure() {
                view.scanFailure();
            }

            @Override
            public void onDisOpenDevice() {
                //scanBle(false);
                if (!mDataManagerModel.getInitDialog()) {
                    view.showOpenDeviceDialog();
                }else {
                    scanBle(activity,false);
                }
            }

            @Override
            public void onDisOpenBle() {
                view.onShowOpenBleDialog();
            }

            @Override
            public void onDisOpenGPS() {
                view.onShowOpenGPSDialog();
            }

            @Override
            public void onDisSupported() {
                view.onShowError();
            }
        });
    }

    private void initSql() {
        if (mDataManagerModel.getIsFirstApp()) {

            mDataManagerModel.setIsFirstApp(false);
        }
    }
}
