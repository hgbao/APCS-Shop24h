package com.hgbao.thread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.hgbao.provider.SharedPrefHandler;
import com.hgbao.provider.DataProvider;
import com.hgbao.shop24h.MainActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class TaskDatabaseCheck extends AsyncTask<Void, Boolean, Boolean> {

    Activity activity;
    Class destination;
    ProgressDialog progressDialog;

    //Constructor
    public TaskDatabaseCheck(Activity activity, Class destination) {
        this.activity = activity;
        this.destination = destination;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Đang tải");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            String action = DataProvider.WEBSERVICE_NAMESPACE + DataProvider.WEBSERVICE_METHOD_CHECK;
            SoapObject request = new SoapObject(DataProvider.WEBSERVICE_NAMESPACE, DataProvider.WEBSERVICE_METHOD_CHECK);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE transport = new HttpTransportSE(DataProvider.WEBSERVICE_URL);
            transport.call(action, envelope);
            SoapPrimitive objResult = (SoapPrimitive) envelope.getResponse();

            DataProvider.LATEST_VERSION = objResult.toString();

            return DataProvider.CURRENT_VERSION.compareTo(DataProvider.LATEST_VERSION) < 0;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        if (result) {
            SharedPrefHandler.setUpdate(true);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Xác nhận cập nhật");
            builder.setMessage("Đã có cập nhật mới, bạn có muốn tải không?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new TaskDatabaseUpdate(activity, destination).execute();
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
        else {
            Toast.makeText(activity, "Chưa có cập nhật mới hoặc thiết bị không kết nối mạng", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, destination);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
