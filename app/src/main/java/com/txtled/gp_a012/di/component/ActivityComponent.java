package com.txtled.gp_a012.di.component;

import android.app.Activity;


import com.txtled.gp_a012.di.module.ActivityModule;
import com.txtled.gp_a012.di.scope.ActivityScope;
import com.txtled.gp_a012.main.MainActivity;
import com.txtled.gp_a012.menu.MainMenuActivity;
import com.txtled.gp_a012.start.StartActivity;

import dagger.Component;

/**
 * Created by Mr.Quan on 2018/4/13.
 */

@ActivityScope
@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

    void inject(MainActivity mainActivity);

    void inject(MainMenuActivity mainActivity);

    void inject(StartActivity startActivity);
}
