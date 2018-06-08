package com.txtled.gp_a012.music.mvp;



import android.content.Context;

import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.RxPresenter;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.bean.event.PlayVolumeEvent;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.music.service.MusicInterface;
import com.txtled.gp_a012.utils.BleUtils;
import com.txtled.gp_a012.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import static com.txtled.gp_a012.base.BaseFragment.TAG;

/**
 * Created by KomoriWu
 * on 2017/9/18.
 */

public class MusicPresenter extends RxPresenter<MusicContract.View> implements MusicContract.
        Presenter {
    private DataManagerModel mDataManagerModel;

    @Inject
    public MusicPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }


    @Override
    public void dealOnClick(int viewId, int position, MusicInterface musicInterface) {
        if (position != -1){
            switch (viewId) {
                case R.id.iv_play:
                case R.id.iv_sb_play:
                    musicInterface.pausePlay(position);
                    break;
                case R.id.iv_list:
                    view.showPopMusicList(mDataManagerModel.getMusicInfoList());
                    break;
                case R.id.iv_previous:
                    musicInterface.playPrevious();
                    break;
                case R.id.iv_next:
                    musicInterface.playNext();
                    break;
            }
        }else {
            view.showNoSongToast();
        }
    }


    @Override
    public void operateVolume(int volume, Context context) {
        //mDataManagerModel.writeCommand(BleUtils.getSound(volume),context);
        Utils.Logger(TAG,"getSound",BleUtils.getSound(volume));
        EventBus.getDefault().post(new PlayVolumeEvent(volume));
    }

    @Override
    public List<Song> getSongList() {
        return mDataManagerModel.getMusicInfoList();
    }

    @Override
    public int getPlayPosition() {
        return mDataManagerModel.getPlayPosition();
    }

    @Override
    public boolean getIsCycle() {
        return mDataManagerModel.getIsCycle();
    }

    @Override
    public boolean getIsRandom() {
        return mDataManagerModel.getIsRandom();
    }

    @Override
    public void onCheckedChanged(int id, boolean b, MusicInterface musicInterface) {
        switch (id) {
            case R.id.cb_cycle:
                mDataManagerModel.setIsCycle(b);
                musicInterface.playCycle(b);
                break;
            case R.id.cb_random:
                mDataManagerModel.setIsRandom(b);
                musicInterface.playRandom(b);
                break;
        }
    }
    @Override
    public int getVolume() {
        return mDataManagerModel.getMainVolume();
    }

    @Override
    public void setVolume(int index) {
        mDataManagerModel.setMainVolume(index);
    }

}
