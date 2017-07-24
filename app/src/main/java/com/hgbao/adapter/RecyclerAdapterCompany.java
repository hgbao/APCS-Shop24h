package com.hgbao.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hgbao.model.Company;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.ImageProvider;
import com.hgbao.provider.SupportProvider;
import com.hgbao.shop24h.CompanyActivity;
import com.hgbao.shop24h.R;

import java.util.ArrayList;

public class RecyclerAdapterCompany extends RecyclerView.Adapter<RecyclerAdapterCompany.ViewHolderCompany> {
    Activity context;
    ArrayList<Company> list;

    public RecyclerAdapterCompany(Activity context, ArrayList<Company> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolderCompany onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(context).inflate(R.layout.custom_row_company, viewGroup, false);
        ViewHolderCompany holder = new ViewHolderCompany(root, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderCompany viewHolder, final int i) {
        Company company = list.get(i);
        viewHolder.imgLogo.setImageBitmap(ImageProvider.createThumbnail(company.getLogo(),
                (int) context.getResources().getDimension(R.dimen.logo_width_big),
                (int) context.getResources().getDimension(R.dimen.logo_height_big)));
        viewHolder.txtName.setText(company.getName());
        viewHolder.txtName.setTextColor(Color.parseColor(company.getColorPrimary()));
        viewHolder.txtSize.setText(company.getList_shop().size() + " cửa hàng");
        viewHolder.layoutCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CompanyActivity.class);
                intent.putExtra(DataProvider.EXTRA_COMPANY_INDEX, i);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolderCompany extends RecyclerView.ViewHolder{
        ImageView imgLogo;
        TextView txtName, txtSize;
        LinearLayout layoutCompany;
        Activity context;
        public ViewHolderCompany(View itemView, Activity context) {
            super(itemView);
            this.context = context;
            imgLogo = (ImageView) itemView.findViewById(R.id.imgCustomRowCompanyLogo);
            txtName = (TextView) itemView.findViewById(R.id.txtCustomRowCompanyName);
            txtSize = (TextView) itemView.findViewById(R.id.txtCustomRowCompanySize);
            layoutCompany = (LinearLayout) itemView.findViewById(R.id.layoutCustomRowCompany);
        }
    }
}