package com.hgbao.shop24h;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgbao.model.Company;
import com.hgbao.model.Shop;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.DatabaseHandler;
import com.hgbao.provider.GPSTracker;
import com.hgbao.provider.SupportProvider;
import com.hgbao.thread.TaskDrawMarker;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    GoogleMap map;
    LatLng position;
    Marker positionMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.list_company = (ArrayList<Company>) DatabaseHandler.readDatabase(this, "dbCompany");
        DataProvider.list_favourite = (ArrayList<Shop>) DatabaseHandler.readDatabase(this, DataProvider.DATABASE_FAV);

        addNavigationDrawer();
        addControl();
        addEvent();

        Intent intent = getIntent();
        if (intent != null){
            double latitude = intent.getDoubleExtra("LATITUDE", position.latitude);
            double longitude = intent.getDoubleExtra("LONGITUDE", position.longitude);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), DataProvider.MAP_ZOOM_CURRENT));
        }
    }

    private void addControl() {
        //Current position
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()){
            position = new LatLng(gps.getLatitude(), gps.getLongitude());
        } else {
            gps.showSettingsAlert();
            position = new LatLng(DataProvider.DEFAULT_LATITUDE, DataProvider.DEFAULT_LONGITUDE);
        }
        //fix bug here
        position = new LatLng(DataProvider.DEFAULT_LATITUDE, DataProvider.DEFAULT_LONGITUDE);

        //Google map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapMain)).getMap();
        if (map != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DataProvider.MAP_ZOOM_ALL));
            map.getUiSettings().setAllGesturesEnabled(true);
            //Draw markers
            new TaskDrawMarker(MainActivity.this, map).execute();

            MarkerOptions options = new MarkerOptions();
            options.position(position);
            positionMarker = map.addMarker(options);
        }
    }

    private void addEvent() {
        if (map != null) {
            //Markers
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.getSnippet() != null){
                        String snippet = marker.getSnippet();
                        int space = snippet.lastIndexOf(" ");
                        //animate camera
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DataProvider.MAP_ZOOM_CURRENT));
                    }
                    return true;
                }
            });
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                }
            });
        }
    }

    public void fabClick(View v){
        if (v.getId() == R.id.fabSearch){
            return;
        }
        if (v.getId() == R.id.fabNearby){
            Intent intent = new Intent(MainActivity.this, NearbyActivity.class);
            intent.putExtra("LATITUDE", position.latitude);
            intent.putExtra("LONGITUDE", position.longitude);
            startActivityForResult(intent, DataProvider.REQUEST_NEARBY);
            return;
        }
    }

    private void addNavigationDrawer() {
        //Action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bản đồ");
        //Navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationViewMain);
        navigationView.getMenu().findItem(R.id.navigation_map).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent i = null;
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_map:
                        //i = new Intent(getApplicationContext(), MainActivity.class);
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
                    if (id != R.id.navigation_exit && id != R.id.navigation_map) {
                        startActivity(i);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            exit();
    }

    private void exit() {
        //Show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DataProvider.REQUEST_PLACE_PICKER && resultCode == DataProvider.RESULT_PLACE_PICKER){
            double latitude = data.getDoubleExtra("LATITUDE", DataProvider.DEFAULT_LATITUDE);
            double longitude = data.getDoubleExtra("LONGITUDE", DataProvider.DEFAULT_LONGITUDE);
            position = new LatLng(latitude, longitude);
            positionMarker.setPosition(position);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DataProvider.MAP_ZOOM_CURRENT));
        }

        if (requestCode == DataProvider.REQUEST_NEARBY && resultCode == DataProvider.RESULT_NEARBY){
            double latitude = data.getDoubleExtra("LATITUDE", position.latitude);
            double longitude = data.getDoubleExtra("LONGITUDE", position.longitude);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), DataProvider.MAP_ZOOM_CURRENT));
        }
    }
}
