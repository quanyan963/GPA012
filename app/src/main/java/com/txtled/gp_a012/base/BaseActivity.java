package com.txtled.gp_a012.base;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.txtled.gp_a012.R;
import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.bean.event.FlameEvent;
import com.txtled.gp_a012.bean.event.MusicServiceEvent;
import com.txtled.gp_a012.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    public TextView tvTitle;
    public boolean isBack = true;
    private long mExitTime;
    private MyApplication mApplication;
    public Toolbar toolbar;
    public Snackbar snackbar;
    public AudioManager mAudioManager;
    public float everyValue;
    public int maxVolume;

    public abstract void init();

    public abstract int getLayout();

    public abstract void bleListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        everyValue = maxVolume / 16f;
        mApplication = MyApplication.getInstance();
        addActivity();
        onCreateView();
        init();
    }

    public void onCreateView() {

    }


    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            tvTitle = (TextView) findViewById(R.id.tv_title);
            setSupportActionBar(toolbar);
            setTitle("");

            toolbar.setOnMenuItemClickListener(onMenuItemClick);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isBack){
                        onBackPressed();
                    }else {
                        bleListener();
                    }

                }
            });
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void setNavigationIcon(boolean isBack) {
        this.isBack = isBack;
        if (isBack) {
            toolbar.setNavigationIcon(R.mipmap.ic_homebtn);
        } else {

            toolbar.setNavigationIcon(R.mipmap.ic_state_disconnect);
        }

    }

    public Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            OnMenuItemClick(menuItem.getItemId());
            return true;
        }
    };

    public void OnMenuItemClick(int itemId) {

    }

    public boolean onExitActivity(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, R.string.exit_program_hint,
                        Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                removeAllActivity();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public void addActivity() {
        mApplication.addActivity(this);
    }


    public void removeAllActivity() {
        mApplication.removeAllActivity();
    }

    public void showSnackBar(View view, int str) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.gray));
        }
        snackbar.show();
    }

    public void hideSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Subscribe
    public void onEventMainThread(MusicServiceEvent serviceEvent) {
        Utils.Logger(TAG,"Position",serviceEvent.getPosition()+"");
        Utils.Logger(TAG,"Progress",serviceEvent.getProgress()+"");
    }

    @Subscribe
    public void onEventFlameThread(FlameEvent flameEvent) {

    }
}
