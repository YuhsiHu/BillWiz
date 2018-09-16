package com.billwiz.admin.billwiz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.billwiz.admin.billwiz.R;
import com.billwiz.admin.billwiz.Activity.BillWizApplication;
import com.billwiz.admin.billwiz.fragment.HelpAboutFragment;
import com.billwiz.admin.billwiz.fragment.HelpBillWizFragment;
import com.billwiz.admin.billwiz.fragment.HelpFeedbackFragment;


public class HelpFragmentAdapter extends FragmentStatePagerAdapter {

    private int position = 0;

    public HelpFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    public HelpFragmentAdapter(android.support.v4.app.FragmentManager fm, int position) {
        super(fm);
        this.position = position;
    }

    @Override
    public Fragment getItem(int position) {
        switch (this.position) {
            case 0: return HelpBillWizFragment.newInstance();
            case 1: return HelpFeedbackFragment.newInstance();
            case 2: return HelpAboutFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (this.position) {
            case 0: return BillWizApplication.getAppContext().getResources().getString(R.string.app_name);
            case 1: return BillWizApplication.getAppContext().getResources().getString(R.string.feedback);
            case 2: return BillWizApplication.getAppContext().getResources().getString(R.string.about);
        }
        return "";
    }
}
