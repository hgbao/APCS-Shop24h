package com.hgbao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hgbao.fragment.FragmentTabPersonal;
import com.hgbao.fragment.FragmentTabSearch;

public class AdapterPagerPersonal extends FragmentStatePagerAdapter {
    FragmentTabPersonal fragment;

    public AdapterPagerPersonal(FragmentManager fm) {
        super(fm);
    }

    public FragmentTabPersonal getFragment(){
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = FragmentTabPersonal.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Thông báo";
        return "Yêu thích";
    }
}
