package com.hgbao.thread;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgbao.model.Company;
import com.hgbao.model.Shop;
import com.hgbao.provider.DataProvider;

import java.io.ByteArrayInputStream;

public class TaskDrawMarker extends AsyncTask<Void, MarkerOptions, Void>{
    Activity context;
    GoogleMap map;

    public TaskDrawMarker(Activity context, GoogleMap map) {
        this.context = context;
        this.map = map;
    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i = 0; i < DataProvider.list_company.size(); i++){
            Company company = DataProvider.list_company.get(i);
            for (int j = 0; j < company.getList_shop().size(); j++){
                Shop shop = company.getList_shop().get(j);
                LatLng position = new LatLng(shop.getLatitude(), shop.getLongitude());
                //Create marker
                MarkerOptions option = new MarkerOptions();
                option.position(position);
                option.icon(BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeStream(new ByteArrayInputStream(company.getMarker()))));
                option.snippet(i + " " + j);
                publishProgress(option);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(MarkerOptions... values) {
        super.onProgressUpdate(values);
        if (map != null) {
            Marker marker = map.addMarker(values[0]);
            marker.hideInfoWindow();
        }
    }
}
