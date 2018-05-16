package com.txtled.gp_a012.main.mvp;


import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.RxPresenter;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.bean.event.FlameEvent;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.utils.BleUtils;
import com.txtled.gp_a012.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import static com.txtled.gp_a012.base.BaseActivity.TAG;
import static com.txtled.gp_a012.utils.Constants.LIGHT_STATUE;

/**
 * Created by Mr.Quan on 2018/4/13.
 */

public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    private DataManagerModel mDataManagerModel;

    @Inject
    public MainPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }


    @Override
    public void switchNavView(int id) {
        switch (id) {
            case R.id.rb_music:
                view.switchMusicView();
                break;
            case R.id.rb_flame:
                view.switchFlameView();
                break;
        }
    }

    @Override
    public void onClick(int id, int type) {
        switch (id) {
            case R.id.iv_play:
                view.playMusic();
                break;
            case R.id.iv_list:
                view.showPopMusicList(mDataManagerModel.getMusicInfoList());
                break;
            case R.id.iv_right:
                if (type == R.id.rb_music) {
                    view.showPopMusicList(mDataManagerModel.getMusicInfoList());
                } else {
                    view.showDrawerOpen();
                }
                break;
            case R.id.head_back:
            case R.id.il_about:
            case R.id.il_instruction:
            case R.id.rb_setting:
                view.showDrawerOpen();
                break;
        }
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
    public void operateSwitch(boolean b) {

    }

    @Override
    public void checkChanged(boolean change) {
        mDataManagerModel.writeCommand(BleUtils.getLightSwitch(change));
        mDataManagerModel.updateFlame(LIGHT_STATUE, change ? 1 : 0);
        Utils.Logger(TAG,"getLightSwitch",BleUtils.getLightSwitch(change));
        EventBus.getDefault().post(new FlameEvent(LIGHT_STATUE,change ? 1 : 0));
    }

    @Override
    public void volumeChange(int volume) {
        mDataManagerModel.writeCommand(BleUtils.getSound(volume));
        Utils.Logger(TAG,"volumeChange",BleUtils.getSound(volume));
        mDataManagerModel.setMainVolume(volume);
    }

    @Override
    public int getVolume() {
        return mDataManagerModel.getMainVolume();
    }

    @Override
    public Flame getFlame() {
        return mDataManagerModel.getFlame();
    }
}
