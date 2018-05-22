package com.txtled.gp_a012.flame.mvp;

import android.content.Context;

import com.txtled.gp_a012.base.BasePresenter;
import com.txtled.gp_a012.base.BaseView;
import com.txtled.gp_a012.bean.Flame;

/**
 * Created by Mr.Quan on 2018/4/20.
 */

public interface FlameContract {
    interface View extends BaseView{
        void changeView(int type);
    }

    interface Presenter extends BasePresenter<View>{
        void changePower(int progress, int type, Context context);
        void changeSpeed(int progress, int type, Context context);
        void changeLight(int progress, int type, Context context);
        Flame getFlame();
        void sendStatue(Flame flame, Context context);
        void setPulseToMusic(boolean isChecked, int type, Context context);
        void closeToMusic();
    }
}
