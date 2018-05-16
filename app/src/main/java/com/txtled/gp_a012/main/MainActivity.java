package com.txtled.gp_a012.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.MvpBaseActivity;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.bean.event.FlameEvent;
import com.txtled.gp_a012.bean.event.MusicServiceEvent;
import com.txtled.gp_a012.bean.event.PlayVolumeEvent;
import com.txtled.gp_a012.flame.FlameFragment;
import com.txtled.gp_a012.main.mvp.MainContract;
import com.txtled.gp_a012.main.mvp.MainPresenter;
import com.txtled.gp_a012.music.MusicFragment;
import com.txtled.gp_a012.music.service.MusicInterface;
import com.txtled.gp_a012.music.service.MusicService;
import com.txtled.gp_a012.utils.AlertUtils;
import com.txtled.gp_a012.utils.Constants;
import com.txtled.gp_a012.utils.Utils;
import com.txtled.gp_a012.widget.CustomButton;
import com.txtled.gp_a012.widget.CustomRButton;
import com.txtled.gp_a012.widget.CustomTextView;
import com.txtled.gp_a012.widget.ItemLayout;
import com.txtled.gp_a012.widget.listener.MusicListListener;
import com.txtled.gp_a012.widget.listener.ViewClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.txtled.gp_a012.utils.Constants.LIGHT_STATUE;

