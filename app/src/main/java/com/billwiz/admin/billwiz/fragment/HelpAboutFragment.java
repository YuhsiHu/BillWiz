package com.billwiz.admin.billwiz.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.billwiz.admin.billwiz.model.BillWiz;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.util.BillWizUtil;


public class HelpAboutFragment extends Fragment {

    private ObservableScrollView mScrollView;

    public static HelpAboutFragment newInstance() {
        HelpAboutFragment fragment = new HelpAboutFragment();
        return fragment;
    }

    private Activity activity;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (Activity)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help_about_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);
        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);

        ((TextView)view.findViewById(R.id.content_0)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_1)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_2)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_3)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_4)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_5)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_6)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_7)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_8)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_9)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_10)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_11)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_12)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_13)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_14)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_15)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_16)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_17)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_18)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_19)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_20)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_21)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_22)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_23)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_24)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_25)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_26)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_27)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_28)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_29)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_30)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_31)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_32)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);
        ((TextView)view.findViewById(R.id.content_33)).setTypeface(BillWizUtil.getInstance().typefaceLatoLight);

        ((MaterialRippleLayout)view.findViewById(R.id.layout_2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/YuhsiHu/BillWiz")));
            }
        });
        ((MaterialRippleLayout)view.findViewById(R.id.layout_3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com")));
            }
        });
        ((MaterialRippleLayout)view.findViewById(R.id.layout_4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillWizUtil.getInstance().copyToClipboard("", mContext);
                BillWizUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.copy_to_clipboard));
            }
        });
    }

}
