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
}
