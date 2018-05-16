package com.txtled.gp_a012.di.module;

import android.app.Activity;


import com.txtled.gp_a012.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mr.Quan on 2018/4/13.
 */

@Module
public class ActivityModule {
    private Activity activity;
    public ActivityModule(Activity activity){
        this.activity = activity;
    }
    @Provides
    @ActivityScope
    Activity provideActivity(){
        return activity;
    }
}
