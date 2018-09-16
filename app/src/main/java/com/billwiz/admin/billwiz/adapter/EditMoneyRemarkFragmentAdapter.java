package com.billwiz.admin.billwiz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.billwiz.admin.billwiz.fragment.EditMoneyFragment;
import com.billwiz.admin.billwiz.fragment.EditRemarkFragment;


public class EditMoneyRemarkFragmentAdapter extends FragmentPagerAdapter {

    private int type;

    public EditMoneyRemarkFragmentAdapter(FragmentManager fm, int type) {
        super(fm);
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return EditMoneyFragment.newInstance(0, type);
        else return EditRemarkFragment.newInstance(1, type);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
