package com.hgbao.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.hgbao.adapter.RecyclerAdapterCompany;
import com.hgbao.adapter.RecyclerAdapterPromotion;
import com.hgbao.adapter.RecyclerAdapterShop;
import com.hgbao.model.Promotion;
import com.hgbao.provider.DataProvider;

import java.util.ArrayList;

public class FragmentTabSearch extends Fragment {
    RecyclerView recyclerView;
    int tab_number;
    RecyclerAdapterCompany adapter_company;
    RecyclerAdapterPromotion adapter_promotion;

    public static FragmentTabSearch newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(DataProvider.EXTRA_TAB, page);
        FragmentTabSearch fragment = new FragmentTabSearch();
        fragment.setArguments(args);
        return fragment;
    }

    public void update(){
        adapter_company.notifyDataSetChanged();
        adapter_promotion.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab_number = getArguments().getInt(DataProvider.EXTRA_TAB);
        //Data
        ArrayList<Promotion> list = new ArrayList<>();
        adapter_company = new RecyclerAdapterCompany(getActivity(), DataProvider.list_company);
        for (int i = 0; i < DataProvider.list_company.size(); i++){
            list.addAll(DataProvider.list_company.get(i).getList_promotion());
        }
        adapter_promotion = new RecyclerAdapterPromotion(getActivity(), list);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());
        if (tab_number == 0)
            recyclerView.setAdapter(adapter_company);
        else
            recyclerView.setAdapter(adapter_promotion);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }
}
