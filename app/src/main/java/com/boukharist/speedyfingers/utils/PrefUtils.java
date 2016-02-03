package com.boukharist.speedyfingers.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.boukharist.speedyfingers.R;
import com.boukharist.speedyfingers.model.Level;

/**
 * Created by ayoub.boukhari on 03/02/2016.
 */
public class PrefUtils {

    public final static String STORAGE_KEY = "progression";

    public static void storePreferences(Context context, String name, String json) {
        SharedPreferences.Editor editor = SwissArmyKnife.getPreferences(context.getApplicationContext(), context.getString(R.string.prefs)).edit();
        editor.putString(name, json);
        editor.commit();
    }


    public static String getPreferences(Context context, String name) {
        SharedPreferences prefs = SwissArmyKnife.getPreferences(context.getApplicationContext(), context.getString(R.string.prefs));
        return prefs.getString(name, null);
    }

    public static Level getLevelProgression(Context context, int position) {
        String name = context.getResources().getStringArray(R.array.level_titles)[position];
        String json = getPreferences(context.getApplicationContext(), name);
        return SwissArmyKnife.getObjectFromJson(json, Level.class);
    }

    public static void putLevelProgression(Context context, Level level) {
        String name = level.getTitle();
        String json = SwissArmyKnife.getJsonFromObject(level);
        storePreferences(context, name, json);
    }

    public static void clearPrefs(Context context) {
        SharedPreferences.Editor editor = SwissArmyKnife.getPreferences(context.getApplicationContext(), context.getString(R.string.prefs)).edit();
        editor.clear();
        editor.commit();
    }
}
