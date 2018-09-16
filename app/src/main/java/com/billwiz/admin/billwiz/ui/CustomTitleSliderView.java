package com.billwiz.admin.billwiz.ui;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.Activity.BillWizApplication;
import com.billwiz.admin.billwiz.fragment.BillWizFragmentManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;


public class CustomTitleSliderView extends BaseSliderView {
    private static Typeface font = null;
    private String content;
    private int type;
    private TextView title;

    public CustomTitleSliderView(String content, int type) {
        super(BillWizApplication.getAppContext());
        this.content = content;
        this.type = type;
        if (type == BillWizFragmentManager.NUMBER_SLIDER) {
            BillWizFragmentManager.numberCustomTitleSliderView = this;
        } else if (type == BillWizFragmentManager.EXPENSE_SLIDER) {
            BillWizFragmentManager.expenseCustomTitleSliderView = this;
        }
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_title_slider_view,null);

        LinearLayout description = (LinearLayout)v.findViewById(R.id.description_layout);
        description.setVisibility(View.GONE);

        title = (TextView)v.findViewById(R.id.title);
        title.setText(content);
        title.setTypeface(BillWizUtil.typefaceLatoLight);

        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return v;
    }

    public void setTitle(String string) {
        title.setText(string);
    }
}
