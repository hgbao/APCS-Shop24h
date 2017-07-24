package com.hgbao.shop24h;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.hgbao.provider.SharedPrefHandler;
import com.hgbao.provider.SupportProvider;
import com.hgbao.thread.TaskDatabaseCheck;
import com.hgbao.thread.TaskDatabaseUpdate;

public class SettingActivity extends AppCompatActivity {

    Switch switchNotification;
    Button btnUpdate, btnRestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        addControl();
        addEvent();
    }

    private void addControl(){
        //Tool bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbarSetting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cài đặt");
        //Controls
        switchNotification = (Switch) findViewById(R.id.switchSettingNotification);
        btnUpdate = (Button) findViewById(R.id.btnSettingUpdate);
        btnRestore = (Button) findViewById(R.id.btnSettingRestore);
        //Data
        if (SharedPrefHandler.isUpdateAvailable())
            switchNotification.setChecked(true);
        else
            switchNotification.setChecked(false);
    }

    private void addEvent(){
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    return;
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SupportProvider.isNetworkConnected(SettingActivity.this))
                    new TaskDatabaseCheck(SettingActivity.this, MainActivity.class).execute();
                else
                    Toast.makeText(SettingActivity.this, "Thiết bị không kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SupportProvider.isNetworkConnected(SettingActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("Xác nhận khôi phục");
                    builder.setMessage("Toàn bộ dữ liệu sẽ được cập nhật đè. Lỗi có thể xảy ra tùy thuộc vào tốc độ mạng.");
                    builder.setPositiveButton("Khôi phục", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new TaskDatabaseUpdate(SettingActivity.this, MainActivity.class).execute();
                        }
                    });
                    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                else
                    Toast.makeText(SettingActivity.this, "Thiết bị không kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
