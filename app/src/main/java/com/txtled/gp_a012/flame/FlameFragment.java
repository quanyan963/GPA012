package com.txtled.gp_a012.flame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.MvpBaseFragment;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.event.FlameEvent;
import com.txtled.gp_a012.flame.mvp.FlameContract;
import com.txtled.gp_a012.flame.mvp.FlamePresenter;
import com.txtled.gp_a012.widget.ItemLayout;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.txtled.gp_a012.utils.Constants.LIGHT;
import static com.txtled.gp_a012.utils.Constants.LIGHT_STATUE;
import static com.txtled.gp_a012.utils.Constants.POWER;
import static com.txtled.gp_a012.utils.Constants.SPEED;
import static com.txtled.gp_a012.utils.Constants.TO_MUSIC;

/**
 * Created by Mr.Quan on 2018/4/20.
 */

public class FlameFragment extends MvpBaseFragment<FlamePresenter> implements FlameContract.View,
        ItemLayout.OnSeekBarListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.il_power)
    ItemLayout ilPower;
    @BindView(R.id.il_light)
    ItemLayout ilLight;
    @BindView(R.id.il_speed)
    ItemLayout ilSpeed;
    @BindView(R.id.switch_view)
    Switch switchView;
    @BindView(R.id.rl_flame_back)
    RelativeLayout rlFlameBack;

    private Flame mFlame;
    private boolean isTouch = true;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flame, null);
    }

    @Override
    public void init() {
        mFlame = presenter.getFlame();
        ilLight.setSeekBarProgress(mFlame.getLight());
        ilPower.setSeekBarProgress(mFlame.getPower() == 1 ? 255 : 0);
        ilSpeed.setSeekBarProgress(mFlame.getSpeed());
        switchView.setChecked(mFlame.getToMusic() == 1 ? true : false);
        changeView(mFlame.getPower());
        ilPower.setOnSeekBarListener(this);
        ilLight.setOnSeekBarListener(this);
        ilSpeed.setOnSeekBarListener(this);
        switchView.setOnCheckedChangeListener(this);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (ilPower.getSeekBar() == seekBar) {
            if (seekBar.getProgress() > 127) {
                seekBar.setProgress(255);
            } else {
                seekBar.setProgress(0);
            }
            presenter.changePower(seekBar.getProgress() > 127 ? 1 : 0, mFlame.getLightStatue());
            //presenter.changePower(seekBar.getProgress(),mFlame.getLightStatue());
        } else if (ilLight.getSeekBar() == seekBar) {
            presenter.changeLight(seekBar.getProgress(), mFlame.getLightStatue());
        } else if (ilSpeed.getSeekBar() == seekBar) {
            presenter.changeSpeed(seekBar.getProgress(), mFlame.getLightStatue());
        }
    }

    @Subscribe
    public void onFragmentFlameEvent(FlameEvent flameEvent) {
        switch (flameEvent.getType()) {
            case LIGHT_STATUE:
                if (flameEvent.getLightStatue() == 1) {
                    presenter.sendStatue(mFlame);
                } else {
                    isTouch = false;
                    switchView.setChecked(false);
                    isTouch = true;
                    presenter.closeToMusic();
                }
                break;
            case POWER:
                ilPower.setSeekBarProgress(flameEvent.getPower());
//                if (!flameEvent.isNeedFlush()){
//
//                }
                break;
            case LIGHT:
                ilLight.setSeekBarProgress(flameEvent.getLight());
                break;
            case SPEED:
                ilSpeed.setSeekBarProgress(flameEvent.getSpeed());
                break;
            case TO_MUSIC:
                isTouch = false;
                switchView.setChecked(flameEvent.getToMusic() == 1 ? true : false);
                isTouch = true;
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isTouch) return;
        presenter.setPulseToMusic(isChecked, mFlame.getLightStatue());
    }

    @Override
    public void changeView(int type) {
        switch (type) {
            case 1:
                rlFlameBack.setBackgroundResource(R.mipmap.ic_flameback_big);
                break;
            case 0:
                rlFlameBack.setBackgroundResource(R.mipmap.ic_flameback_small);
                break;
        }
    }
}
