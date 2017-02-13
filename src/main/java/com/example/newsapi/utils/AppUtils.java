package com.example.newsapi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.newsapi.application.NewsApiApplication;

public class AppUtils {

    public static boolean internetIsEnabled() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NewsApiApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean isEmpty(String txt) {
        return txt == null || txt.trim().isEmpty();
    }


}