package com.anandkumar.mjira;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Anand on 2/9/2016.
 */
public class SaveData {

    private static final String CURRENT_USER=null;
    private static final String DEVICE_ID=null;



    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context mContext;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SaveData(Context context) {
        this.mContext = context;

        // Sharedpref file name
        final String PREF_NAME = mContext.getString(R.string.app_name) + "_pref";
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getCurrentUser() {
        return pref.getString(CURRENT_USER, null);
    }

    public void setCurrentUser(String user){
        editor.putString(CURRENT_USER, user);
        editor.commit();
    }

    public String getDeviceId() {
        return pref.getString(DEVICE_ID, null);
    }

    public void setDeviceId(String deviceId){
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }

}