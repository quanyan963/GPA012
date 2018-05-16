package com.txtled.gp_a012.flame.mvp;

import com.txtled.gp_a012.base.RxPresenter;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.utils.BleUtils;
import com.txtled.gp_a012.utils.RxUtil;
import com.txtled.gp_a012.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.txtled.gp_a012.base.BaseFragment.TAG;
import static com.txtled.gp_a012.utils.Constants.LIGHT;
import static com.txtled.gp_a012.utils.Constants.POWER;
import static com.txtled.gp_a012.utils.Constants.SPEED;
import static com.txtled.gp_a012.utils.Constants.TO_MUSIC;

/**
 * Created by Mr.Quan on 2018/4/20.
 */

public class FlamePresenter extends RxPresenter<FlameContract.View> implements
        FlameContract.Presenter {
    private DataManagerModel mDataManagerModel;

    @Inject
    public FlamePresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void changePower(int progress, int type) {
        mDataManagerModel.updateFlame(POWER, progress);
        view.changeView(progress);
        if (type == 1) {
            mDataManagerModel.writeCommand(BleUtils.getPower(progress));
            Utils.Logger(TAG, "getPower", BleUtils.getPower(progress));
        }
    }

    @Override
    public void changeSpeed(int progress, int type) {
        mDataManagerModel.updateFlame(SPEED, progress);
        if (type == 1) {
            mDataManagerModel.writeCommand(BleUtils.getSpeed(progress));
            Utils.Logger(TAG, "getSpeed", BleUtils.getSpeed(progress));
        }
    }

    @Override
    public void changeLight(int progress, int type) {
        mDataManagerModel.updateFlame(LIGHT, progress);
        if (type == 1) {
            mDataManagerModel.writeCommand(BleUtils.getLight(progress));
            Utils.Logger(TAG, "getLight", BleUtils.getLight(progress));
        }
    }

    @Override
    public Flame getFlame() {
        return mDataManagerModel.getFlame();
    }

    @Override
    public void sendStatue(final Flame mFlame) {
        addSubscribe(Flowable.timer(30, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        changeLight(mFlame.getLight(), mFlame.getLightStatue());
                    }
                }));
        addSubscribe(Flowable.timer(60, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        changePower(mFlame.getPower(), mFlame.getLightStatue());
                    }
                }));
        addSubscribe(Flowable.timer(90, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        changeSpeed(mFlame.getSpeed(), mFlame.getLightStatue());
                    }
                }));
        addSubscribe(Flowable.timer(120, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        setPulseToMusic(mFlame.getToMusic() == 1 ? true : false,
                                mFlame.getLightStatue());
                    }
                }));
    }

    @Override
    public void setPulseToMusic(boolean isChecked, int type) {
        mDataManagerModel.updateFlame(TO_MUSIC, isChecked ? 1 : 0);
        if (type == 1){
            mDataManagerModel.writeCommand(BleUtils.getToMusic(isChecked));
        }
    }

    @Override
    public void closeToMusic() {
        mDataManagerModel.updateFlame(TO_MUSIC,0);
    }
}
