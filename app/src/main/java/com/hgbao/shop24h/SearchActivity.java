package com.hgbao.shop24h;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hgbao.adapter.AdapterPagerSearch;
import com.hgbao.fragment.FragmentTabSearch;
import com.hgbao.model.Company;
import com.hgbao.model.Promotion;
import com.hgbao.provider.DataProvider;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity{
    TabLayout tabLayout;
    ViewPager viewPager;
    AdapterPagerSearch adapter;

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    Menu menuSearch;
    int current_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        addNavigationDrawer();
        addControl();

        String extraTab = getIntent().getStringExtra(DataProvider.EXTRA_TAB);
        if (extraTab.equals(DataProvider.EXTRA_TAB_COMPANY))
            handleTabChange(0);
        else
        if (extraTab.equals(DataProvider.EXTRA_TAB_PROMOTION))
            handleTabChange(1);
    }

    private void addControl(){
        //View pager
        viewPager = (ViewPager) findViewById(R.id.viewPagerSearch);
        adapter = new AdapterPagerSearch(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        //Tab layout
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutSearch);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tra cứu");
        //Navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutSearch);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationViewSearch);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent i = null;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_map:
                        i = new Intent(getApplicationContext(), MainActivity.class);
                        break;
                    //Personal
                    case R.id.navigation_notification:
                        i = new Intent(getApplicationContext(), PersonalActivity.class);
                        i.putExtra(DataProvider.EXTRA_TAB, DataProvider.EXTRA_TAB_NOTIFICATION);
                        break;
                    case R.id.navigation_favourite:
                        i = new Intent(getApplicationContext(), PersonalActivity.class);
                        i.putExtra(DataProvider.EXTRA_TAB, DataProvider.EXTRA_TAB_FAVOURITE);
                        break;
                    //Search
                    case R.id.navigation_company:
                        handleTabChange(0);
                        break;
                    case R.id.navigation_promotion:
                        handleTabChange(1);
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
                            id != R.id.navigation_company &&
                            id != R.id.navigation_promotion) {
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
        //Menu
        current_tab = tab_index;
        if (menuSearch != null) {
            if (tab_index == 0)
                menuSearch.findItem(R.id.action_delete_outofdate).setEnabled(false);
            else
                menuSearch.findItem(R.id.action_delete_outofdate).setEnabled(true);
        }
        //Navigation
        tabLayout.getTabAt(tab_index).select();
        Menu menu = navigationView.getMenu();
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.navigation_main);
        switch (tab_index){
            case 0:
                menu.findItem(R.id.navigation_company).setChecked(true);
                break;
            default:
                menu.findItem(R.id.navigation_promotion).setChecked(true);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_search, menu);
        menuSearch = menu;
        if (current_tab == 0)
            menuSearch.findItem(R.id.action_delete_outofdate).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
