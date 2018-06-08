package com.txtled.gp_a012.start;


import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.txtled.gp_a012.R;
import com.txtled.gp_a012.base.MvpBaseActivity;
import com.txtled.gp_a012.menu.MainMenuActivity;
import com.txtled.gp_a012.start.mvp.StartContract;
import com.txtled.gp_a012.start.mvp.StartPresenter;
import com.txtled.gp_a012.utils.AlertUtils;
import com.txtled.gp_a012.utils.Utils;

import java.util.Timer;
import java.util.logging.Handler;

/**
 * Created by Mr.Quan on 2018/4/14.
 */

public class StartActivity extends MvpBaseActivity<StartPresenter> implements StartContract.View {
    private static final int REQUEST_CODE_OPEN_BLE = 2;
    private static final int REQUEST_CODE_OPEN_GPS = 3;
    private static final int REQUEST_CODE_CONN_BLE = 4;
    @Override
    public void toMainView() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainMenuActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        },1500);

    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.black));
                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //presenter.checkPermission(this);
        toMainView();

    }

    @Override
    public int getLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void bleListener() {

    }
}
