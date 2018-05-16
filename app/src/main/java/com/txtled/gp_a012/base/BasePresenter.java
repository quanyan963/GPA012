package com.txtled.gp_a012.base;

/**
 * Created by Mr.Quan on 2018/4/13.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);
    void detachView();
}
