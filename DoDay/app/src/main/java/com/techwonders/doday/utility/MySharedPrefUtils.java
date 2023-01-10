package com.techwonders.doday.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPrefUtils {

    private final SharedPreferences mPrefs;

    public MySharedPrefUtils(Context mContext) {
        mPrefs = mContext.getSharedPreferences("MyDoDayPref", 0);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return mPrefs.getString(key, "");
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return mPrefs.getInt(key, -1);
    }


    public void clearString() {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.apply();
    }


    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return mPrefs.getLong(key, -1);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return mPrefs.getBoolean(key, false);
    }

}
