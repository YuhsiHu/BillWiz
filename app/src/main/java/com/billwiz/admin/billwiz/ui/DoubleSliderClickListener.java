package com.billwiz.admin.billwiz.ui;

import android.view.View;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;


public abstract class DoubleSliderClickListener implements BaseSliderView.OnSliderClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

    long lastClickTime = 0;

    public void onSliderClick(BaseSliderView v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            onDoubleClick(v);
        } else {
            onSingleClick(v);
        }
        lastClickTime = clickTime;
    }

    public abstract void onSingleClick(BaseSliderView v);
    public abstract void onDoubleClick(BaseSliderView v);
}
