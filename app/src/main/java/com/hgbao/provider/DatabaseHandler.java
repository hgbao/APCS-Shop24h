package com.hgbao.provider;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.hgbao.shop24h.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public final class DatabaseHandler {
    /**
     * Copy database file from assets to internal storage
     * @param context
     */
    private static void copyDatabaseFromAssets(Context context, String dbName){
        String DATABASE_NAME = dbName;
        String DB_PATH_SUFFIX = "/databases/";
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                f.mkdir();
                InputStream inputFile = context.getAssets().open(DATABASE_NAME);
                String outputFile = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
                OutputStream myOutput = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputFile.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                inputFile.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void createDatabase(Context context){
        copyDatabaseFromAssets(context, "dbCompany");
    }

    /**
     * Save database
     * @param context
     * @param objects
     * @param fileName
     * @return
     */
    public static boolean saveDatabase(Context context, Object objects, String fileName) {
        try {
            File file = context.getDatabasePath(fileName);
            //File file = new File(Environment.getExternalStorageDirectory() + "/Shop24h", fileName);
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(objects);
            fout.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Open and read database
     * @param context
     * @param fileName
     * @return
     */
    public static ArrayList<?> readDatabase(Context context, String fileName){
        try {
            File file = context.getDatabasePath(fileName);
            //File file = new File(Environment.getExternalStorageDirectory() + "/Shop24h", fileName);
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fin);
            ArrayList<?> database = (ArrayList<?>) ois.readObject();
            fin.close();
            return database;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
