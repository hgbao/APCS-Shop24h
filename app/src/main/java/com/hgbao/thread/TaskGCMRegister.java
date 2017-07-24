package com.hgbao.thread;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.google.android.gcm.GCMRegistrar;
import com.hgbao.provider.DataProvider;
import com.hgbao.provider.SharedPrefHandler;
import com.hgbao.shop24h.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class TaskGCMRegister extends AsyncTask<Void, Void, Boolean>{
    Context context;
    String regID;

    //Constructor
    public TaskGCMRegister(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        GCMRegistrar.checkDevice(context);
        GCMRegistrar.checkManifest(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        regID = GCMRegistrar.getRegistrationId(context);
        //Not created yet
        if (regID.isEmpty()) {
            GCMRegistrar.register(context, context.getResources().getString(R.string.GCM_SENDER_ID));
            GCMRegistrar.setRegisteredOnServer(context, true);
            regID = GCMRegistrar.getRegistrationId(context);
            return (!regID.isEmpty() && addDatabase(regID));
        }
        //Created
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            //regID was not inserted to database
            if (!addDatabase(regID)) {
                GCMRegistrar.unregister(context);
                return false;
            }
        }
        //Created but not yet sent ID to sever
        else {
            GCMRegistrar.unregister(context);
            return false;
        }
        return true;
    }

    private boolean addDatabase(String regId) {
        try{
            String method = DataProvider.WEBSERVICE_METHOD_REGISTER;
            String action = DataProvider.WEBSERVICE_NAMESPACE + method;
            SoapObject request = new SoapObject(DataProvider.WEBSERVICE_NAMESPACE, method);
            request.addProperty(DataProvider.WEBSERVICE_PARAMETER_REGID, regId);

            SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            MarshalFloat marshal = new MarshalFloat();
            marshal.register(envelope);
            //Call
            HttpTransportSE androidHttpTransport= new HttpTransportSE(DataProvider.WEBSERVICE_URL);
            androidHttpTransport.call(action, envelope);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result){
            SharedPrefHandler.setGCMRegID(regID);
        }
    }
}
