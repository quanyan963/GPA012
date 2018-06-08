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
    }

    interface Presenter extends BasePresenter<View> {

    }
}
