package com.hgbao.shop24h;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hgbao.model.Company;
import com.hgbao.model.Shop;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.DatabaseHandler;
import com.hgbao.provider.DimensionProvider;
import com.hgbao.provider.SupportProvider;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShopActivity extends AppCompatActivity {
    CollapsingToolbarLayout toolbarLayout;
    AppBarLayout appbarLayout;

    TextView txtAddress, txtPhone;
    ImageView imgAvatar;
    LinearLayout layoutAddress, layoutPhone;
    Button btnReport, btnFavourite;

    FloatingActionButton fab;

    Company company;
    Shop shop;
    boolean hasAvatar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shop = (Shop) getIntent().getSerializableExtra(DataProvider.EXTRA_SHOP);
        for (int i = 0; i < DataProvider.list_company.size(); i++) {
            company = DataProvider.list_company.get(i);
            if (company.getId().equalsIgnoreCase(shop.getCompanyId()))
                break;
        }

        addControl();
        addEvent();
        setData();
    }

    private void addControl() {
        //Tool bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarShop));
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarShop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appbarLayout = (AppBarLayout) findViewById(R.id.appBarShop);
        //Controls
        txtAddress = (TextView) findViewById(R.id.txtShopAddress);
        txtPhone = (TextView) findViewById(R.id.txtShopPhone);
        imgAvatar = (ImageView) findViewById(R.id.imgShopAvatar);
        imgAvatar.getLayoutParams().height = DimensionProvider.DIMEN_LOGO_HEIGHT;
        layoutAddress = (LinearLayout) findViewById(R.id.layoutShopAddress);
        layoutPhone = (LinearLayout) findViewById(R.id.layoutShopPhone);
        btnReport = (Button) findViewById(R.id.btnShopReport);
        btnFavourite = (Button) findViewById(R.id.btnShopFavourite);
        fab = (FloatingActionButton) findViewById(R.id.fabShop);
    }

    private void addEvent() {
        layoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                intent.putExtra("LATITUDE", shop.getLatitude());
                intent.putExtra("LONGITUDE", shop.getLongitude());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_slide_down);
                finish();
            }
        });

        layoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shop.getPhone()));
                startActivity(intent);
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, PlacePickerActivity.class);
                intent.putExtra("LATITUDE", shop.getLatitude());
                intent.putExtra("LONGITUDE", shop.getLongitude());
                startActivityForResult(intent, DataProvider.REQUEST_PLACE_PICKER);
            }
        });

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop.setFavourite();

                if (DatabaseHandler.saveDatabase(ShopActivity.this, DataProvider.list_favourite, DataProvider.DATABASE_FAV))
                    Toast.makeText(ShopActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ShopActivity.this, "Thất bại", Toast.LENGTH_LONG).show();
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasAvatar) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File avatar = new File(
                            Environment.getExternalStorageDirectory() + DataProvider.DIRECTORY_SHOP_AVATAR + "/" + shop.getId() + ".jpg");
                    intent.setDataAndType(Uri.parse(avatar.getAbsolutePath()), "image/*");
                    startActivity(intent);
                }
                else
                    Toast.makeText(ShopActivity.this, "Không có ảnh", Toast.LENGTH_LONG).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, DataProvider.REQUEST_CAMERA);
            }
        });
    }

    private void setData() {
        //Tool bar
        toolbarLayout.setTitle(shop.getName());
        appbarLayout.getLayoutParams().height = DimensionProvider.DIMEN_LOGO_HEIGHT;
        //Text data
        txtAddress.setText(shop.getAddress());
        txtPhone.setText(shop.getPhone().isEmpty()? "(Không rõ)" : shop.getPhone());
        if (shop.isFavourite())
            btnFavourite.setEnabled(false);
        //Image data
        File avatar = new File(Environment.getExternalStorageDirectory() + "/" + DataProvider.DIRECTORY_SHOP_AVATAR + "/" +
                shop.getId() + ".jpg");
        if (avatar.exists())
            hasAvatar = true;
        Picasso.with(ShopActivity.this)
                .load(avatar)
                .error(R.drawable.error_no_image)
                .into(imgAvatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_slide_down);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_slide_down);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Report place
        if (requestCode == DataProvider.REQUEST_PLACE_PICKER && resultCode == DataProvider.RESULT_PLACE_PICKER) {
            final double latitude = data.getDoubleExtra("LATITUDE", DataProvider.DEFAULT_LATITUDE);
            final double longitude = data.getDoubleExtra("LONGITUDE", DataProvider.DEFAULT_LONGITUDE);
            //Send mail
            Intent i = new Intent(Intent.ACTION_SENDTO);
            i.setType("text/plain");
            //i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject_report) + " " + shop.getName());
            i.putExtra(Intent.EXTRA_TEXT, latitude + ", " + longitude);
            i.setData(Uri.parse("mailto:" + getResources().getString(R.string.app_email)));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            //Confirm change database
            AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
            builder.setTitle("Đổi dữ liệu");
            builder.setMessage("Bạn có muốn đổi dữ liệu vị trí cửa hàng không?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues values = new ContentValues();
                    values.put("Latitude", latitude);
                    values.put("Longitude", longitude);
                    shop.setLatitude(latitude);
                    shop.setLongitude(longitude);

                    DatabaseHandler.saveDatabase(ShopActivity.this, DataProvider.list_company, "dbCompany");
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

        //Taking picture
        if (requestCode == DataProvider.REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgAvatar.setImageBitmap(photo);

            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String directory = Environment.getExternalStorageDirectory() + DataProvider.DIRECTORY_SHOP_AVATAR;
                new File(directory).mkdirs();
                File f = new File(directory, shop.getId() + ".jpg");
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
                hasAvatar = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
