package com.hgbao.shop24h;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hgbao.adapter.AdapterPagerPersonal;
import com.hgbao.adapter.AdapterPagerSearch;
import com.hgbao.fragment.FragmentTabPersonal;
import com.hgbao.fragment.FragmentTabSearch;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.SupportProvider;

public class PersonalActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    AdapterPagerPersonal adapter;

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    int current_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        addNavigationDrawer();
        addControl();

        String extraTab = getIntent().getStringExtra(DataProvider.EXTRA_TAB);
        if (extraTab.equals(DataProvider.EXTRA_TAB_NOTIFICATION))
            handleTabChange(0);
        else
        if (extraTab.equals(DataProvider.EXTRA_TAB_FAVOURITE))
            handleTabChange(1);
    }

    private void addControl(){
        //View pager
        viewPager = (ViewPager) findViewById(R.id.viewPagerPersonal);
        adapter = new AdapterPagerPersonal(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //Tab layout
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutPersonal);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //Change tab
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                handleTabChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addNavigationDrawer(){
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPersonal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cá nhân");
        //Navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutPersonal);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationViewPersonal);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent i = null;
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.navigation_map:
                        i = new Intent(getApplicationContext(), MainActivity.class);
                        break;
                    //Personal
                    case R.id.navigation_notification:
                        handleTabChange(0);
                        break;
                    case R.id.navigation_favourite:
                        handleTabChange(1);
                        break;
                    //Search
                    case R.id.navigation_company:
                        i = new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra(DataProvider.EXTRA_TAB, DataProvider.EXTRA_TAB_COMPANY);
                        break;
                    case R.id.navigation_promotion:
                        i = new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra(DataProvider.EXTRA_TAB, DataProvider.EXTRA_TAB_PROMOTION);
                        break;
                    //Settings
                    case R.id.navigation_setting:
                        i = new Intent(getApplicationContext(), SettingActivity.class);
                        break;
                    case R.id.navigation_exit:
                        exit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                if (id == R.id.navigation_setting)
                    startActivity(i);
                else {
                    if (id != R.id.navigation_exit &&
                            id != R.id.navigation_notification &&
                            id != R.id.navigation_favourite) {
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
                return true;
            }
        });
    }

    private void handleTabChange(int tab_index){
        current_tab = tab_index;
        tabLayout.getTabAt(tab_index).select();
        Menu menu = navigationView.getMenu();
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.navigation_main);
        switch (tab_index){
            case 0:
                menu.findItem(R.id.navigation_notification).setChecked(true);
                break;
            default:
                menu.findItem(R.id.navigation_favourite).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            exit();
    }

    private void exit(){
        //Show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
        builder.setTitle("Xác nhận thoát");
        builder.setMessage("Bạn có chắc chắn muốn thoát");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            default:
                if (current_tab == 0) {
                    DataProvider.list_notification.clear();
                    //DataProvider.database.execSQL("DELETE FROM tblNotification");
                    adapter.getFragment().update();
                }
                else {
                    DataProvider.list_favourite.clear();
                    //DataProvider.database.execSQL("UPDATE tblShop SET Favourite = '0'");
                    adapter.getFragment().update();
                }

                Toast.makeText(PersonalActivity.this, "Đã xóa, hãy thoát app để cập nhật", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
