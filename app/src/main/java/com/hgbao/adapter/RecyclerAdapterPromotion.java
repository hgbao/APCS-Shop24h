package com.hgbao.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.hgbao.model.Company;
import com.hgbao.model.Promotion;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.DimensionProvider;
import com.hgbao.provider.ImageProvider;
import com.hgbao.provider.SupportProvider;
import com.hgbao.shop24h.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class RecyclerAdapterPromotion extends RecyclerView.Adapter<RecyclerAdapterPromotion.ViewHolderCompany> {
    Activity context;
    ArrayList<Promotion> list;

    public RecyclerAdapterPromotion(Activity context, ArrayList<Promotion> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolderCompany onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(context).inflate(R.layout.custom_row_promotion, viewGroup, false);
        ViewHolderCompany holder = new ViewHolderCompany(root, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderCompany viewHolder, final int i) {
        final Promotion promotion = list.get(i);
        //Get company
        Company company = null;
        for (int j = 0; j < DataProvider.list_company.size(); j++){
            company = DataProvider.list_company.get(j);
            if (promotion.getCompanyId().equalsIgnoreCase(company.getId())) {
                break;
            }
        }
        //Avatar
        final String directory = Environment.getExternalStorageDirectory() + "/" + DataProvider.DIRECTORY_PROMOTION_AVATAR + "/" +
                promotion.getId() + ".jpg";
        final File avatar = new File(directory);
        Picasso.with(context)
                .load(avatar)
                .resize(DimensionProvider.DIMEN_LOGO_WIDTH, DimensionProvider.DIMEN_LOGO_WIDTH * 5)
                .centerCrop()
                .error(R.drawable.error_no_image)
                .into(viewHolder.imgAvatar);
        //Data's controls
        viewHolder.imgLogo.setImageBitmap(ImageProvider.createThumbnail(company.getLogo(),
                (int) context.getResources().getDimension(R.dimen.logo_width),
                (int) context.getResources().getDimension(R.dimen.logo_height)));
        viewHolder.txtName.setText(promotion.getName());
        viewHolder.txtFrom.setText("Từ: " + SupportProvider.getStringDate(promotion.getStartDate()));
        viewHolder.txtTo.setText("Đến: " + SupportProvider.getStringDate(promotion.getEndDate()));
        viewHolder.layoutPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                if (avatar.exists()) {
                    intent.setDataAndType(Uri.parse(avatar.getAbsolutePath()), "image/*");
                    context.startActivity(intent);
                }
                else
                    Toast.makeText(context, "Không tìm thấy ảnh", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolderCompany extends RecyclerView.ViewHolder{
        ImageView imgLogo, imgAvatar;
        TextView txtName, txtFrom, txtTo;
        FrameLayout layoutPromotion;
        Activity context;
        public ViewHolderCompany(View itemView, Activity context) {
            super(itemView);
            this.context = context;
            imgLogo = (ImageView) itemView.findViewById(R.id.imgCustomRowPromotionLogo);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgCustomRowPromotionAvatar);
            txtName = (TextView) itemView.findViewById(R.id.txtCustomRowPromotionName);
            txtFrom = (TextView) itemView.findViewById(R.id.txtCustomRowPromotionFrom);
            txtTo = (TextView) itemView.findViewById(R.id.txtCustomRowPromotionTo);
            layoutPromotion = (FrameLayout) itemView.findViewById(R.id.layoutCustomRowPromotion);
        }
    }
}