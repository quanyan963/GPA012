package com.txtled.gp_a012.di.component;

import android.app.Activity;


import com.txtled.gp_a012.di.module.FragmentModule;
import com.txtled.gp_a012.di.scope.FragmentScope;
import com.txtled.gp_a012.flame.FlameFragment;
import com.txtled.gp_a012.music.MusicFragment;

import dagger.Component;

/**
 * Created by Mr.Quan on 2018/4/14.
 */

@FragmentScope
@Component(dependencies = AppComponent.class,modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

    void inject(MusicFragment musicFragment);

    void inject(FlameFragment musicFragment);
}
