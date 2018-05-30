package com.txtled.gp_a012.music;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;


import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.MvpBaseFragment;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.bean.event.MusicServiceEvent;
import com.txtled.gp_a012.bean.event.PlayVolumeEvent;
import com.txtled.gp_a012.main.MainActivity;
import com.txtled.gp_a012.music.mvp.MusicContract;
import com.txtled.gp_a012.music.mvp.MusicPresenter;
import com.txtled.gp_a012.music.service.MusicInterface;
import com.txtled.gp_a012.utils.AlertUtils;
import com.txtled.gp_a012.utils.RxUtil;
import com.txtled.gp_a012.utils.Utils;
import com.txtled.gp_a012.widget.CircularSeekBar;
import com.txtled.gp_a012.widget.CustomTextView;
import com.txtled.gp_a012.widget.ItemLayout;
import com.txtled.gp_a012.widget.listener.MusicListListener;
import com.txtled.gp_a012.widget.listener.ViewClickListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


/**
 * Created by KomoriWu
 * on 2017/9/18.
 */

public class MusicFragment extends MvpBaseFragment<MusicPresenter> implements MusicContract.View,
        CircularSeekBar.OnCircularSeekBarChangeListener, ItemLayout.OnSeekBarListener,
        CompoundButton.OnCheckedChangeListener, View.OnTouchListener {
    public static final String TAG = MusicFragment.class.getSimpleName();
    public static final long DELAY = 200;
    @BindView(R.id.iv_cover_bg)
    ImageView ivCoverBg;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_music_name)
    CustomTextView tvMusicName;
    @BindView(R.id.tv_music_player)
    CustomTextView tvMusicPlayer;
    @BindView(R.id.iv_list)
    ImageView ivList;
    @BindView(R.id.iv_previous)
    ImageView ivPrevious;
    @BindView(R.id.mtv_count_time)
    CustomTextView mtvCountTime;
    @BindView(R.id.csb_round)
    CircularSeekBar csbRound;
    @BindView(R.id.mtv_surplus_time)
    CustomTextView mtvSurplusTime;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_sb_play)
    ImageView ivSbPlay;
    @BindView(R.id.ll_player)
    RelativeLayout llPlayer;
    @BindView(R.id.rl_player)
    RelativeLayout rlPlayer;
    @BindView(R.id.il_voice)
    ItemLayout ilVoice;
    @BindView(R.id.cb_cycle)
    CheckBox cbCycle;
    @BindView(R.id.cb_random)
    CheckBox cbRandom;
    private List<Song> mSongList;
    private int mPosition = -1;
    private int mDuration;
    private int mVolume;
    private int mIsUpdateImg = -1;
    private AudioManager mAudioManager;
    private int maxValue;
    private float everyValue;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, null);
    }

    @Override
    public void init() {
        rlPlayer.setBackgroundColor(getResources().getColor(R.color.half_transparent));
        mSongList = presenter.getSongList();
        mPosition = presenter.getPlayPosition();
        mVolume = presenter.getVolume();
        if (mSongList.size() > mPosition) {
            initPlayUi(mSongList.get(mPosition));
        } else {
            mPosition = -1;
        }
        cbCycle.setChecked(presenter.getIsCycle());
        cbRandom.setChecked(presenter.getIsRandom());
        updatePlayUi();
        mAudioManager = (AudioManager) getActivity().
                getSystemService(Context.AUDIO_SERVICE);
        maxValue = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        everyValue = maxValue / 16f;
        ilVoice.setSeekBarProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        ivPlay.setVisibility(View.INVISIBLE);
        ivList.setVisibility(View.INVISIBLE);
        initListener();

    }

    private void initListener() {
        ivNext.setOnTouchListener(this);
        ivPrevious.setOnTouchListener(this);
        ivSbPlay.setOnTouchListener(this);
        csbRound.setOnSeekBarChangeListener(this);
        ilVoice.setOnSeekBarListener(this);
        cbCycle.setOnCheckedChangeListener(this);
        cbRandom.setOnCheckedChangeListener(this);
    }

    private void updatePlayUi() {
        Flowable.timer(DELAY, TimeUnit.MILLISECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (getMusicInterface() != null && getMusicInterface().getMediaPlayer() != null) {
                            ivPlay.setImageResource(getMusicInterface().isPlaying() ?
                                    R.mipmap.ic_music_pause_mini :
                                    R.mipmap.ic_home_play_btn);
                            ivSbPlay.setImageResource(getMusicInterface().isPlaying() ?
                                    R.mipmap.ic_music_pause :
                                    R.mipmap.ic_music_play);
                            int duration = getMusicInterface().getMediaPlayer().getDuration() / 1000;
                            int progress = getMusicInterface().getMediaPlayer().getCurrentPosition() / 1000;
                            mtvCountTime.setText(Utils.getShowTime(progress));
                            mtvSurplusTime.setText("-" + Utils.getShowTime(duration - progress));
                            csbRound.setProgress(progress);
                        }

                    }
                });
    }


    @OnClick({R.id.iv_play, R.id.iv_list, R.id.iv_previous, R.id.iv_sb_play, R.id.iv_next})
    public void onViewClicked(View view) {
        presenter.dealOnClick(view.getId(), mPosition, getMusicInterface());
    }

    @Override
    public void showPopMusicList(List<Song> musicInfoList) {
        Utils.showBottomDialog(getActivity(), getMusicInterface().isPlaying(), musicInfoList, new
                MusicListListener() {
                    @Override
                    public void onItemSelected(int position) {
                        getMusicInterface().play(position);
                    }
                });
    }

    @Override
    public void showNoSongToast() {
        AlertUtils.showSnackbar(getContext(), ilVoice, R.string.there_was_no_playable_song);
    }

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar seekBar) {
        getMusicInterface().seekTo(seekBar.getProgress());

    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mVolume = seekBar.getProgress();
        presenter.operateVolume(seekBar.getProgress(),getContext());
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (mVolume * everyValue), 0);
        presenter.setVolume(mVolume);
    }

    @Subscribe
    public void onEventFragmentThread(MusicServiceEvent event) {
        int progress = event.getProgress();
        if (mPosition != event.getPosition()) {
            mPosition = event.getPosition();
            Song song = mSongList.get(mPosition);
            initPlayUi(song);
        }

        int a = getMusicInterface().isPlaying() ? 1 : 0;
        if (a == 1){
            mtvCountTime.setText(Utils.getShowTime(progress));
            mtvSurplusTime.setText("-" + Utils.getShowTime(mDuration - progress));
            csbRound.setProgress(progress);
        }
        if (mIsUpdateImg != a) {
            mIsUpdateImg = a;
            ivPlay.setImageResource(getMusicInterface().isPlaying() ?
                    R.mipmap.ic_music_pause_mini :
                    R.mipmap.ic_home_play_btn);
            ivSbPlay.setImageResource(getMusicInterface().isPlaying() ?
                    R.mipmap.ic_music_pause :
                    R.mipmap.ic_music_play);
        }
    }

    private void initPlayUi(Song song) {
        mDuration = song.getFormatDuration();
        Utils.displayImage(getActivity(), song.getUri(), ivCoverBg, Utils.getImageOptions(R.mipmap.
                ic_music_back));
        tvMusicName.setText(song.getName());
        tvMusicPlayer.setText(song.getSinger());
        csbRound.setMax(song.getFormatDuration());

    }

    private MusicInterface getMusicInterface() {
        return ((MainActivity) getActivity()).musicInterface;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        presenter.onCheckedChanged(compoundButton.getId(), b, getMusicInterface());
    }

    @Subscribe
    public void onEventFragmentThread(PlayVolumeEvent event) {
        if (event.isVolume()) {
            mVolume = event.getVolume();
            ilVoice.setSeekBarProgress(mVolume);
            //presenter.operateVolume(mVolume);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return Utils.changeViewColor(v, event, getContext(), new ViewClickListener() {
            @Override
            public void getViewId(int id) {
                presenter.dealOnClick(id, mPosition, getMusicInterface());
            }
        });
    }

    public void onKeyDown(int volume) {
        mVolume = volume;
        ilVoice.setSeekBarProgress(mVolume);
    }
}
