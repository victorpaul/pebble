package com.sukinsan.pebble.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sukinsan.pebble.entity.Cache;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by victorpaul on 2/2/15.
 */
public class SystemUtils {
    public static final String TAG = SystemUtils.class.getSimpleName();

    public static final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static Cache CACHE;

    public static void saveCache(Context context) {
        Log.i(TAG, "Save cache: " + CACHE.toString());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            sharedPreferences.edit().remove(Cache.class.getName()).commit();
            sharedPreferences.edit().putString(Cache.class.getName(), MAPPER.writeValueAsString(CACHE)).commit();
        } catch (Exception e) {
            Log.e(TAG, "save CACHE", e);
        }
    }

    public static synchronized void getCache(Context context,Cache.CallBack callback) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!sharedPreferences.getString(Cache.class.getName(), "").isEmpty()) {
            try {
                CACHE = MAPPER.readValue(sharedPreferences.getString(Cache.class.getName(), ""), Cache.class);
            } catch (Exception e) {
                Log.e(TAG, "get CACHE", e);
            }
        }
        if (CACHE == null) {
            CACHE = new Cache();
        }

        if(callback != null){
            if(callback.run(CACHE)){
                saveCache(context);
            }
        }
    }
}