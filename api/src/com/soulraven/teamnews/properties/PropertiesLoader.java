package com.soulraven.teamnews.properties;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public final class PropertiesLoader {

    private static final String VALUE_NOT_AVAILABLE = "N/A";
    private static final String TAG = PropertiesLoader.class.getSimpleName();

    private static boolean INITIALIZED = false;
    private static final Properties properties = new Properties();

    public static final String TEAM_NAME = "team.name";
    public static final String KEYWORDS_SEPARATOR = "keywords.separator";
    public static final String KEYWORDS = "keywords";
    public static final String RSS_URL_NOFILTER = "rss.url.nofilter";
    public static final String RSS_URL_KEYWORDS = "rss.url.keywords";
    public static final String APP_ID = "app.id";

    private PropertiesLoader() {}

    public static void initialize(final Context context) {
        Log.d(TAG, "Initializing properties. Context: " + context);
        try {
            initialize(context.getAssets().open("application.properties"));
        } catch (Exception e) {
            Log.e(TAG, "Error while loading properties", e);
        }
    }

    public static void initialize(final InputStream inputStream) {
        try {
            properties.load(inputStream);
            INITIALIZED = true;
            inputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Error while loading properties", e);
        }
    }

    public static String getProperty(final String key) {
        if (!INITIALIZED) {
            throw new RuntimeException("Properties not initialized");
        }
        Log.d(TAG, "retrieving property " + key);
        return properties.getProperty(key, VALUE_NOT_AVAILABLE);
    }

    public static Enumeration<Object> getKeys() {
        if (!INITIALIZED) {
            throw new RuntimeException("Properties not initialized");
        }
        Log.d(TAG, "retrieving property keys");
        return properties.keys();
    }
}
