package com.hgbao.provider;

import android.content.SharedPreferences;

import com.hgbao.model.City;
import com.hgbao.model.Company;
import com.hgbao.model.District;
import com.hgbao.model.Notification;
import com.hgbao.model.Shop;
import java.util.ArrayList;

public final class DataProvider {
    //Database
    public final static String DATABASE_FAV = "favourite";

    //Directories
    public final static String DIRECTORY_SHOP_AVATAR = "/Shop24h/Pictures/ShopPic";
    public final static String DIRECTORY_PROMOTION_AVATAR = "/Shop24h/Pictures/PromotionPic";

    //Extras
    public final static String EXTRA_COMPANY_INDEX = "EXTRA_COMPANY_INDEX";
    public final static String EXTRA_SHOP = "EXTRA_SHOP";
    public final static String EXTRA_TAB = "EXTRA_TAB";
    public final static String EXTRA_TAB_COMPANY = "TAB_COMPANY";
    public final static String EXTRA_TAB_PROMOTION = "TAB_PROMOTION";
    public final static String EXTRA_TAB_FAVOURITE = "TAB_FAVOURITE";
    public final static String EXTRA_TAB_NOTIFICATION = "TAB_NOTIFICATION";

    //Default values
    public final static double DEFAULT_LATITUDE = 10.7627166;
    public final static double DEFAULT_LONGITUDE = 106.6823101;
    public final static float MAP_ZOOM_ALL = 15;
    public final static float MAP_ZOOM_CURRENT = 17;

    //Webservice
    public final static String WEBSERVICE_NAMESPACE = "http://tempuri.org/";
    public final static String WEBSERVICE_URL = "http://shop24h.somee.com/shop24hwebservice.asmx";
    public final static String WEBSERVICE_PARAMETER_DATE = "date";
    public final static String WEBSERVICE_PARAMETER_REGID = "regId";
    public final static String WEBSERVICE_METHOD_REGISTER = "addDevice";
    public final static String WEBSERVICE_METHOD_CHECK = "getNewestVersion";
    public final static String WEBSERVICE_METHOD_PROMOTION = "getListPromotion";
    public final static String WEBSERVICE_METHOD_SHOP = "getListShop";
    public final static String WEBSERVICE_METHOD_COMPANY = "getListCompany";
    public final static String WEBSERVICE_METHOD_DISTRICT = "getListDistrict";
    public final static String WEBSERVICE_METHOD_CITY = "getListCity";

    //Notifications
    public final static int NOTIFICATION_CODE_PROMOTION = 441400;
    public final static int NOTIFICATION_CODE_SHOP = 441401;
    public final static int NOTIFICATION_CODE_COMPANY = 441402;
    public final static int NOTIFICATION_CODE_DISTRICT = 441403;
    public final static int NOTIFICATION_CODE_CITY = 441404;
    public final static String TYPE_CITY = "CITY";
    public final static String TYPE_DISTRICT = "DISTRICT";
    public final static String TYPE_COMPANY = "COMPANY";
    public final static String TYPE_SHOP = "SHOP";
    public final static String TYPE_PROMOTION = "PROMOTION";

    //Request and result code, timing
    public final static int REQUEST_PLACE_PICKER = 1000;
    public final static int RESULT_PLACE_PICKER = 1001;
    public final static int REQUEST_NEARBY = 2000;
    public final static int RESULT_NEARBY = 2001;
    public final static int REQUEST_CAMERA = 5555;
    public final static int TIME_ANIMATION_FADING = 1500;
    public final static int TIME_OUT_REQUEST = 5000;
    //Attributes
    public final static ArrayList<City> list_city = new ArrayList<>();
    public final static ArrayList<District> list_district = new ArrayList<>();
    public static ArrayList<Company> list_company = new ArrayList<>();
    public static ArrayList<Shop> list_favourite = new ArrayList<>();
    public final static ArrayList<Notification> list_notification = new ArrayList<>();

    public final static Shop getShop(String companyID, String shopID) {
        Company company = null;
        Shop shop = null;
        for (int i = 0; i < list_company.size(); i++) {
            company = list_company.get(i);
            if (company.getId().equalsIgnoreCase(companyID))
                break;
        }
        for (int i = 0; i < company.getList_shop().size(); i++) {
            shop = company.getList_shop().get(i);
            if (shop.getId().equalsIgnoreCase(shopID))
                break;
        }
        return shop;
    }

    public final static String getDistrict(String districtID) {
        District district = null;
        for (int i = 0; i < list_district.size(); i++) {
            district = list_district.get(i);
            if (district.getId().equalsIgnoreCase(districtID))
                break;
        }
        return district.getName();
    }

    //Shared Preference
    public static SharedPreferences sharedPreferences;
    public static String CURRENT_VERSION;
    public static String LATEST_VERSION;
}