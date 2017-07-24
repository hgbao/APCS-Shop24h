package com.hgbao.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hgbao.fragment.FragmentTabPersonal;
import com.hgbao.fragment.FragmentTabSearch;

public class AdapterPagerSearch extends FragmentStatePagerAdapter {
    FragmentTabSearch fragment;

    public AdapterPagerSearch(FragmentManager fm) {
        super(fm);
    }

    public FragmentTabSearch getFragment(){
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = FragmentTabSearch.newInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Thương hiệu";
        return "Khuyến mãi";
    }
}
