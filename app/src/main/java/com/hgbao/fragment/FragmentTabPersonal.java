package com.hgbao.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hgbao.adapter.RecyclerAdapterCompany;
import com.hgbao.adapter.RecyclerAdapterNotification;
import com.hgbao.adapter.RecyclerAdapterPromotion;
import com.hgbao.adapter.RecyclerAdapterShop;
import com.hgbao.model.Promotion;
import com.hgbao.model.Shop;
import com.hgbao.provider.DataProvider;

import java.util.ArrayList;

public class FragmentTabPersonal extends Fragment {
    RecyclerView recyclerView;
    int tab_number;
    RecyclerAdapterNotification adapter_notification;
    RecyclerAdapterShop adapter_shop;

    public static FragmentTabPersonal newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(DataProvider.EXTRA_TAB, page);
        FragmentTabPersonal fragment = new FragmentTabPersonal();
        fragment.setArguments(args);
        return fragment;
    }

    public void update(){
        adapter_notification.notifyDataSetChanged();
        adapter_shop.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab_number = getArguments().getInt(DataProvider.EXTRA_TAB);
        //Data
        adapter_notification = new RecyclerAdapterNotification(getActivity(), DataProvider.list_notification);
        adapter_shop = new RecyclerAdapterShop(getActivity(), DataProvider.list_favourite);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());
        if (tab_number == 0)
            recyclerView.setAdapter(adapter_notification);
        else
            recyclerView.setAdapter(adapter_shop);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }
}
