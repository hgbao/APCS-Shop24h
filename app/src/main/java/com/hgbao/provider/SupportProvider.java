package com.hgbao.provider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.hgbao.thread.TaskDatabaseUpdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public final class SupportProvider {
    //Current status
    public static boolean isNetworkConnected(Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnectedOrConnecting());
    }

    public static boolean isGPSEnabled(Activity context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //Position
    public static String getCurrentAddress(Activity context, LatLng position){
        try {
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            List<Address> list = geo.getFromLocation(position.latitude, position.longitude, 1);
            if (list.isEmpty())
                return "";
            else{
                Address address = list.get(0);
                return address.getAddressLine(0) + ", " + address.getAdminArea() + ", " + address.getCountryName();
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "";
    }

    public static Address getAddress(Activity context, String address) {
        Geocoder geoCoder = new Geocoder(context);
        try {
            List<Address> list_address = geoCoder.getFromLocationName(address, 10);
            if (list_address.size() >  0)
                return list_address.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b ) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        double kmConversion = 1.609;

        DecimalFormat df = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.US));
        return Double.valueOf(df.format(distance * kmConversion));
    }

    public static String getStringAddress(Address address){
        return address.getAddressLine(0) + ", " +
                address.getAdminArea() + ", " +
                address.getCountryName();
    }

    //Other
    public static String getStringDate(long dateInMillis){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(dateInMillis);
        String result;
        int day =  date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);
        if (day < 10)
            result = "0" + day + "/";
        else
            result = day + "/";
        if (month < 10)
            result = result + "0" + month + "/";
        else
            result = result + month + "/";
        return result + year;
    }
}