public class MainActivity extends MvpBaseActivity<MainPresenter> implements MainContract.View ,
    View.OnClickListener,RadioGroup.OnCheckedChangeListener,View.OnTouchListener,
        CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.tv_title)
    CustomTextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_save)
    CustomTextView tvSave;
    @BindView(R.id._check)
    CheckBox Check;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_music_name)
    CustomTextView tvMusicName;
    @BindView(R.id.tv_music_player)
    CustomTextView tvMusicPlayer;
    @BindView(R.id.iv_list)
    ImageView ivList;
    @BindView(R.id.rl_player)
    RelativeLayout rlPlayer;
    @BindView(R.id.layout_top)
    LinearLayout layoutTop;
    @BindView(R.id.frame_content)
    FrameLayout frameContent;
    @BindView(R.id.layout_clock)
    RelativeLayout layoutClock;
    @BindView(R.id.rb_flame)
    CustomRButton rbLight;
    @BindView(R.id.rb_setting)
    CustomButton rbSetting;
    @BindView(R.id.rb_music)
    CustomRButton rbMusic;
    @BindView(R.id.radio_group_bottom)
    RadioGroup radioGroupBottom;
    @BindView(R.id.layout_radio_group)
    LinearLayout layoutRadioGroup;
    @BindView(R.id.nav)
    NavigationView nav;
    @BindView(R.id.dl_main)
    DrawerLayout dlMain;

    private int rb_id;
    private Fragment mCurrentFragment;
    private MusicFragment mMusicFragment;
    private FlameFragment mFlameFragment;
    private float radioValue;
    private ImageView mHeadBack;
    private ItemLayout mIlInstruction;
    private ItemLayout mIlAbout;
    private int mPosition = -1;
    private List<Song> mSongList;
    public MusicInterface musicInterface;
    private Intent mIntent;
    private MyServiceConn mServiceConn;
    private int mIsUpdateImg = -1;
    private int mFragmentPage;
    private int mVolume;
    private boolean isReq;
    private Flame mFlame;
    @Override
    public void init() {
        initToolbar();
        initService();
        mVolume = presenter.getVolume();
        mFlame = presenter.getFlame();
        isReq = true;
        Check.setChecked(mFlame.getLightStatue() == 1 ? true : false);
        isReq = false;
        setNavigationIcon(true);
        rb_id = getIntent().getIntExtra(Constants.RB_ID, 0);
        mCurrentFragment = new MusicFragment();
        View head = nav.getHeaderView(0);
        mHeadBack = (ImageView) head.findViewById(R.id.head_back);
        mIlInstruction = (ItemLayout) head.findViewById(R.id.il_instruction);
        mIlAbout = (ItemLayout) head.findViewById(R.id.il_about);
        initListener();
        radioGroupBottom.check(rb_id);
        mSongList = presenter.getSongList();
        mPosition = presenter.getPlayPosition();
        if (mSongList.size() > mPosition) {
            initPlayUi(mSongList.get(mPosition));
        }else {
            mPosition = -1;
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void bleListener() {
    }

    private void initListener() {
        radioGroupBottom.setOnCheckedChangeListener(this);
        mIlInstruction.setOnClickListener(this);
        mIlAbout.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        Check.setOnCheckedChangeListener(this);
        rbSetting.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);
    }

    public void initService() {
        mServiceConn = new MyServiceConn();
        mIntent = new Intent(this, MusicService.class);
        //startService(mIntent);
        bindService(mIntent, mServiceConn, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return Utils.changeViewColor(v, event, this, new ViewClickListener() {
            @Override
            public void getViewId(int id) {
                presenter.onClick(id,rb_id);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isReq)
            return;
        presenter.checkChanged(isChecked);
    }

    private class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicInterface = (MusicInterface) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        presenter.switchNavView(checkedId);
    }

    @Override
    public void switchFlameView() {
        tvTitle.setText(R.string.flame);
        initToolbarRight(R.string.flame);



        if (mFlameFragment == null) {
            mFlameFragment = new FlameFragment();
        }
        switchContent(mCurrentFragment, mFlameFragment);
    }

    private void initToolbarRight(int id) {
        switch (id){
            case R.string.flame:
                ivRight.setVisibility(View.GONE);
                rlPlayer.setVisibility(VISIBLE);
                Check.setVisibility(VISIBLE);
                break;
            case R.string.music:
                rlPlayer.setVisibility(View.GONE);
                ivRight.setVisibility(VISIBLE);
                Check.setVisibility(View.GONE);
        }
    }

    @Override
    public void switchMusicView() {
        tvTitle.setText(R.string.music);
        initToolbarRight(R.string.music);
        if (mMusicFragment == null) {
            mMusicFragment = new MusicFragment();
        }
        switchContent(mCurrentFragment, mMusicFragment);
        //dlMain.setBackgroundResource(R.mipmap.ic_home_back);
    }

    @Override
    public void showDrawerOpen() {
        if (dlMain.isDrawerOpen(nav)) {
            dlMain.closeDrawer(nav);
        } else {
            dlMain.openDrawer(nav);
        }
    }

    public void switchContent(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(from).add(R.id.frame_content, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commit();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            mVolume = Utils.getSoundValue(mAudioManager.
                    getStreamVolume(AudioManager.STREAM_MUSIC)+1,everyValue);
            mVolume = mVolume == 17 ? 16 : mVolume;
            EventBus.getDefault().post(new PlayVolumeEvent(mVolume));
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    Math.round(mVolume * everyValue),0);
            presenter.volumeChange(mVolume);
        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            mVolume = Utils.getSoundValue(mAudioManager.
                    getStreamVolume(AudioManager.STREAM_MUSIC)-1,everyValue);
            mVolume = mVolume == -1 ? 0 : mVolume;
            EventBus.getDefault().post(new PlayVolumeEvent(mVolume));
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    Math.round(mVolume * everyValue),0);
            presenter.volumeChange(mVolume);
        }
        if (mCurrentFragment instanceof MusicFragment){
            if (keyCode == KeyEvent.KEYCODE_BACK){
                return super.onKeyDown(keyCode, event);
            }else {
                ((MusicFragment)mCurrentFragment).onKeyDown(mVolume);
                return true;
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBack) {
            if (dlMain.isDrawerOpen(nav)){
                dlMain.closeDrawer(nav);
            }else {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.screen_left_in,R.anim.screen_right_out);
                super.onBackPressed();
            }
        }
//        else {
//            if (mFragmentPage == WEEK_SPA) {
//                switchSpaSoundsView();
//            } else if (mFragmentPage == FIRST_ALARM || mFragmentPage == SECOND_ALARM) {
//                switchAlarmView();
//            } else if (mFragmentPage == RADIO_STATION) {
//                switchRadioView();
//            }
//        }
    }

    @Override
    @OnClick({R.id.iv_play, R.id.iv_list})
    public void onClick(View v) {
        presenter.onClick(v.getId(),rb_id);
    }

    @Override
    public void playMusic() {
        if (mPosition == -1){
            AlertUtils.showSnackbar(this,dlMain,R.string.there_was_no_playable_song);
        }else {
            musicInterface.pausePlay(mPosition);
        }

    }

    @Override
    public void showPopMusicList(List<Song> musicInfoList) {
        if (mPosition == -1){
            AlertUtils.showSnackbar(this,dlMain,R.string.there_was_no_playable_song);
        }else {
            Utils.showBottomDialog(this, musicInterface.isPlaying(), musicInfoList, new
                    MusicListListener() {
                        @Override
                        public void onItemSelected(int position) {
                            musicInterface.play(position);
                        }
                    });
        }
    }

    @Override
    public void onEventMainThread(MusicServiceEvent event) {
        super.onEventMainThread(event);
        if (mPosition != event.getPosition()) {
            mPosition = event.getPosition();
            Song song = mSongList.get(mPosition);
            initPlayUi(song);
        }
        int a = musicInterface.isPlaying() ? 1 : 0;
        if (mIsUpdateImg != a) {
            mIsUpdateImg = a;
            ivPlay.setImageResource(musicInterface.isPlaying() ? R.mipmap.ic_music_pause_mini :
                    R.mipmap.ic_home_play_btn);
        }
    }

    @Subscribe
    public void onEventFragmentThread(PlayVolumeEvent event) {
        if (!event.isVolume() && !event.getMusicState() && musicInterface.isPlaying()) {
            musicInterface.pausePlay(-1);
        }
        if (event.isVolume()){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    Math.round(mVolume * everyValue), 0);
        }
    }

    @Override
    public void onEventFlameThread(FlameEvent flameEvent) {
        super.onEventFlameThread(flameEvent);
        if (flameEvent.getType().equals(LIGHT_STATUE)){
            //开关灯
            isReq = true;
            Check.setChecked(flameEvent.getLightStatue() == 0 ? false : true);
            isReq = false;
        }
    }

    private void initPlayUi(Song song) {
        tvMusicName.setText(song.getName());
        tvMusicPlayer.setText(song.getSinger());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConn != null) {
            unbindService(mServiceConn);
            stopService(mIntent);
        }
    }

    public int getFragmentPage() {
        return mFragmentPage;
    }

    public void setFragmentPage(int mFragmentPage) {
        this.mFragmentPage = mFragmentPage;
    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }
}
