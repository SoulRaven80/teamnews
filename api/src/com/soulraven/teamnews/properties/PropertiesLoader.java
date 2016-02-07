package com.soulraven.teamnews.properties;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {

    private static final String VALUE_NOT_AVAILABLE = "N/A";
    private static final String TAG = PropertiesLoader.class.getSimpleName();

    private static boolean INITIALIZED = false;
    private static final Properties properties = new Properties();

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
}
