package com.txtled.gp_a012.model.operate;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.utils.Constants;
import com.txtled.gp_a012.utils.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public class OperateHelperImpl implements OperateHelper {
    public static final String TAG = OperateHelperImpl.class.getSimpleName();

    @Inject
    public OperateHelperImpl() {

    }

    @Override
    public void requestPermissions(Activity activity, String[] permissions, final
    OnPermissionsListener onPermissionsListener) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEach(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            onPermissionsListener.onSuccess(permission.name);
                            Utils.Logger(TAG,"",permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            onPermissionsListener.onAskAgain();
                            Utils.Logger(TAG,"",permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            onPermissionsListener.onFailure();
                            Utils.Logger(TAG,"",permission.name + " is denied.");
                        }
                    }
                });
    }

    @Override
    public Flowable<ArrayList<Song>> scanMusic(final Activity activity) {
        return Flowable.create(new FlowableOnSubscribe<ArrayList<Song>>() {
            @Override
            public void subscribe(FlowableEmitter<ArrayList<Song>> e) throws Exception {
                Cursor cursor = activity.getContentResolver().query(MediaStore.Audio.Media.
                        EXTERNAL_CONTENT_URI,null,null,
                        null,null);
                if (cursor != null){
                    e.onNext(scan(cursor));
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    private ArrayList<Song> scan(Cursor cursor) {
        ArrayList<Song> musicInfoList = new ArrayList<>();
        if (cursor != null) {
            //Song.deleteAll(Song.class);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Utils.
                        musicMedias[0]));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Utils.
                        musicMedias[1]));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(Utils.
                        musicMedias[2]));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(Utils.
                        musicMedias[3]));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(Utils.
                        musicMedias[4]));
                int albumId = cursor.getInt(cursor.getColumnIndexOrThrow(Utils.
                        musicMedias[5]));
                Uri albumUri = ContentUris.withAppendedId(Uri.parse(
                        Constants.MUSIC_ALBUM_URI), albumId);
                if (duration > Constants.LIMIT_DURATION) {
                    Song musicInfo = new Song(id, title, artist, url,albumUri+"",duration);
                    musicInfoList.add(musicInfo);
                    //musicInfo.save();
                }
            }
        }
        cursor.close();
        return musicInfoList;
    }
}
