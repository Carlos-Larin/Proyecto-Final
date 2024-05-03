package com.ugb.controlesbasicos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class detectarInternet {
    private Context context;
    public detectarInternet(Context context) {
        this.context = context;
    }
    public boolean hayConexionInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if( connectivityManager==null ) return false;
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : networkInfos) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) return true;
        }
        return false;
    }
}