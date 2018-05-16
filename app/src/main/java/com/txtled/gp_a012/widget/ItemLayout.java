package com.txtled.gp_a012.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.txtled.gp_a012.R;
import com.txtled.gp_a012.utils.Utils;
import com.txtled.gp_a012.widget.listener.ViewClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017/9/19.
 */


public class ItemLayout extends RelativeLayout {

    @BindView(R.id.iv_item_left)
    ImageView ivLeft;
    @BindView(R.id.tv_left)
    CustomTextView tvLeft;
    @BindView(R.id.layout_left)
    RelativeLayout layoutLeft;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.tv_right)
    CustomTextView tvRight;
    @BindView(R.id.iv_item_right)
    ImageView ivRight;
    @BindView(R.id.layout_item)
    RelativeLayout layoutItem;
    private Context mContext;
    private OnItemListener mOnItemListener;
    private OnTvRightListener mOnTvRightListener;
    private OnIvLeftListener mOnIvLeftListener;
    private OnSeekBarListener mOnSeekBarListener;


    public void setOnItemListener(OnItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
        initItemListener();
    }

    public void setOnIvLeftListener(OnIvLeftListener mOnIvLeftListener) {
        this.mOnIvLeftListener = mOnIvLeftListener;
        initLeftIvListener();
        initLeftTouchListener();
    }

    public void setOnTvRightListener(OnTvRightListener mOnTvRightListener) {
        this.mOnTvRightListener = mOnTvRightListener;
        initRightTvListener();
    }

    public void setOnAllRightListener(OnTvRightListener mOnTvRightListener) {
        this.mOnTvRightListener = mOnTvRightListener;
        initAllRightListener();
    }

    private void initAllRightListener() {
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTvRightListener != null) {
                    mOnTvRightListener.onClickTvRightListener(v);
                }
            }
        });
        ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTvRightListener != null) {
                    mOnTvRightListener.onClickTvRightListener(v);
                }
            }
        });
    }

    public void setOnSeekBarListener(OnSeekBarListener mOnSeekBarListener) {
        this.mOnSeekBarListener = mOnSeekBarListener;
        initSeekBarListener(false);
    }

    public void setOnSeekBarListener(boolean isOpenSbChange, OnSeekBarListener mOnSeekBarListener) {
        this.mOnSeekBarListener = mOnSeekBarListener;
        initSeekBarListener(isOpenSbChange);
    }

    public interface OnItemListener {
        void onClickItemListener(View v);
    }

    public interface OnIvLeftListener {
        void onClickIvLeftListener(View view);
    }

    public interface OnTvRightListener {
        void onClickTvRightListener(View view);
    }

    public interface OnSeekBarListener {
        void onStopTrackingTouch(SeekBar seekBar);
    }


    public ItemLayout(Context context) {
        this(context, null);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Drawable drawableLeft = null;
        Drawable drawableRight = null;
        String strLeft = "";
        String strRight = "";
        boolean isShowLineTop = true;
        boolean isShowLineBottom = false;
        boolean isShowSeekBar = false;
        Drawable sbThumb = null;
        Drawable sbProgressDrawable = null;
        int sbMax = 255;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ItemLayout,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ItemLayout_ivLeft:
                    drawableLeft = a.getDrawable(attr);
                    break;
                case R.styleable.ItemLayout_textLeft:
                    strLeft = a.getString(attr);
                    break;
                case R.styleable.ItemLayout_textRight:
                    strRight = a.getString(attr);
                    break;
                case R.styleable.ItemLayout_ivRight:
                    drawableRight = a.getDrawable(attr);
                    break;
                case R.styleable.ItemLayout_viewLineTop:
                    isShowLineTop = a.getBoolean(attr, true);
                    break;
                case R.styleable.ItemLayout_viewLineBottom:
                    isShowLineBottom = a.getBoolean(attr, false);
                    break;
                case R.styleable.ItemLayout_showSeekBar:
                    isShowSeekBar = a.getBoolean(attr, false);
                    break;
                case R.styleable.ItemLayout_sbThumb:
                    sbThumb = a.getDrawable(attr);
                    break;
                case R.styleable.ItemLayout_sbProgressDrawable:
                    sbProgressDrawable = a.getDrawable(attr);
                    break;
                case R.styleable.ItemLayout_sbMax:
                    sbMax = a.getInt(attr, 255);
                    break;
            }

        }
        a.recycle();
        init(context);
        if (drawableLeft == null) {
            ivLeft.setVisibility(GONE);
        } else {
            ivLeft.setVisibility(VISIBLE);
            ivLeft.setImageDrawable(drawableLeft);
        }

        if (TextUtils.isEmpty(strLeft)) {
            tvLeft.setVisibility(GONE);
        } else {
            tvLeft.setVisibility(VISIBLE);
            tvLeft.setText(strLeft);
        }

        if (TextUtils.isEmpty(strRight)) {
            tvRight.setVisibility(GONE);
        } else {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(strRight);
        }
        if (drawableRight == null) {
            ivRight.setVisibility(INVISIBLE);
        } else {
            ivRight.setVisibility(VISIBLE);
            ivRight.setImageDrawable(drawableRight);
        }

