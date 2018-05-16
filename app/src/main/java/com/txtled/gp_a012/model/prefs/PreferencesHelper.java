package com.txtled.gp_a012.model.prefs;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public interface PreferencesHelper {
    int getPlayPosition();

    void setPlayPosition(int position);

    void setIsFirstApp(boolean b);

    boolean getIsFirstApp();

    void setMainVolume(int progress);

    int getMainVolume();

    void setInitDialog(boolean b);

    boolean getInitDialog();

    boolean getIsCycle();

    void setIsCycle(boolean b);

    boolean getIsRandom();

    void setIsRandom(boolean b);
}
