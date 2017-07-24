package com.hgbao.shop24h;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hgbao.provider.DatabaseHandler;
import com.hgbao.provider.SharedPrefHandler;
import com.hgbao.thread.TaskDatabaseCheck;
import com.hgbao.thread.TaskGCMRegister;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        startLoading();
    }

    private void requirePermission(String permission){
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
    }

    private void startLoading() {
        //Check permission
        requirePermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
        requirePermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        requirePermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requirePermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //Create shared preference
        SharedPrefHandler.initializeData(this);

        //Create database
        DatabaseHandler.createDatabase(this);

        //Check update and read database
        if (!SharedPrefHandler.isGCMRegistered())
            new TaskGCMRegister(this).execute();

        if (SharedPrefHandler.isUpdateAvailable()){
            new TaskDatabaseCheck(this, MainActivity.class).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_loading, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
