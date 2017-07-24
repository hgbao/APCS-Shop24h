package com.hgbao.shop24h;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.hgbao.adapter.RecyclerAdapterNearby;
import com.hgbao.adapter.RecyclerAdapterShop;
import com.hgbao.adapter.WrappingLinearLayoutManager;
import com.hgbao.model.Shop;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.SupportProvider;

import java.util.ArrayList;
import java.util.Collections;

public class NearbyActivity extends AppCompatActivity {
    TextView txtPosition;
    RecyclerView recyclerView;
    ArrayList<Shop> list_shop;
    ArrayList<Double> list_distance;

    LatLng position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        double latitude = getIntent().getDoubleExtra("LATITUDE", DataProvider.DEFAULT_LATITUDE);
        double longitude = getIntent().getDoubleExtra("LONGITUDE", DataProvider.DEFAULT_LONGITUDE);
        position = new LatLng(latitude, longitude);

        addControl();
        addEvent();
    }

    private void addControl(){
        txtPosition = (TextView) findViewById(R.id.txtNearbyPosition);
        String current = SupportProvider.getCurrentAddress(NearbyActivity.this, position);
        if (current.isEmpty()) {
            if (position.latitude == DataProvider.DEFAULT_LATITUDE && position.longitude == DataProvider.DEFAULT_LONGITUDE)
                txtPosition.setText("Vị trí: 227C Nguyễn Văn Cừ, Quận 5, Tp. Hồ Chí Minh");
            else
                txtPosition.setText("Tọa độ: " + position.latitude + "," + position.longitude);
        }
        else
            txtPosition.setText("Vị trí: " + current);
        //Recycler view
        list_shop = new ArrayList<>();
        list_distance = new ArrayList<>();
        for (int i = 0; i < DataProvider.list_company.size(); i++){
            list_shop.addAll(DataProvider.list_company.get(i).getList_shop());
        }
        for (int i = 0; i < list_shop.size(); i++){
            Shop shop = list_shop.get(i);
            list_distance.add(SupportProvider.getDistance(
                    position.latitude, position.longitude,
                    shop.getLatitude(), shop.getLongitude()));
        }
        for (int i = 0; i < 10; i++){
            for (int j = i + 1; j < list_shop.size(); j++){
                if (list_distance.get(i) > list_distance.get(j)){
                    double tmpDistance = list_distance.get(i);
                    list_distance.set(i, list_distance.get(j));
                    list_distance.set(j, tmpDistance);

                    Shop tmpShop = list_shop.get(i);
                    list_shop.set(i, list_shop.get(j));
                    list_shop.set(j, tmpShop);
                }
            }
        }
        list_shop = new ArrayList<>(list_shop.subList(0, 9));

        //Recycler view
        recyclerView = (RecyclerView) findViewById(R.id.rvListNearby);
        recyclerView.setAdapter(new RecyclerAdapterNearby(NearbyActivity.this, list_shop, list_distance));
        recyclerView.setLayoutManager(new LinearLayoutManager(NearbyActivity.this));
    }

    private void addEvent(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nearby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
