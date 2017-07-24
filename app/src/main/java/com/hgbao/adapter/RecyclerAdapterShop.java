package com.hgbao.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.hgbao.shop24h.ShopActivity;

import java.util.ArrayList;

public class RecyclerAdapterShop extends RecyclerView.Adapter<RecyclerAdapterShop.ViewHolderShop> {
    Activity context;
    static ArrayList<Shop> list;

    public RecyclerAdapterShop(Activity context, ArrayList<Shop> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolderShop onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(context).inflate(R.layout.custom_row_shop, viewGroup, false);
        return new ViewHolderShop(root, context);
    }

    @Override
    public void onBindViewHolder(ViewHolderShop viewHolder, final int i) {
        Shop shop = list.get(i);
        //Get marker
        Company company = null;
        for (int j = 0; j < DataProvider.list_company.size(); j++){
            company = DataProvider.list_company.get(j);
            if (shop.getCompanyId().equalsIgnoreCase(company.getId())) {
                break;
            }
        }
        viewHolder.imgLogo.setImageBitmap(BitmapFactory.decodeByteArray(company.getMarker(), 0, company.getMarker().length));
        viewHolder.txtName.setText(shop.getName());
        viewHolder.txtName.setTextColor(Color.parseColor(company.getColorPrimary()));
        viewHolder.txtAddress.setText(shop.getAddress());
        viewHolder.txtDistrict.setText(DataProvider.getDistrict(shop.getDistrictId()));
        viewHolder.layoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopActivity.class);
                intent.putExtra(DataProvider.EXTRA_SHOP, list.get(i));
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_fade_out);
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
        TextView txtName, txtAddress, txtDistrict;
        LinearLayout layoutShop;

        public ViewHolderShop(View itemView, Activity context) {
            super(itemView);
            this.context = context;
            imgLogo = (ImageView) itemView.findViewById(R.id.imgCustomRowShopLogo);
            txtName = (TextView) itemView.findViewById(R.id.txtCustomRowShopName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtCustomRowShopAddress);
            txtDistrict = (TextView) itemView.findViewById(R.id.txtCustomRowShopDistrict);
            layoutShop = (LinearLayout) itemView.findViewById(R.id.layoutCustomRowShop);
        }
    }
}