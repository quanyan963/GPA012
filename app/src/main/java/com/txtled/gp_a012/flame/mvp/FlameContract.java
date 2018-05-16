package com.txtled.gp_a012.flame.mvp;

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
        void changePower(int progress, int type);
        void changeSpeed(int progress, int type);
        void changeLight(int progress, int type);
        Flame getFlame();
        void sendStatue(Flame flame);
        void setPulseToMusic(boolean isChecked, int type);
        void closeToMusic();
    }
}
