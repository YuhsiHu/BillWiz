package com.billwiz.admin.billwiz.Activity;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;




public class BillWizApplication extends Application {

    public static final int VERSION = 120;

    private static Context mContext;





    @Override public void onCreate() {
        super.onCreate();

        BillWizApplication.mContext = this.getApplicationContext();
    }

    public static Context getAppContext() {
        return BillWizApplication.mContext;
    }

    public static String getAndroidId() {
        return Settings.Secure.getString(
                getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
