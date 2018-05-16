package com.txtled.gp_a012.music;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.txtled.gp_a012.R;
import com.txtled.gp_a012.application.MyApplication;
import com.txtled.gp_a012.bean.Song;
import com.txtled.gp_a012.utils.RxUtil;
import com.txtled.gp_a012.widget.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by KomoriWu on 2017/9/21.
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MusicViewHolder> {

    private List<Song> mList;
    private Context mContext;
    private MusicClickListener listener;
    private List<Boolean> mSelectedList;

    public MusicListAdapter(Context mContext) {
        this.mContext = mContext;
        mSelectedList = new ArrayList<>();
    }

    public void addList(boolean playing, List<Song> mList, RecyclerView recyclerView) {
        this.mList = mList;
        for (int i = 0; i < mList.size(); i++) {
            int position = MyApplication.getAppComponent().getPreferencesHelper().getPlayPosition();
            mSelectedList.add(i, playing && i == position);
            recyclerView.scrollToPosition(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_music_list, null);
        return new MusicViewHolder(view);
    }

    private void setSelected(final int position) {
        Flowable.timer(150, TimeUnit.MILLISECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        for (int i = 0; i < mList.size(); i++) {
                            mSelectedList.add(i, i == position);
                        }
                        notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, final int position) {
        holder.mtvSong.setText(mList.get(position).getName());
        holder.mtvSinger.setText(mList.get(position).getSinger());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
                setSelected(position);
            }
        });
        //holder.ivPlaying.setVisibility(mSelectedList.get(position) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mtv_song)
        CustomTextView mtvSong;
        @BindView(R.id.mtv_singer)
        CustomTextView mtvSinger;
//        @BindView(R.id.iv_playing)
//        ImageView ivPlaying;

        public MusicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void setOnClickListener(MusicClickListener listener) {
        this.listener = listener;
    }

    public interface MusicClickListener {
        void onItemClick(int position);
    }
}
