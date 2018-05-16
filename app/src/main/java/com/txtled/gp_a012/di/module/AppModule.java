package com.txtled.gp_a012.di.module;


import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.model.DataManagerModel;
import com.txtled.gp_a012.model.ble.BleHelper;
import com.txtled.gp_a012.model.ble.BleHelperImpl;
import com.txtled.gp_a012.model.db.DBHelper;
import com.txtled.gp_a012.model.db.DBHelperImpl;
import com.txtled.gp_a012.model.operate.OperateHelper;
import com.txtled.gp_a012.model.operate.OperateHelperImpl;
import com.txtled.gp_a012.model.prefs.PreferencesHelper;
import com.txtled.gp_a012.model.prefs.PreferencesHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mr.Quan on 2018/4/13.
 */

@Module
public class AppModule {
    private MyApplication myApplication;

    public AppModule(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Provides
    @Singleton
    MyApplication provideMyApplication() {
        return myApplication;
    }

    @Provides
    @Singleton
    BleHelper provideBLEHelper(BleHelperImpl bleHelper) {
        return bleHelper;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(DBHelperImpl dbHelper) {
        return dbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(PreferencesHelperImpl preferencesHelper) {
        return preferencesHelper;
    }

    @Provides
    @Singleton
    OperateHelper provideOperateHelper(OperateHelperImpl operateHelper) {
        return operateHelper;
    }

    @Provides
    @Singleton
    DataManagerModel provideDataManagerModel(BleHelperImpl bleHelper, DBHelperImpl dbHelper,
                                             PreferencesHelperImpl preferencesHelper,
                                             OperateHelperImpl operateHelper) {
        return new DataManagerModel(bleHelper, dbHelper, preferencesHelper, operateHelper);
    }
}
