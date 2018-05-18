package com.txtled.gp_a012.menu;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.MvpBaseActivity;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.bean.event.MusicServiceEvent;
import com.txtled.gp_a012.bean.event.PlayVolumeEvent;
import com.txtled.gp_a012.main.MainActivity;
import com.txtled.gp_a012.menu.mvp.MenuContract;
import com.txtled.gp_a012.menu.mvp.MenuPresenter;
import com.txtled.gp_a012.menu.service.ConnBleInterface;
import com.txtled.gp_a012.menu.service.ConnBleService;
import com.txtled.gp_a012.music.service.MusicInterface;
import com.txtled.gp_a012.music.service.MusicService;
import com.txtled.gp_a012.utils.AlertUtils;
import com.txtled.gp_a012.utils.Constants;
import com.txtled.gp_a012.utils.Utils;
import com.txtled.gp_a012.widget.CustomButton;
import com.txtled.gp_a012.widget.CustomTextView;
import com.txtled.gp_a012.widget.ItemLayout;
import com.txtled.gp_a012.widget.listener.MusicListListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Mr.Quan on 2018/4/19.
 */

public class MainMenuActivity extends MvpBaseActivity<MenuPresenter> implements MenuContract.View,
        View.OnClickListener, Observer{
    @BindView(R.id.tv_title)
    CustomTextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_save)
    CustomTextView tvSave;
    @BindView(R.id._check)
    CheckBox Check;
    @BindView(R.id.rb_music)
    CustomButton rbMusic;
    @BindView(R.id.rb_flame)
    CustomButton rbFlame;
    @BindView(R.id.rb_setting)
    CustomButton rbSetting;
//    @BindView(R.id.radio_group_other)
//    RadioGroup radioGroup;
    @BindView(R.id.dl_menu)
    DrawerLayout dlMenu;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_music_name)
    CustomTextView tvMusicName;
    @BindView(R.id.tv_music_player)
    CustomTextView tvMusicPlayer;
    @BindView(R.id.iv_list)
    ImageView ivList;
    @BindView(R.id.nav)
    NavigationView nav;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.rl_player)
    RelativeLayout rlPlayer;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private boolean mIsReturn = false;
    private ImageView mHeadBack;
    private ItemLayout mIlInstruction;
    private ItemLayout mIlAbout;
    public static final int REQ_TO_MAIN_VIEW = 100;
    private List<Song> mSongList;
    private int mPosition = -1;
    private MyServiceConn mServiceConn;
    private MyBleServiceConn mBleServiceConn;
    public MusicInterface musicInterface;
    private ConnBleInterface mConnBleInterface;
    private Intent mIntent;
    private Intent mBleIntent;
    private int mIsUpdateImg = -1;
    private static final int REQUEST_CODE_OPEN_BLE = 2;
    private static final int REQUEST_CODE_OPEN_GPS = 3;
    private static final int REQUEST_CODE_CONN_BLE = 4;
    private boolean isConn;
    private int mVolume;

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main_menu;
    }

    @Override
    public void bleListener() {
//        if (isConn) {
//            presenter.unConn();
//            isConn = false;
//            setBleStatue(isConn);
//            //BluetoothAdapter.getDefaultAdapter().disable();
//        } else {
//            presenter.checkBlePermission(true, this);
//        }
    }

    @Override
    public void init() {
        initToolbar();

        tvTitle.setText(R.string.home);
        setNavigationIcon(false);
        initBleService();
        presenter.checkBlePermission(true, this);

        presenter.checkPermission(this);
        View head = nav.getHeaderView(0);
        mHeadBack = (ImageView) head.findViewById(R.id.head_back);
        mIlInstruction = (ItemLayout) head.findViewById(R.id.il_instruction);
        mIlAbout = (ItemLayout) head.findViewById(R.id.il_about);

        //radioGroup.setOnCheckedChangeListener(this);
        mIlInstruction.setOnClickListener(this);
        mIlAbout.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);
        showSnackBar(dlMenu, R.string.dis_conn);
    }

    private void initBleService() {
        mBleServiceConn = new MyBleServiceConn();
        mBleIntent = new Intent(this, ConnBleService.class);
        startService(mBleIntent);
        bindService(mBleIntent, mBleServiceConn, BIND_AUTO_CREATE);
    }

    public void initService() {
        mServiceConn = new MyServiceConn();
        mIntent = new Intent(this, MusicService.class);
        startService(mIntent);
        bindService(mIntent, mServiceConn, BIND_AUTO_CREATE);
    }

    @Override
    public void update(Observable o, Object arg) {
        String macAddress = (String) arg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toolbar.setNavigationIcon(isConn ? R.mipmap.ic_state_connect : R.mipmap.ic_state_disconnect);
            }
        });
        if (!isConn)
            presenter.checkBlePermission(false, this);
    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        if (mIsReturn) return;
