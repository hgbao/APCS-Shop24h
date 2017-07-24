package com.hgbao.provider;

import android.content.Context;
import android.content.SharedPreferences;
import com.hgbao.shop24h.R;

public final class SharedPrefHandler {
    private static void setPreference(String name, String value) {
        SharedPreferences.Editor editor = DataProvider.sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    private static String getPreference(String name) {
        return DataProvider.sharedPreferences.getString(name, "");
    }

    /**
     * Initial data for DataProvicer
     * @param context
     */
    public static void initializeData(Context context){
        DataProvider.sharedPreferences = context.getSharedPreferences("mySharedPreferences", context.MODE_PRIVATE);
        //Initialize data in shared preference
        if (getPreference("CURRENT_VERSION").equalsIgnoreCase("")) {
            setPreference("UPDATE_AVAILABLE", "1");
            setPreference("CURRENT_VERSION", context.getResources().getString(R.string.app_version));
        }
        //Set data to DataProvider
        DataProvider.CURRENT_VERSION = getPreference("CURRENT_VERSION");
    }

    //Checking functions
    public static boolean isGCMRegistered(){
        return !getPreference("GCM_REGISTER_ID").isEmpty();
    }
    public static boolean isUpdateAvailable(){
        return getPreference("UPDATE_AVAILABLE").equalsIgnoreCase("1");
    }
    
    //Setting functions
    public static void setUpdate(boolean value){
        setPreference("UPDATE_AVAILABLE", value ? "1" : "0");
    }
    public static void setVersion(String value){
        setPreference("CURRENT_VERSION", value);
    }
    public static void setGCMRegID(String value){
        setPreference("GCM_REGISTER_ID", value);
    }
}
