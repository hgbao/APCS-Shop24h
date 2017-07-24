package com.hgbao.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgbao.model.Notification;
import com.hgbao.model.Shop;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.SupportProvider;
import com.hgbao.shop24h.CompanyActivity;
import com.hgbao.shop24h.MainActivity;
import com.hgbao.shop24h.R;
import com.hgbao.shop24h.ShopActivity;
import com.hgbao.thread.TaskDatabaseCheck;

import java.util.ArrayList;

public class RecyclerAdapterNotification extends RecyclerView.Adapter<RecyclerAdapterNotification.ViewHolderNotification> {
    Activity context;
    static ArrayList<Notification> list;

    public RecyclerAdapterNotification(Activity context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolderNotification onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(context).inflate(R.layout.custom_row_notification, viewGroup, false);
        ViewHolderNotification holder = new ViewHolderNotification(root, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderNotification viewHolder, final int i) {
        final Notification notification = list.get(i);
        //Get name and intent
        String name = "";
        if (notification.getType().equalsIgnoreCase(DataProvider.TYPE_PROMOTION))
            name = "Khuyến mãi mới";
        else
        if (notification.getType().equalsIgnoreCase(DataProvider.TYPE_SHOP))
            name = "Cửa hàng mới";
        else
            name = "Thương hiệu mới";
        //Set text
        viewHolder.txtName.setText(name);
        viewHolder.txtDescription.setText(notification.getDescription());
        viewHolder.txtDate.setText(SupportProvider.getStringDate(notification.getDate()));
        viewHolder.layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isUpdate())
                    openNotification(list.get(i));
                else{
                    new TaskDatabaseCheck(context, MainActivity.class).execute();
                }
            }
        });
    }

    private void openNotification(Notification notification){
        Intent intent = null;
        if (notification.getType().equalsIgnoreCase(DataProvider.TYPE_PROMOTION)) {
            //Start intent
        }
        else
        if (notification.getType().equalsIgnoreCase(DataProvider.TYPE_SHOP)) {
            intent = new Intent(context, ShopActivity.class);
            Shop shop = DataProvider.getShop(notification.getCompanyId(), notification.getObjectId());
            if (shop != null)
                intent.putExtra(DataProvider.EXTRA_SHOP, shop);
        }
        else {
            intent = new Intent(context, CompanyActivity.class);
            for (int j = 0; j < DataProvider.list_company.size(); j++){
                if (DataProvider.list_company.get(j).getId().equalsIgnoreCase(notification.getCompanyId())) {
                    intent.putExtra(DataProvider.EXTRA_COMPANY_INDEX, j);
                    break;
                }
            }
        }
        //Start intent
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_up, R.anim.activity_fade_out);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolderNotification extends RecyclerView.ViewHolder{
        Activity context;
        TextView txtName, txtDescription, txtDate;
        LinearLayout layoutNotification;

        public ViewHolderNotification(View itemView, Activity context) {
            super(itemView);
            this.context = context;
            txtName = (TextView) itemView.findViewById(R.id.txtCustomRowNotificationName);
            txtDescription = (TextView) itemView.findViewById(R.id.txtCustomRowNotificationDescription);
            txtDate = (TextView) itemView.findViewById(R.id.txtCustomRowNotificationDate);
            layoutNotification = (LinearLayout) itemView.findViewById(R.id.layoutCustomRowNotification);
        }
    }
}