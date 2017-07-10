package com.uta.gradhelp.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GradHelp extends Application {

    private static GradHelp appController;
    private String netID;
    private LoginResponse loginResponse;

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
    }

    public static synchronized GradHelp getInstance() {
        return appController;
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    public String getNetID() {
        return netID;
    }

    public void setNetID(String netID) {
        this.netID = netID;
    }

    public static int dpToPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getInstance().getResources().getDisplayMetrics());
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public String getFormattedDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.format(d);
    }
}
