package com.txtled.gp_a012.model.db;


import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.bean.Flame;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.bean.dao.DaoMaster;
import com.txtled.gp_a012.bean.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import javax.inject.Inject;

import static com.txtled.gp_a012.utils.Constants.LIGHT;
import static com.txtled.gp_a012.utils.Constants.LIGHT_STATUE;
import static com.txtled.gp_a012.utils.Constants.POWER;
import static com.txtled.gp_a012.utils.Constants.SPEED;
import static com.txtled.gp_a012.utils.Constants.TO_MUSIC;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class DBHelperImpl implements DBHelper {
    private static final String DB_NAME = "gp012.db";
    private DaoSession mDaoSession;

    @Inject
    public DBHelperImpl() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(MyApplication.
                getInstance(), DB_NAME);
        Database db = openHelper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    @Override
    public void insertMusic(Song song) {
        mDaoSession.getSongDao().insert(song);
    }

    @Override
    public void deleteAllSong() {
        mDaoSession.getSongDao().deleteAll();
    }

    @Override
    public List<Song> getMusicInfoList() {
        return mDaoSession.getSongDao().loadAll();
    }

    @Override
    public void insertFlame(Flame flame) {
        mDaoSession.getFlameDao().insert(flame);
    }

    @Override
    public void updateFlame(String type, int value) {
        Flame flame = getFlame();
        switch (type){
            case LIGHT_STATUE:
                flame.setLightStatue(value);
                mDaoSession.getFlameDao().update(flame);
                break;
            case LIGHT:
                flame.setLight(value);
                mDaoSession.getFlameDao().update(flame);
                break;
            case POWER:
                flame.setPower(value);
                mDaoSession.getFlameDao().update(flame);
                break;
            case SPEED:
                flame.setSpeed(value);
                mDaoSession.getFlameDao().update(flame);
                break;
            case TO_MUSIC:
                flame.setToMusic(value);
                mDaoSession.getFlameDao().update(flame);
                break;
        }
    }

    @Override
    public Flame getFlame() {
        return mDaoSession.getFlameDao().loadAll().get(0);
    }
}
