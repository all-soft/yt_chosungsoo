package com.chsapps.yt_nahoonha.preference;

import android.content.SharedPreferences;

import com.chsapps.yt_nahoonha.app.AllSoft;
import com.securepreferences.SecurePreferences;

public class Preference {
    private final static String TAG = Preference.class.getSimpleName();

    private static Preference instance;
    private SharedPreferences prefs = new SecurePreferences(AllSoft.getContext());
    public static Preference getInstance() {
        if(instance == null) {
            instance = new Preference();
        }
        return instance;
    }

    public Preference() {
        initialize();
    }

    public void putBoolean(String key, boolean val) {
        prefs.edit().putBoolean(key, val).apply();
    }
    public void putInteger(String key, int val) {
        prefs.edit().putInt(key, val).apply();
    }
    public void putLong(String key, long val) {
        prefs.edit().putLong(key, val).apply();
    }
    public void putString(String key, String val) {
        prefs.edit().putString(key, val).apply();
    }
    public boolean getBoolean(String key, boolean def) {
        return prefs.getBoolean(key, def);
    }
    public int getInteger(String key, int def) {
        return prefs.getInt(key, def);
    }
    public long getLong(String key, long def) {
        return prefs.getLong(key, def);
    }
    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    private void initialize() {
    }

    public void clear() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
