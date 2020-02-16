package com.mancel.yann.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Yann MANCEL on 16/02/2020.
 * Name of the project: Go4Lunch
 * Name of the package: com.mancel.yann.go4lunch.utils
 */
public abstract class SaveUtils {

    // FIELDS --------------------------------------------------------------------------------------

    private static final String SAVE_FILE_NAME = "com.mancel.yann.go4lunch.SAVE_FILE_NAME";

    // METHODS -------------------------------------------------------------------------------------

    // -- SharedPreferences --

    /**
     * Saves a boolean thanks to {@link SharedPreferences}
     * @param context   a {@link Context}
     * @param key       a {@link String} that contains the key
     * @param value     a boolean that contains the value
     */
    public static void saveBooleanIntoSharedPreferences(@Nullable final Context context,
                                                        @NonNull final String key,
                                                        final boolean value) {
        // No context
        if (context == null) {
            return;
        }

        // SharedPreferences
        final SharedPreferences preferences = context.getSharedPreferences(SAVE_FILE_NAME,
                                                                           Context.MODE_PRIVATE);
        // Save
        preferences.edit()
                   .putBoolean(key, value)
                   .apply();
    }

    /**
     * Retrieves a boolean from {@link SharedPreferences}
     * @param context   a {@link Context}
     * @param key       a {@link String} that contains the key
     * @return a boolean that contains the value
     */
    public static boolean loadBooleanFromSharedPreferences(@Nullable final Context context,
                                                           @NonNull final String key) {
        // No context
        if (context == null) {
            return true;
        }

        // SharedPreferences
        final SharedPreferences preferences = context.getSharedPreferences(SAVE_FILE_NAME,
                                                                           Context.MODE_PRIVATE);

        return preferences.getBoolean(key, true);
    }
}
