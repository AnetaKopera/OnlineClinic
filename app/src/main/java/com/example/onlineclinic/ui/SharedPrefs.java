package com.example.onlineclinic.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public class SharedPrefs
{
    private static final String SHARED_PREFS = "sharedPrefs";

    public static void saveData(Context context, String key, String value)
    {
        SharedPreferences sharedPreferences =
                Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static String loadData(Context context, String key)
    {
        SharedPreferences sharedPreferences =
                Objects.requireNonNull(context).getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key," ");
    }
}