//        viewLineTop.setVisibility(isShowLineTop ? VISIBLE : GONE);
//        viewLineBottom.setVisibility(isShowLineBottom ? VISIBLE : GONE);

        seekBar.setVisibility(isShowSeekBar ? VISIBLE : GONE);
        if (seekBar.getVisibility() == VISIBLE) {
            if (sbThumb == null && sbProgressDrawable == null) {
                seekBar.setThumb(getResources().getDrawable(R.mipmap.ic_music_slider_circle));
                seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seek_bar_red));
            } else {
                seekBar.setThumb(sbThumb);
                seekBar.setProgressDrawable(sbProgressDrawable);
            }
            seekBar.setMax(sbMax);
        }
    }

    public SeekBar getSeekBar(){
        return seekBar;
    }

    public void setTopAndBottomVisibility(boolean top, boolean bottom) {
//        viewLineTop.setVisibility(top ? VISIBLE : GONE);
//        viewLineBottom.setVisibility(bottom ? VISIBLE : GONE);
    }

    public String getTvLeftStr() {
        return tvLeft.getText().toString();
    }

    public String getTvRightStr() {
        return tvRight.getText().toString();
    }

    public void setTvLeftStr(String leftStr) {
        if (!leftStr.isEmpty()) {
            tvLeft.setVisibility(VISIBLE);
            tvLeft.setText(leftStr);
        }
    }

    public void setIvLeft(int drawableId) {
        ivLeft.setImageDrawable(getResources().getDrawable(drawableId));
    }

    public void setTvRightStr(String rightStr) {
        if (!rightStr.isEmpty()) {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(rightStr);
        }
    }

    public void init(Context c) {
        this.mContext = c;
        LayoutInflater.from(mContext).inflate(R.layout.item_layout, this, true);
        ButterKnife.bind(this);
    }

    private void initItemListener() {
        layoutItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemListener != null) {
                    ivRight.setFocusable(true);
                    mOnItemListener.onClickItemListener(v);
                }
            }
        });
    }

    private void initLeftIvListener() {
        ivLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnIvLeftListener != null) {
                    mOnIvLeftListener.onClickIvLeftListener(v);
                }
            }
        });
    }

    private void initLeftTouchListener() {
        ivLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnIvLeftListener != null) {
                    mOnIvLeftListener.onClickIvLeftListener(v);
                }
            }
        });
        ivLeft.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                return Utils.changeViewColor(v, event, getContext(), new ViewClickListener() {
                    @Override
                    public void getViewId(int id) {
                        mOnIvLeftListener.onClickIvLeftListener(v);
                    }
                });
            }
        });
    }

    private void initRightTvListener() {
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTvRightListener != null) {
                    mOnTvRightListener.onClickTvRightListener(v);
                }
            }
        });
    }

    private void initSeekBarListener(final boolean isOpenSbChange) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (isOpenSbChange) {
                    tvLeft.setText(progress + mContext.getString(R.string.percent));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mOnSeekBarListener != null) {
                    mOnSeekBarListener.onStopTrackingTouch(seekBar);
                }
            }
        });
    }

    public void setSeekBarMax(int max) {
        seekBar.setMax(max);
    }

    public void setSeekBarProgress(int progress) {
        seekBar.setProgress(progress);
    }
}
