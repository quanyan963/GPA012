package com.txtled.gp_a012.start;


import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

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
                finish();
            }
        },1500);

    }

    @Override
    public void onShowError() {
        AlertUtils.showAlertDialog(this, getString(R.string.no_support_ble));
    }

    @Override
    public void onShowOpenBleDialog() {
        Intent intent = new Intent();
        intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQUEST_CODE_OPEN_BLE);
    }

    @Override
    public void onShowOpenGPSDialog() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(locationIntent, REQUEST_CODE_OPEN_GPS);
    }

    @Override
    public void startAnim() {

    }

    @Override
    public void scanFailure() {
        AlertUtils.showAlertDialog(this, getString(R.string.turn_on_device));
        //tvConnState.setText(R.string.turn_on_device);
        connFailure();
    }

    @Override
    public void connFailure() {
//        mAnim.cancel();
//        mAnimatorSet.cancel();
        AlertUtils.showAlertDialog(this, R.string.conn_failure, new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.checkPermission(StartActivity.this);
            }
        });
    }


    @Override
    public void connected() {

    }

    @Override
    public void showOpenDeviceDialog() {
        AlertUtils.showAlertDialog(this, R.layout.view_dialog, new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.checkPermission(false,StartActivity.this);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE_CONN_BLE);
            }
        });
    }

    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (Utils.isLocationEnable(this)) {
                presenter.checkPermission(this);
            } else {
                AlertUtils.showAlertDialog(this, R.string.gps_not_open_hint);
            }
        } else if (requestCode == REQUEST_CODE_OPEN_BLE && resultCode == RESULT_OK) {
            presenter.checkPermission(this);
        } else if (requestCode == REQUEST_CODE_CONN_BLE) {
            presenter.checkPermission(this);
        }
    }
}
