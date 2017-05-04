package com.android.parteek.dugo;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Suraj on 4/25/2017.
 */

public class MyApp extends Application {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    boolean idConnected(){
        connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null&&networkInfo.isConnected());
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if(idConnected()) {
            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
