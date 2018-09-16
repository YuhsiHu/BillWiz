package com.billwiz.admin.billwiz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.billwiz.admin.billwiz.fragment.CustomViewFragment;



// Todo optimize this

public class CustomViewFragmentAdapter extends FragmentStatePagerAdapter {

    public CustomViewFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return CustomViewFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
