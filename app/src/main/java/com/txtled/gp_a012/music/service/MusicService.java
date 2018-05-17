package com.txtled.gp_a012.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.bean.event.FlameEvent;
import com.txtled.gp_a012.bean.event.MusicServiceEvent;
import com.txtled.gp_a012.bean.event.PlayVolumeEvent;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.model.ble.BleHelper;
import com.txtled.gp_a012.utils.Constants;
import com.txtled.gp_a012.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.txtled.gp_a012.utils.BleUtils.OPEN_CLOSE;
import static com.txtled.gp_a012.utils.BleUtils.POWER_REQ;
import static com.txtled.gp_a012.utils.BleUtils.REQUEST_REQ;
import static com.txtled.gp_a012.utils.Constants.LIGHT;
import static com.txtled.gp_a012.utils.Constants.LIGHT_STATUE;
import static com.txtled.gp_a012.utils.Constants.POWER;
import static com.txtled.gp_a012.utils.Constants.SPEED;
import static com.txtled.gp_a012.utils.Constants.TO_MUSIC;

/**
 * Created by KomoriWu
 * on 2017-09-25.
 */

public class MusicService extends Service implements BleHelper.OnReadListener{
    public static final String TAG = MusicService.class.getSimpleName();
    public static final int UPDATE_DELAY = 0;
    public static final int UPDATE_PERIOD = 1;
    private MediaPlayer mMediaPlayer;
    private int mCurrentPlayPosition;
    private List<Song> mSongList;
    private Song mSong;
    private Disposable mDisposable;
    private int mProgress;
    private boolean mIsRandom;
    private boolean mIsCycle;
    private DataManagerModel mDataManagerModel;

