package com.txtled.gp_a012.music.mvp;



import com.txtled.gp_a012.base.BasePresenter;
import com.txtled.gp_a012.base.BaseView;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.music.service.MusicInterface;

import java.util.List;

/**
 * Created by KomoriWu
 * on 2017/9/18.
 */

public interface MusicContract {
    interface View extends BaseView {
        void showPopMusicList(List<Song> musicInfoList);
        void showNoSongToast();
    }

    interface Presenter extends BasePresenter<View> {
        void dealOnClick(int viewId, int id, MusicInterface musicInterface);
        List<Song> getSongList();

        int getPlayPosition();
        boolean getIsCycle();

        boolean getIsRandom();

        void onCheckedChanged(int id, boolean b, MusicInterface musicInterface);
        int getVolume();
        void setVolume(int index);
        void operateVolume(int volume);

    }


}
