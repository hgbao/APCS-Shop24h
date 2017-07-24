package com.hgbao.provider;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.hgbao.shop24h.R;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageProvider implements Transformation{
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size)/2;
        int y = (source.getHeight() - size)/2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    public static Bitmap createThumbnail(byte[] arr, int width, int height){
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(arr, 0, arr.length), width, height);
    }

    public static byte[] getByteArrayFromURL(Activity context, String link){
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(DataProvider.TIME_OUT_REQUEST);
            connection.setReadTimeout(DataProvider.TIME_OUT_REQUEST);
            connection.setDoInput(true);
            connection.connect();

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while ((n = connection.getInputStream().read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, n);
            }
            return byteBuffer.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override public String key() { return "square()"; }
}
