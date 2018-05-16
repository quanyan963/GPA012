package com.txtled.gp_a012.music.service;

import android.media.MediaPlayer;

/**
 * Created by KomoriWu
 * on 2017-09-22.
 */


public interface MusicInterface {

    void play(int position);

    void pausePlay(int position);

    void playNext();

    void playPrevious();

    boolean isPlaying();

    void seekTo(int progress);

    void playCycle(boolean state);
    void playRandom(boolean state);

    MediaPlayer getMediaPlayer();

    void initPlayer(int position);

    void initRead();

}
