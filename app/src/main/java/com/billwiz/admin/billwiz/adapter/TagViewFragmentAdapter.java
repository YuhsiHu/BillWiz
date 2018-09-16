package com.billwiz.admin.billwiz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.billwiz.admin.billwiz.Activity.BillWizApplication;
import com.billwiz.admin.billwiz.fragment.TagViewFragment;
import com.billwiz.admin.billwiz.model.RecordManager;
import com.billwiz.admin.billwiz.util.BillWizUtil;


public class TagViewFragmentAdapter extends FragmentStatePagerAdapter {

    public TagViewFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return TagViewFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return RecordManager.getInstance(BillWizApplication.getAppContext()).TAGS.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return BillWizUtil.GetTagName(
                RecordManager.getInstance(BillWizApplication.getAppContext()).TAGS.get(position % RecordManager.TAGS.size()).getId());
    }
}
