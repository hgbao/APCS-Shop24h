package com.hgbao.shop24h;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.hgbao.provider.SharedPrefHandler;
import com.hgbao.provider.DataProvider;

import java.util.StringTokenizer;

public class GCMIntentService extends GCMBaseIntentService{
    @Override
    protected void onMessage(Context context, Intent intent) {
        if (intent != null) {
            String message = intent.getStringExtra("message");
            if (message != null && !message.isEmpty()) {
                generateNotification(context, message);
                insertNotification(context, message);
                SharedPrefHandler.setUpdate(true);
            }
        }
    }

    @Override
    protected void onError(Context context, String errorId) {
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        //ServerTask.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            GCMRegistrar.setRegisteredOnServer(context, false);
            //ServerTask.post_unregister(context, registrationId);
        }
    }

    private static void insertNotification(Context context, String message){
        StringTokenizer tokens = new StringTokenizer(message, "-");
        String type = tokens.nextToken();
        String description = tokens.nextToken();
        String companyId = tokens.nextToken();
        String objectId = tokens.nextToken();
        String date = tokens.nextToken();
        //Execute query
        ContentValues values = new ContentValues();
        values.put("Type", type);
        values.put("Description", description);
        values.put("CompanyID", companyId);
        values.put("ObjectID", objectId);
        values.put("Date", date);
        values.put("isUpdate", "0");
        //Search
        SQLiteDatabase database = context.openOrCreateDatabase("dbShop24h", context.MODE_PRIVATE, null);
        Cursor cur = database.query("tblNotification", null, null, null, null, null, null);
        boolean isExist = false;
        while (cur.moveToNext()) {
            if (cur.getString(2).equalsIgnoreCase(description))
                isExist = true;
        }
        if (!isExist)
            database.insert("tblNotification", null, values);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void generateNotification(Context context, String message) {
        StringTokenizer tokens = new StringTokenizer(message, "-");
        String title = tokens.nextToken();
        String content = tokens.nextToken();
        int code = 0;
        //Get notification code and title
        if (title.equalsIgnoreCase(DataProvider.TYPE_PROMOTION)) {
            title = "Khuyến mãi mới";
            code = DataProvider.NOTIFICATION_CODE_PROMOTION;
        }
        if (title.equalsIgnoreCase(DataProvider.TYPE_SHOP)) {
            title = "Cửa hàng mới";
            code = DataProvider.NOTIFICATION_CODE_SHOP;
        }
        if (title.equalsIgnoreCase(DataProvider.TYPE_COMPANY)){
            title = "Nhãn hiệu mới";
            code = DataProvider.NOTIFICATION_CODE_COMPANY;
        }
        if (title.equalsIgnoreCase(DataProvider.TYPE_CITY)){
            title = "Cập nhật thành phố mới";
            code = DataProvider.NOTIFICATION_CODE_CITY;
        }
        if (title.equalsIgnoreCase(DataProvider.TYPE_DISTRICT)){
            title = "Cập nhật quận mới";
            code = DataProvider.NOTIFICATION_CODE_DISTRICT;
        }

        //Notification
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent iOpen = new Intent(context, LoadingActivity.class);
        PendingIntent iPending = PendingIntent.getActivity(context, 0, iOpen, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Build notification
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_small)
                .setAutoCancel(true)
                .setContentIntent(iPending)
                .setSound(alarmSound);
        Notification notification = builder.build();
        manager.notify(code, notification);
    }
}