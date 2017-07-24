package com.hgbao.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hgbao.model.Company;
import com.hgbao.model.Shop;
import com.hgbao.provider.DataProvider;
import com.hgbao.shop24h.R;

import java.util.ArrayList;

public class RecyclerAdapterNearby extends RecyclerView.Adapter<RecyclerAdapterNearby.ViewHolderShop> {
    Activity context;
    static ArrayList<Shop> list;
    static ArrayList<Double> list_distance;

    public RecyclerAdapterNearby(Activity context, ArrayList<Shop> list, ArrayList<Double> list_distance) {
        this.context = context;
        this.list = list;
        this.list_distance = list_distance;
    }

    @Override
    public ViewHolderShop onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(context).inflate(R.layout.custom_row_nearby, viewGroup, false);
        ViewHolderShop holder = new ViewHolderShop(root, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderShop viewHolder, final int i) {
        final Shop shop = list.get(i);
        //Get marker
        Company company = null;
        for (int j = 0; j < DataProvider.list_company.size(); j++){
            company = DataProvider.list_company.get(j);
            if (shop.getCompanyId().equalsIgnoreCase(company.getId())) {
                break;
            }
        }
        viewHolder.imgLogo.setImageBitmap(BitmapFactory.decodeByteArray(company.getMarker(), 0, company.getMarker().length));
        viewHolder.txtName.setText(company.getName());
        viewHolder.txtAddress.setText(shop.getAddress());
        viewHolder.txtDistance.setText(list_distance.get(i) + " km");
        viewHolder.layoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = context.getIntent();
                intent.putExtra("LATITUDE", shop.getLatitude());
                intent.putExtra("LONGITUDE", shop.getLongitude());
                context.setResult(DataProvider.RESULT_NEARBY, intent);
                context.finish();
            }
        });
        viewHolder.layoutDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double currentLat = context.getIntent().getDoubleExtra("LATITUDE", DataProvider.DEFAULT_LATITUDE);
                double currentLong = context.getIntent().getDoubleExtra("LONGITUDE", DataProvider.DEFAULT_LONGITUDE);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&hl=en&saddr=" + currentLat + "," + currentLong +
                                "&daddr=" + shop.getLatitude() + "," + shop.getLongitude()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolderShop extends RecyclerView.ViewHolder{
        Activity context;
        ImageView imgLogo;
        TextView txtName, txtAddress, txtDistance;
        LinearLayout layoutShop, layoutDirection;

        public ViewHolderShop(View itemView, Activity context) {
            super(itemView);
            this.context = context;
            imgLogo = (ImageView) itemView.findViewById(R.id.imgCustomRowNearbyLogo);
            txtName = (TextView) itemView.findViewById(R.id.txtCustomRowNearbyName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtCustomRowNearbyAddress);
            txtDistance = (TextView) itemView.findViewById(R.id.txtCustomRowNearbyDistance);
            layoutShop = (LinearLayout) itemView.findViewById(R.id.layoutCustomRowNearbyInformation);
            layoutDirection = (LinearLayout) itemView.findViewById(R.id.layoutCustomRowNearbytDirection);
        }
    }
}