package com.billwiz.admin.billwiz.util;

import android.graphics.Color;

import com.billwiz.admin.billwiz.Activity.BillWizApplication;
import com.github.johnpersano.supertoasts.SuperToast;


public class BillWizToast {
    private static BillWizToast ourInstance = new BillWizToast();

    public static BillWizToast getInstance() {
        return ourInstance;
    }

    private BillWizToast() {
    }

    public void showToast(int text, int color) {
        SuperToast.cancelAllSuperToasts();
        SuperToast superToast = new SuperToast(BillWizApplication.getAppContext());
        superToast.setAnimations(BillWizUtil.TOAST_ANIMATION);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(BillWizApplication.getAppContext().getResources().getString(text));
        superToast.setBackground(color);
        superToast.getTextView().setTypeface(BillWizUtil.typefaceLatoLight);
        superToast.show();
    }

    public void showToast(String text , int color) {
        SuperToast.cancelAllSuperToasts();
        SuperToast superToast = new SuperToast(BillWizApplication.getAppContext());
        superToast.setAnimations(BillWizUtil.TOAST_ANIMATION);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setText(text);
        superToast.setBackground(color);
        superToast.getTextView().setTypeface(BillWizUtil.typefaceLatoLight);
        superToast.show();

        SuperToast.create(BillWizApplication.getAppContext(), "Hello world!", SuperToast.Duration.LONG).show();
    }
}


