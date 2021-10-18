package org.fk.batstats;

import android.content.Context;
import android.content.SharedPreferences;

public class DatabaseUtils {
    private DatabaseUtils() {}

    public static final String FUELGAUGE_SHOW_DETAILS = "fuelgauge_show_details";

    public static SharedPreferences getInstance(Context context) {
        return getInstance(context, null);
    }

    public static SharedPreferences getInstance(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences spref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (listener != null)
            spref.registerOnSharedPreferenceChangeListener(listener);
        return spref;
    }

    public static boolean isShowDetailsEnabled(SharedPreferences spref) {
        return spref.getBoolean(FUELGAUGE_SHOW_DETAILS, false);
    }

    public static boolean setShowDetailsEnabled(SharedPreferences spref, boolean value) {
        return putBoolean(spref, FUELGAUGE_SHOW_DETAILS, value);
    }

    public static boolean putBoolean(SharedPreferences spref, String key, boolean value) {
        return spref.edit().putBoolean(key, value).commit();
    }
}
