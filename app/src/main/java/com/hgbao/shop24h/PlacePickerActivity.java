package com.hgbao.shop24h;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.GPSTracker;
import com.hgbao.provider.SupportProvider;

public class PlacePickerActivity extends AppCompatActivity {
    SearchView searchView;
    GoogleMap map;
    Button btnYes, btnNo;

    LatLng position;
    Marker positionMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        double latitude = getIntent().getDoubleExtra("LATITUDE", DataProvider.DEFAULT_LATITUDE);
        double longitude = getIntent().getDoubleExtra("LONGITUDE", DataProvider.DEFAULT_LONGITUDE);
        position = new LatLng(latitude, longitude);

        addControl();
        addEvent();
    }

    private void addControl() {
        //Place picker
        searchView = (SearchView) findViewById(R.id.searchViewPicker);
        //Map picker
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapPicker)).getMap();
        if (map != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DataProvider.MAP_ZOOM_CURRENT));
            map.getUiSettings().setAllGesturesEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            map.setMyLocationEnabled(true);

            MarkerOptions options = new MarkerOptions();
            options.position(position);
            positionMarker = map.addMarker(options);
        }

        btnYes = (Button) findViewById(R.id.btnPickerYes);
        btnNo = (Button) findViewById(R.id.btnPickerNo);
    }

    private void addEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Address place = SupportProvider.getAddress(PlacePickerActivity.this, query);
                if (place != null) {
                    searchView.setQuery(SupportProvider.getStringAddress(place), false);
                    handleChangePosition(place.getLatitude(), place.getLongitude());
                } else
                    searchView.setQuery("Không tìm thấy", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        if (map != null) {
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    handleChangePosition(latLng.latitude, latLng.longitude);
                }
            });
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    GPSTracker gps = new GPSTracker(PlacePickerActivity.this);
                    if (gps.canGetLocation())
                        position = new LatLng(gps.getLatitude(), gps.getLongitude());
                    handleChangePosition(position.latitude, position.longitude);
                    return true;
                }
            });
        }

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("LATITUDE", position.latitude);
                intent.putExtra("LONGITUDE", position.longitude);
                setResult(DataProvider.RESULT_PLACE_PICKER, intent);
                finish();
            }
        });

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (SupportProvider.isGPSEnabled(PlacePickerActivity.this)) {
                    GPSTracker gps = new GPSTracker(PlacePickerActivity.this);
                    if (gps.canGetLocation())
                        position = new LatLng(gps.getLatitude(), gps.getLongitude());
                    handleChangePosition(position.latitude, position.longitude);
                } else
                    Toast.makeText(PlacePickerActivity.this, "Chưa kích hoạt GPS", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleChangePosition(double latitude, double longitude) {
        position = new LatLng(latitude, longitude);
        positionMarker.setPosition(position);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DataProvider.MAP_ZOOM_CURRENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
