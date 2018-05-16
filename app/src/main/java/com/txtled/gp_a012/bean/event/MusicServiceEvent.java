package com.txtled.gp_a012.bean.event;

/**
 * Created by KomoriWu
 * on 2017-09-22.
 */

public class MusicServiceEvent {
    private int progress;
    private int position;
    private boolean isPlaying;

    public MusicServiceEvent(int progress, int position, boolean isPlaying) {
        this.progress = progress;
        this.position = position;
        this.isPlaying = isPlaying;
    }

    public MusicServiceEvent(int progress, int position) {
        this.progress = progress;
        this.position = position;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getProgress() {
        return progress;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
