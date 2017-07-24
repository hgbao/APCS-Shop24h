package com.hgbao.thread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.hgbao.model.City;
import com.hgbao.model.Company;
import com.hgbao.model.District;
import com.hgbao.model.Promotion;
import com.hgbao.model.Shop;
import com.hgbao.provider.DatabaseHandler;
import com.hgbao.provider.SharedPrefHandler;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.ImageProvider;
import com.hgbao.provider.SupportProvider;
import com.hgbao.shop24h.MainActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TaskDatabaseUpdate extends AsyncTask<String, String, Void> {
    Activity context;
    Class destination;
    ProgressDialog progressDialog;

    //Data
    ArrayList<Shop> list_shop;
    ArrayList<Company> list_company;
    ArrayList<District> list_district;
    ArrayList<City> list_city;

    //Constructor
    public TaskDatabaseUpdate(Activity context, Class destination) {
        this.context = context;
        this.destination = destination;

        list_shop = new ArrayList<>();
        list_company = new ArrayList<>();
        list_district = new ArrayList<>();
        list_city = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang kiểm tra cập nhật");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        //Get list
        getListShop();
        getListCompany();
        getListDistrict();
        getListCity();
        return null;
    }

    /*
    private void getListPromotion() {
        try {
            String namespace = DataProvider.WEBSERVICE_NAMESPACE;
            String method = DataProvider.WEBSERVICE_METHOD_PROMOTION;
            String action = namespace + method;
            SoapObject request = new SoapObject(namespace, method);
            request.addProperty(DataProvider.WEBSERVICE_PARAMETER_DATE, DataProvider.CURRENT_VERSION + "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(DataProvider.WEBSERVICE_URL);
            transport.call(action, envelope);
            SoapObject soapArray = (SoapObject) envelope.getResponse();
            int n = soapArray.getPropertyCount();

            for (int i = 0; i < n; i++) {
                publishProgress("Đang tải danh sách khuyến mãi mới");
                //Dowloading
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                Promotion obj = new Promotion();
                obj.setId(soapItem.getProperty("ID").toString());
                obj.setCompanyId(soapItem.getProperty("CompanyID").toString());
                obj.setName(soapItem.getProperty("Name").toString());
                obj.setDetail(soapItem.getProperty("Detail").toString());
                obj.setStartDate(Long.parseLong(soapItem.getProperty("StartDate").toString()));
                obj.setEndDate(Long.parseLong(soapItem.getProperty("EndDate").toString()));
                //Image
                byte[] avatar = ImageProvider.getByteArrayFromURL(context, soapItem.getProperty("Avatar").toString());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(avatar.length);
                bytes.write(avatar, 0, avatar.length);
                String directory = Environment.getExternalStorageDirectory() + DataProvider.DIRECTORY_PROMOTION_AVATAR;
                new File(directory).mkdirs();
                File f = new File(directory, obj.getId() + ".jpg");
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
                //Null-able types
                list_promotion.add(obj);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void getListShop() {
        try {
            String namespace = DataProvider.WEBSERVICE_NAMESPACE;
            String method = DataProvider.WEBSERVICE_METHOD_SHOP;
            String action = namespace + method;
            SoapObject request = new SoapObject(namespace, method);
            request.addProperty(DataProvider.WEBSERVICE_PARAMETER_DATE, DataProvider.CURRENT_VERSION + "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(DataProvider.WEBSERVICE_URL);
            transport.call(action, envelope);
            SoapObject soapArray = (SoapObject) envelope.getResponse();
            int n = soapArray.getPropertyCount();

            for (int i = 0; i < n; i++) {
                publishProgress("Đang tải danh sách cửa hàng mới");
                //Downloading
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                Shop obj = new Shop();
                obj.setId(soapItem.getProperty("ID").toString());
                obj.setCompanyId(soapItem.getProperty("CompanyID").toString());
                obj.setName(soapItem.getProperty("Name").toString());
                obj.setAddress(soapItem.getProperty("Address").toString());
                obj.setLatitude(Double.parseDouble(soapItem.getProperty("Latitude").toString()));
                obj.setLongitude(Double.parseDouble(soapItem.getProperty("Longitude").toString()));
                obj.setDistrictId(soapItem.getProperty("DistrictID").toString());
                obj.setCityId(soapItem.getProperty("CityID").toString());
                //Nullable
                String phone = soapItem.getProperty("Phone").toString();
                obj.setPhone(phone.equalsIgnoreCase("anyType{}")? "" : phone);
                list_shop.add(obj);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getListCompany() {
        try {
            String namespace = DataProvider.WEBSERVICE_NAMESPACE;
            String method = DataProvider.WEBSERVICE_METHOD_COMPANY;
            String action = namespace + method;
            SoapObject request = new SoapObject(namespace, method);
            request.addProperty(DataProvider.WEBSERVICE_PARAMETER_DATE, DataProvider.CURRENT_VERSION + "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(DataProvider.WEBSERVICE_URL);
            transport.call(action, envelope);
            SoapObject soapArray = (SoapObject) envelope.getResponse();
            int n = soapArray.getPropertyCount();

            for (int i = 0; i < n; i++) {
                publishProgress("Đang tải danh sách thương hiệu mới");
                //Downloading
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                Company obj = new Company();
                obj.setId(soapItem.getProperty("ID").toString());
                obj.setName(soapItem.getProperty("Name").toString());

                obj.setWebsite(soapItem.getProperty("Website").toString());
                obj.setDetail(soapItem.getProperty("Detail").toString());
                obj.setColorPrimary(soapItem.getProperty("Color").toString());
                //Images
                obj.setLogo(ImageProvider.getByteArrayFromURL(context, soapItem.getProperty("Logo").toString()));
                obj.setMarker(ImageProvider.getByteArrayFromURL(context, soapItem.getProperty("Marker").toString()));
                //Nullable
                String address = soapItem.getProperty("Address").toString();
                String phone = soapItem.getProperty("Phone").toString();
                String email = soapItem.getProperty("Email").toString();
                obj.setAddress(address.equalsIgnoreCase("anyType{}")? "" : address);
                obj.setPhone(phone.equalsIgnoreCase("anyType{}")? "" : phone);
                obj.setEmail(email.equalsIgnoreCase("anyType{}")? "" : email);
                list_company.add(obj);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getListDistrict() {
        try {
            String namespace = DataProvider.WEBSERVICE_NAMESPACE;
            String method = DataProvider.WEBSERVICE_METHOD_DISTRICT;
            String action = namespace + method;
            SoapObject request = new SoapObject(namespace, method);
            request.addProperty(DataProvider.WEBSERVICE_PARAMETER_DATE, DataProvider.CURRENT_VERSION + "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(DataProvider.WEBSERVICE_URL);
            transport.call(action, envelope);
            SoapObject soapArray = (SoapObject) envelope.getResponse();
            int n = soapArray.getPropertyCount();

            for (int i = 0; i < n; i++) {
                publishProgress("Đang tải danh sách quận mới");
                //Downloading
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                District obj = new District();
                obj.setId(soapItem.getProperty("ID").toString());
                obj.setName(soapItem.getProperty("Name").toString());
                list_district.add(obj);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getListCity() {
        try {
            String namespace = DataProvider.WEBSERVICE_NAMESPACE;
            String method = DataProvider.WEBSERVICE_METHOD_CITY;
            String action = namespace + method;
            SoapObject request = new SoapObject(namespace, method);
            request.addProperty(DataProvider.WEBSERVICE_PARAMETER_DATE, DataProvider.CURRENT_VERSION + "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(DataProvider.WEBSERVICE_URL);
            transport.call(action, envelope);
            SoapObject soapArray = (SoapObject) envelope.getResponse();
            int n = soapArray.getPropertyCount();

            for (int i = 0; i < n; i++) {
                publishProgress("Đang tải danh sách thành phố mới");
                //Downloading
                SoapObject soapItem = (SoapObject) soapArray.getProperty(i);
                City obj = new City();
                obj.setId(soapItem.getProperty("ID").toString());
                obj.setName(soapItem.getProperty("Name").toString());
                list_city.add(obj);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (list_shop.isEmpty() || list_city.isEmpty() || list_district.isEmpty() || list_company.isEmpty())
            Toast.makeText(context, "Thất bại, xin thử lại sau", Toast.LENGTH_LONG).show();

        DatabaseHandler.saveDatabase(context, DataProvider.list_company, "dbCompany");

        //Set preferences and database
        DataProvider.CURRENT_VERSION = DataProvider.LATEST_VERSION;
        SharedPrefHandler.setUpdate(false);
        SharedPrefHandler.setVersion(DataProvider.LATEST_VERSION);
        progressDialog.dismiss();
        Toast.makeText(context, "Đã tải xong", Toast.LENGTH_LONG).show();

        //Change intent
        Intent intent = new Intent(context, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        context.finish();
    }
}
