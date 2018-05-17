package com.txtled.gp_a012.menu.mvp;

import android.app.Activity;


import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.CommonSubscriber;
import com.txtled.gp_a012.base.RxPresenter;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.model.ble.BleHelper;
import com.txtled.gp_a012.model.operate.OperateHelper;
import com.txtled.gp_a012.utils.BleUtils;
import com.txtled.gp_a012.utils.Constants;
import com.txtled.gp_a012.utils.RxUtil;
import com.txtled.gp_a012.utils.Utils;
import com.txtled.gp_a012.widget.listener.BleConnListener;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.txtled.gp_a012.base.BaseActivity.TAG;

/**
 * Created by KomoriWu
 * on 2017/9/18.
 */

public class MenuPresenter extends RxPresenter<MenuContract.View> implements MenuContract.Presenter {
    public static final int DELAY = 1;
    private DataManagerModel mDataManagerModel;

    @Inject
    public MenuPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void setBleListener() {
        mDataManagerModel.isBleConnected(new BleConnListener() {
            @Override
            public void onConn() {
                view.setBleStatue(true);
            }

            @Override
            public void onDisConn() {
                //view.setBleStatue(false);
            }
        });
    }

    @Override
    public void checkPermission(final Activity activity) {
        final String[] permissions = new String[]{Constants.permissions[2]};
        mDataManagerModel.requestPermissions(activity, permissions, new OperateHelper.
                OnPermissionsListener() {
            @Override
            public void onSuccess(String permissionName) {
                scanMusic(activity);
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
    public void checkBlePermission(final boolean isSpecified, final Activity activity) {
        final String[] permissions = new String[]{Constants.permissions[0], Constants.
                permissions[1]};
        mDataManagerModel.requestPermissions(activity, permissions, new OperateHelper.
                OnPermissionsListener() {
            @Override
            public void onSuccess(String permissionName) {
                if (permissionName.equals(permissions[0])) {
                    initSql();
                    scanBle(activity,isSpecified);
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

    private void initSql() {
        if (mDataManagerModel.getIsFirstApp()) {
            mDataManagerModel.insertFlame(new Flame(true));
            mDataManagerModel.setIsFirstApp(false);
        }
    }

    private void scanBle(final Activity activity, boolean isSpecified) {
        mDataManagerModel.scanBle(activity,isSpecified, new BleHelper.OnScanBleListener() {
            @Override
            public void onStart() {
                view.startAnim();
            }

            @Override
            public void onSuccess() {
                //view.connected();
                mDataManagerModel.isBleConnected(new BleConnListener() {
                    @Override
                    public void onConn() {
                        view.setBleStatue(true);
                    }

                    @Override
                    public void onDisConn() {
                        view.setBleStatue(false);
                        unConn();
                    }
                });

                mDataManagerModel.connBle(new BleHelper.OnConnBleListener() {
                    @Override
                    public void onSuccess() {
                        mDataManagerModel.notifyBle();
                        addSubscribe(Flowable.timer(DELAY, TimeUnit.MILLISECONDS)
                                .compose(RxUtil.<Long>rxSchedulerHelper())
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        //setBleListener();

                                        view.connected();
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

    private void scanMusic(final Activity activity) {
        mDataManagerModel.deleteAllSong();
        addSubscribe(mDataManagerModel.scanMusic(activity)
                .flatMap(new Function<ArrayList<Song>, Publisher<Song>>() {
                    @Override
                    public Publisher<Song> apply(@NonNull ArrayList<Song> songBeen) throws
                            Exception {
                        return Flowable.fromIterable(songBeen);
                    }
                }).delay(DELAY, TimeUnit.SECONDS)
                .compose(RxUtil.<Song>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Song>(view) {
                    @Override
                    protected void onStart() {
                        view.showScanSnackBar();
                        super.onStart();
                    }

                    @Override
                    public void onNext(Song songBeen) {
                        mDataManagerModel.insertMusic(songBeen);
                    }

                    @Override
                    public void onComplete() {
                        view.hideScanSnackBar();
                    }
                }));

    }


    @Override
    public void switchNavView(int id) {
        view.toMainView(id);
    }

    @Override
    public void onClick(int id,boolean isFinish) {
        switch (id) {
            case R.id.head_back:
            case R.id.il_about:
            case R.id.il_instruction:
            case R.id.rb_setting:
                view.showDrawerOpen();
                break;
            case R.id.iv_play:
                if (isFinish)return;
                view.playMusic();
                break;
            case R.id.iv_list:
                if (isFinish)return;
                view.showPopMusicList(mDataManagerModel.getMusicInfoList());
                break;
            default:
                view.toMainView(id);
                break;
        }
    }

    @Override
    public List<Song> getSongList() {
        return mDataManagerModel.getMusicInfoList();
    }

    @Override
    public void unConn() {
        mDataManagerModel.unRegisterConn();
    }

    @Override
    public int getPlayPosition() {
        return mDataManagerModel.getPlayPosition();
    }

    @Override
    public void getBleConnectedStatue() {
        mDataManagerModel.writeCommand(BleUtils.getBleStatue());
        Utils.Logger(TAG,"getBleStatue",BleUtils.getBleStatue());
    }

    @Override
    public void volumeChange(int volume) {
        mDataManagerModel.writeCommand(BleUtils.getSound(volume));
        Utils.Logger(TAG,"volumeChange",BleUtils.getSound(volume));
        mDataManagerModel.setMainVolume(volume);
    }

    @Override
    public int getVolume() {
        return mDataManagerModel.getMainVolume();
    }

    @Override
    public void checkChange(int checkId) {
        view.toMainView(checkId);
    }
}
