package com.txtled.gp_a012.bean.event;


import com.txtled.gp_a012.utils.Constants;

/**
 * Created by KomoriWu
 * on 2017-10-09.
 */

public class PlayVolumeEvent {
    private int flag;
    private boolean isVolume;
    private boolean state;
    private int volume;

    public PlayVolumeEvent(int volume) {
        this.volume = volume;
        this.isVolume = true;
    }

    public PlayVolumeEvent(int flag, boolean state) {
        this.flag = flag;
        this.state = state;
        this.isVolume = false;
    }

    public boolean isVolume() {
        return isVolume;
    }

    public void setVolume(boolean volume) {
        isVolume = volume ;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean getSpaState() {
        if (flag == Constants.FLAG_SPA) {
            return state;
        } else {
            return false;
        }
    }

    public boolean getMusicState() {
        if (flag == Constants.FLAG_MUSIC) {
            return state;
        } else {
            return false;
        }
    }

    public boolean getRadioState() {
        if (flag == Constants.FLAG_RADIO) {
            return state;
        } else {
            return false;
        }
    }
}
