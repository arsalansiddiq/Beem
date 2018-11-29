package com.example.arsalansiddiq.beem.databases;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.arsalansiddiq.beem.utils.Constants;

/**
 * Created by jellani on 11/29/2018.
 */

public class BeemPreferencesCount {

    private Context context;
    SharedPreferences.Editor editor;

    public BeemPreferencesCount(Context context) {
        this.context = context;
        editor = context.getSharedPreferences(Constants.BEEM_PREFERENCE_COUNT, Context.MODE_PRIVATE).edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }
}
