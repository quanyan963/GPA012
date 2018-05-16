package com.txtled.gp_a012.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;


public class CustomRButton extends AppCompatRadioButton {

    public CustomRButton(Context context) {
        super(context);
        init(0);
    }

    public CustomRButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(0);
    }

    public CustomRButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(defStyleAttr);
    }

    private void init(int defStyleAttr) {

        //设置字体
        if (isInEditMode()) return;
        String fontName = "Montserrat_Light.ttf";
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                "fonts/" + fontName), defStyleAttr);
    }

}