//        presenter.checkChange(checkedId);
//    }

    private class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicInterface = (MusicInterface) iBinder;
            musicInterface.initPlayer(mPosition);
            if (isConn){
                hideSnackBar();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    @Override
    public void toMainView(int flags) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.RB_ID, flags);
        startActivityForResult(intent, REQ_TO_MAIN_VIEW);
        overridePendingTransition(R.anim.screen_left_out, R.anim.screen_right_in);
    }

    @Override
    public void showDrawerOpen() {
        if (dlMenu.isDrawerOpen(nav)) {
            dlMenu.closeDrawer(nav);
        } else {
            dlMenu.openDrawer(nav);
        }
    }

    @Override
    public void showScanSnackBar() {
        mIsReturn = true;
        //showSnackBar(dlMenu, R.string.scan_hint);
    }

    @Override
    public void hideScanSnackBar() {
        //radioGroup.clearCheck();
        mIsReturn = false;
        initService();
        mSongList = presenter.getSongList();
        mPosition = presenter.getPlayPosition();
        if (mSongList.size() > mPosition) {
            initPlayUi(mSongList.get(mPosition));
        } else {
            mPosition = -1;
        }

    }

    @Override
    public void playMusic() {
        if (mPosition == -1) {
            AlertUtils.showSnackbar(this, rlMain, R.string.there_was_no_playable_song);
        } else {
            musicInterface.pausePlay(mPosition);
        }
    }

    private void initPlayUi(Song song) {
        tvMusicName.setText(song.getName());
        tvMusicPlayer.setText(song.getSinger());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_TO_MAIN_VIEW && resultCode == RESULT_OK) {
            mIsReturn = true;
            //radioGroup.clearCheck();
            mIsReturn = false;
        }
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (Utils.isLocationEnable(this)) {
                presenter.checkBlePermission(true, this);
            } else {
                AlertUtils.showAlertDialog(this, R.string.gps_not_open_hint);
            }
        } else if (requestCode == REQUEST_CODE_OPEN_BLE && resultCode == RESULT_OK) {
            presenter.checkBlePermission(true, this);
        } else if (requestCode == REQUEST_CODE_CONN_BLE) {
            presenter.checkBlePermission(true, this);
        }
    }

    @Override
    public void showPopMusicList(List<Song> musicInfoList) {
        if (mPosition == -1) {
            AlertUtils.showSnackbar(this, rlMain, R.string.there_was_no_playable_song);
        } else {
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
    public void onShowError() {
        AlertUtils.showAlertDialog(this, getString(R.string.no_support_ble));
    }

    @Override
    public void onShowOpenBleDialog() {
        Intent intent = new Intent();
        intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_CODE_OPEN_BLE);
    }

    @Override
    public void onShowOpenGPSDialog() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(locationIntent, REQUEST_CODE_OPEN_GPS);
    }

    @Override
    public void scanFailure() {
        AlertUtils.showAlertDialog(this, getString(R.string.turn_on_device));
        //tvConnState.setText(R.string.turn_on_device);
        hideProgress();
        connFailure();
    }

    @Override
    public void connFailure() {
        AlertUtils.showAlertDialog(this, R.string.conn_failure, new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.checkBlePermission(true, MainMenuActivity.this);
            }
        });
    }

    @Override
    public void connected() {
        musicInterface.initRead();
        isConn = true;
        hideSnackBar();
        hideProgress();
        //toolbar.setNavigationIcon(R.mipmap.ic_state_connect);
        //presenter.getBleConnectedStatue();
    }

    @Override
    public void showOpenDeviceDialog() {
        AlertUtils.showAlertDialog(this, R.layout.view_dialog, new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //presenter.checkBlePermission(false, MainMenuActivity.this,true);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE_CONN_BLE);
            }
        });
    }

    @Override
    public void startAnim() {
        isConn = false;
        showProgress();
        //showSnackBar(dlMenu, R.string.scan_ble);
    }

    @Override
    public void setBleStatue(boolean isConn) {
        this.isConn = isConn;
        hideProgress();
        //rlPlayer.setVisibility(isConn ? View.VISIBLE : View.INVISIBLE);
        toolbar.setNavigationIcon(isConn ? R.mipmap.ic_state_connect : R.mipmap.ic_state_disconnect);
        if (!isConn) {
            //toolbar.setNavigationIcon(R.mipmap.ic_state_disconnect);
            showSnackBar(dlMenu, R.string.dis_conn);
            mConnBleInterface.addObserver(MainMenuActivity.this);
            mConnBleInterface.scanBle();

        }else {
            //presenter.getBleConnectedStatue();

        }
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    @OnClick({R.id.rb_music, R.id.rb_flame, R.id.rb_setting, R.id.iv_play, R.id.iv_list})
    public void onClick(View v) {
        if (!isConn)
            return;
        presenter.onClick(v.getId(), mIsReturn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mVolume = Utils.getSoundValue(mAudioManager.
                    getStreamVolume(AudioManager.STREAM_MUSIC) + 1, everyValue);
            if (mVolume == 17)
                mVolume = 16;
            EventBus.getDefault().post(new PlayVolumeEvent(mVolume));
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round(mVolume * everyValue), 0);
            presenter.volumeChange(mVolume);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mVolume = Utils.getSoundValue(mAudioManager.
                    getStreamVolume(AudioManager.STREAM_MUSIC) - 1, everyValue);
            if (mVolume == -1)
                mVolume = 0;
            EventBus.getDefault().post(new PlayVolumeEvent(mVolume));
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round(mVolume * everyValue), 0);
            presenter.volumeChange(mVolume);
            return true;
        } else if (dlMenu.isDrawerOpen(nav)) {
            dlMenu.closeDrawer(nav);
            return false;
        } else {
            return onExitActivity(keyCode, event);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgress();
        if (mServiceConn != null) {
            unbindService(mServiceConn);
            stopService(mIntent);
        }
        if (mBleServiceConn != null){
            unbindService(mBleServiceConn);
            stopService(mBleIntent);
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
            isConn = true;
            hideProgress();
        }
    }

    private class MyBleServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mConnBleInterface = (ConnBleInterface) iBinder;
            mConnBleInterface.scanBle();
            mConnBleInterface.addObserver(MainMenuActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}