    @Override
    public void onCreate() {
        super.onCreate();
        mDataManagerModel = MyApplication.getAppComponent().getDataManagerModel();
        mSongList = mDataManagerModel.getMusicInfoList();
        mIsCycle = mDataManagerModel.getIsCycle();
        mIsRandom = mDataManagerModel.getIsRandom();
        mMediaPlayer = new MediaPlayer();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    @Override
    public void onRead(byte[] data) {
        String mData = data.toString();

        switch (mData.substring(5,6)){
            case POWER_REQ:
                String num = mData.substring(6,8);
                if (num.equals("03")){//脉动音乐
                    mDataManagerModel.updateFlame(TO_MUSIC,1);
                    EventBus.getDefault().post(new FlameEvent(TO_MUSIC,1));
                }else {//1~2
                    mDataManagerModel.updateFlame(LIGHT, Integer.parseInt(num)-1);
                    EventBus.getDefault().post(new FlameEvent(LIGHT,Integer
                            .parseInt(num)-1));
                }
                break;
            case REQUEST_REQ://返回所有数据
                String allData = mData.substring(8,mData.length()-1);
                mDataManagerModel.updateFlame(LIGHT_STATUE, Integer.parseInt(allData.substring(0,2),16));
                mDataManagerModel.updateFlame(LIGHT, Integer.parseInt(allData.substring(2,4),16)-1);
                mDataManagerModel.updateFlame(POWER, Integer.parseInt(allData.substring(4,6),16)-1);
                mDataManagerModel.updateFlame(TO_MUSIC, Integer.parseInt(allData.substring(6,8),16)-1);
                mDataManagerModel.updateFlame(SPEED,Integer.parseInt(allData.substring(8,10),16));
//                mDataManagerModel.setMainVolume(Integer.parseInt(allData[5],16));
//                EventBus.getDefault().post(new PlayVolumeEvent(Integer.parseInt(allData[5],16)));

                break;
            case OPEN_CLOSE://开灯
                String statue = mData.substring(6,8);
                mDataManagerModel.updateFlame(LIGHT_STATUE,Integer.parseInt(statue,16));
                EventBus.getDefault().post(new FlameEvent(LIGHT_STATUE,Integer
                        .parseInt(statue,16)));
                break;

        }
    }

    private class MusicControl extends Binder implements MusicInterface {

        @Override
        public void play(int position) {
            mDataManagerModel.setPlayPosition(position);
            mCurrentPlayPosition = position;
            mSong = mSongList.get(mCurrentPlayPosition);
            try {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }
                try {
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(mSong.getUrl());
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IllegalStateException e) {
                    mMediaPlayer = null;
                    mMediaPlayer = new MediaPlayer();
                }
                if (mMediaPlayer.isPlaying()) {
                    addTimer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void pausePlay(int position) {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    dispose();
                    mMediaPlayer.pause();
                    EventBus.getDefault().post(new MusicServiceEvent(mProgress,
                            mCurrentPlayPosition, false));
                } else {
                    mMediaPlayer.start();
                    addTimer();
                    EventBus.getDefault().post(new MusicServiceEvent(mProgress,
                            mCurrentPlayPosition, true));
                }
            } else {
                play(position);
            }

            if (position != -1) {
                EventBus.getDefault().post(new PlayVolumeEvent(Constants.FLAG_MUSIC, mMediaPlayer.
                        isPlaying()));
                //mDataManagerModel.updatePlayState(Constants.FLAG_MUSIC);
                //mDataManagerModel.writeCommand(BleUtils.getPlayMusic(mMediaPlayer.isPlaying()));
            }
        }

        @Override
        public void playNext() {
            new MusicControl().play(getNextPosition());
        }

        @Override
        public void playPrevious() {
            new MusicControl().play(getPreviousPosition());
        }

        @Override
        public boolean isPlaying() {
            return mMediaPlayer != null && mMediaPlayer.isPlaying();
        }

        @Override
        public void seekTo(int progress) {
            if (mMediaPlayer != null) {
                dispose();
                mMediaPlayer.seekTo(progress * 1000);
                addTimer();
            }
        }

        @Override
        public void playCycle(boolean state) {
            mIsCycle = state;
        }

        @Override
        public void playRandom(boolean state) {
            mIsRandom = state;
        }

        @Override
        public MediaPlayer getMediaPlayer() {
            return mMediaPlayer;
        }

        @Override
        public void initPlayer(int position) {
            mDataManagerModel.setPlayPosition(position);
            mCurrentPlayPosition = position;
            mSong = mSongList.get(mCurrentPlayPosition);

            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mSong.getUrl());
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
                mMediaPlayer = new MediaPlayer();
            }
        }

        @Override
        public void initRead() {
            mDataManagerModel.readCommand(MusicService.this);
        }

    }

    private void addTimer() {
        dispose();
        mProgress = mMediaPlayer.getCurrentPosition() / 1000;
        final int duration = mSong.getFormatDuration();
        Observable.interval(UPDATE_DELAY, UPDATE_PERIOD, TimeUnit.SECONDS)
                .take(duration - mProgress + UPDATE_PERIOD)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return (long) (mProgress++);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        EventBus.getDefault().post(new MusicServiceEvent(Integer.parseInt(String.
                                valueOf(aLong)), mCurrentPlayPosition, true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Utils.Logger(TAG,"errorMsg",e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (mCurrentPlayPosition == mSongList.size() - 1 && !mIsCycle) {
                            new MusicControl().pausePlay(mCurrentPlayPosition);
                            mMediaPlayer = null;
                        } else {
                            new MusicControl().play(getNextPosition());
                        }
                    }
                });
    }

    private void dispose() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public int getNextPosition() {
        if (mIsRandom) {
            return (int) (Math.random() * mSongList.size() - 1);
        } else {
            int nextPosition = 0;
            if (mCurrentPlayPosition < mSongList.size() - 1) {
                nextPosition = mCurrentPlayPosition + 1;
            }
            return nextPosition;
        }
    }

    public int getPreviousPosition() {
        int previousPosition = mCurrentPlayPosition - 1;
        if (mCurrentPlayPosition == 0) {
            previousPosition = mSongList.size() - 1;
        }
        return previousPosition;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            dispose();
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }

}
