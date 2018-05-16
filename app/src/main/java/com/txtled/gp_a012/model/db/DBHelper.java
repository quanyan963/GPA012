package com.txtled.gp_a012.model.db;


import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;

import java.util.List;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public interface DBHelper {
    void insertMusic(Song song);

    void deleteAllSong();

    List<Song> getMusicInfoList();

    void insertFlame(Flame flame);

    void updateFlame(String type, int value);

    Flame getFlame();
}
