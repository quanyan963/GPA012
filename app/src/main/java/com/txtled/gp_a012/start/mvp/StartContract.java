package com.txtled.gp_a012.start.mvp;

import android.app.Activity;

import com.txtled.gp_a012.base.BasePresenter;
import com.txtled.gp_a012.base.BaseView;


/**
 * Created by Mr.Quan on 2018/4/14.
 */

public interface StartContract {
    interface View extends BaseView {
        void toMainView();
        void onShowError();
        void onShowOpenBleDialog();
        void onShowOpenGPSDialog();
        void startAnim();
        void scanFailure();
        void connFailure();
        void connected();
        void showOpenDeviceDialog();
    }

    interface Presenter extends BasePresenter<View> {
        void checkPermission(Activity activity);
        void checkPermission(boolean isSpecified, Activity activity);
        void unConn();
    }
}
