package com.hgbao.shop24h;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgbao.adapter.RecyclerAdapterShop;
import com.hgbao.adapter.WrappingLinearLayoutManager;
import com.hgbao.model.Company;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.DimensionProvider;
import com.hgbao.provider.ImageProvider;
import com.hgbao.provider.SupportProvider;

public class CompanyActivity extends AppCompatActivity {
    TextView txtInformation, txtListShop;
    TextView txtName, txtAddress, txtPhone, txtWebsite, txtEmail;
    ImageView imgLogo;

    Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        company = DataProvider.list_company.get(getIntent().getIntExtra(DataProvider.EXTRA_COMPANY_INDEX, 0));
        addControl();
        addEvent();
        setData();
        addShopList();
    }

    private void addControl() {
        //Tool bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarCompany));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(company.getName());
        //Controls
        txtInformation = (TextView) findViewById(R.id.txtCompanyInformation);
        txtListShop = (TextView) findViewById(R.id.txtCompanyListShop);
        txtName = (TextView) findViewById(R.id.txtCompanyName);
        txtAddress = (TextView) findViewById(R.id.txtCompanyAddress);
        txtPhone = (TextView) findViewById(R.id.txtCompanyPhone);
        txtWebsite = (TextView) findViewById(R.id.txtCompanyWebsite);
        txtEmail = (TextView) findViewById(R.id.txtCompanyMail);
        imgLogo = (ImageView) findViewById(R.id.imgCompanyLogo);
    }

    private void addEvent() {

    }

    private void setData() {
        //Color data
        txtInformation.setBackgroundColor(Color.parseColor(company.getColorPrimary()));
        txtListShop.setBackgroundColor(Color.parseColor(company.getColorSecondary()));
        //Text data
        String address = company.getAddress();
        String email = company.getEmail();
        String phone = company.getPhone();
        if (address == null || address.equals(""))
            address = getResources().getString(R.string.error_infomation);
        if (email == null || email.equals(""))
            email = getResources().getString(R.string.error_infomation);
        if (phone == null || phone.equals(""))
            phone = getResources().getString(R.string.error_infomation);
        txtName.setText(company.getDetail());
        txtWebsite.setText(company.getWebsite());
        txtAddress.setText(address);
        txtPhone.setText(phone);
        txtEmail.setText(email);
        //Image data
        imgLogo.setImageBitmap(ImageProvider.createThumbnail(company.getLogo(), DimensionProvider.DIMEN_LOGO_WIDTH, DimensionProvider.DIMEN_LOGO_HEIGHT));
        imgLogo.getLayoutParams().height = DimensionProvider.DIMEN_LOGO_HEIGHT;
        imgLogo.requestLayout();
    }

    private void addShopList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvListShop);
        recyclerView.setAdapter(new RecyclerAdapterShop(this, company.getList_shop()));
        recyclerView.setLayoutManager(new WrappingLinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company, menu);
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
}
