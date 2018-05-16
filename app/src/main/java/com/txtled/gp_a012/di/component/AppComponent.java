package com.txtled.gp_a012.di.component;


import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.di.module.AppModule;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.model.ble.BleHelper;
import com.txtled.gp_a012.model.db.DBHelper;
import com.txtled.gp_a012.model.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Mr.Quan on 2018/4/13.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    MyApplication getContext();

    DataManagerModel getDataManagerModel();

    BleHelper getBleHelper();

    DBHelper getDbHelper();

    PreferencesHelper getPreferencesHelper();
}
