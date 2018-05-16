package com.txtled.gp_a012.main.mvp;


import com.txtled.gp_a012.base.BasePresenter;
import com.txtled.gp_a012.base.BaseView;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/4/13.
 */

public interface MainContract {
    interface View extends BaseView {
        void switchFlameView();

        void switchMusicView();

        void showDrawerOpen();

        void playMusic();

        void showPopMusicList(List<Song> musicInfoList);
    }

    interface Presenter extends BasePresenter<View> {
        void switchNavView(int id);

        void onClick(int id,int type);

        List<Song> getSongList();

        int getPlayPosition();
        void operateSwitch(boolean b);
        void checkChanged(boolean change);
        void volumeChange(int volume);
        int getVolume();
        Flame getFlame();
    }
}
