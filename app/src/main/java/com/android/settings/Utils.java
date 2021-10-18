package com.android.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.text.NumberFormat;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;

import org.fk.batstats.R;

import com.android.settingslib.fuelgauge.BatteryStatus;

import java.util.Locale;

public class Utils {
    public static final int[] BADNESS_COLORS = new int[] {
            0x00000000, 0xffc43828, 0xffe54918, 0xfff47b00,
            0xfffabf2c, 0xff679e37, 0xff0a7f42
    };

    public static boolean isWifiOnly(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        for (Network net : cm.getAllNetworks()) {
            if (cm.getNetworkCapabilities(net).hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return false;
            }
        }
        return true;
    }

    public static String formatPercentage(int percentage) {
        return formatPercentage(((double) percentage) / 100.0);
    }

    public static String formatPercentage(double percentage) {
        return NumberFormat.getPercentInstance().format(percentage);
    }

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = 60 * 60;
    private static final int SECONDS_PER_DAY = 24 * 60 * 60;

    /**
     * Returns elapsed time for the given millis, in the following format:
     * 2d 5h 40m 29s
     * @param context the application context
     * @param millis the elapsed time in milli seconds
     * @return the formatted elapsed time
     */
    public static String formatElapsedTime(Context context, double millis) {
        StringBuilder sb = new StringBuilder();
        int seconds = (int) Math.floor(millis / 1000);

        int days = 0, hours = 0, minutes = 0;
        if (seconds > SECONDS_PER_DAY) {
            days = seconds / SECONDS_PER_DAY;
            seconds -= days * SECONDS_PER_DAY;
        }
        if (seconds > SECONDS_PER_HOUR) {
            hours = seconds / SECONDS_PER_HOUR;
            seconds -= hours * SECONDS_PER_HOUR;
        }
        if (seconds > SECONDS_PER_MINUTE) {
            minutes = seconds / SECONDS_PER_MINUTE;
            seconds -= minutes * SECONDS_PER_MINUTE;
        }
        if (days > 0) {
            sb.append(context.getString(R.string.battery_history_days,
                    days, hours, minutes, seconds));
        } else if (hours > 0) {
            sb.append(context.getString(R.string.battery_history_hours, hours, minutes, seconds));
        } else if (minutes > 0) {
            sb.append(context.getString(R.string.battery_history_minutes, minutes, seconds));
        } else {
            sb.append(context.getString(R.string.battery_history_seconds, seconds));
        }
        return sb.toString();
    }

    public static int getColorAccentInt(Context context) {
        return getColorAccent(context).getDefaultColor();
    }

    public static ColorStateList getColorAccent(Context context) {
        return getColorAttr(context, android.R.attr.colorAccent);
    }

    public static ColorStateList getColorAttr(Context context, int attr) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        ColorStateList stateList = null;
        try {
            stateList = ta.getColorStateList(0);
        } finally {
            ta.recycle();
        }
        return stateList;
    }

    public static int getBatteryLevel(Intent batteryChangedIntent) {
        int level = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return (level * 100) / scale;
    }

    public static String getBatteryStatus(Context context, Intent batteryChangedIntent) {
        final Resources res = context.getResources();
        return getBatteryStatus(res, batteryChangedIntent);
    }

    public static String getBatteryStatus(Resources res, Intent batteryChangedIntent) {
        final int status = batteryChangedIntent.getIntExtra(BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN);

        String statusString = res.getString(R.string.battery_info_status_unknown);
        final BatteryStatus batteryStatus = new com.android.settingslib.fuelgauge.BatteryStatus(batteryChangedIntent);

        if (batteryStatus.isCharged()) {
            statusString = res.getString(R.string.battery_info_status_full);
        } else {
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                if (batteryStatus.isPluggedInWired()) {
                    switch (batteryStatus.getChargingSpeed()) {
                        case BatteryStatus.CHARGING_FAST:
                            statusString = res.getString(
                                    R.string.battery_info_status_charging_fast);
                            break;
                        case BatteryStatus.CHARGING_SLOWLY:
                            statusString = res.getString(
                                    R.string.battery_info_status_charging_slow);
                            break;
                        default:
                            statusString = res.getString(R.string.battery_info_status_charging);
                            break;
                    }
                } else {
                    statusString = res.getString(R.string.battery_info_status_charging_wireless);
                }
            } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                statusString = res.getString(R.string.battery_info_status_discharging);
            } else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                statusString = res.getString(R.string.battery_info_status_not_charging);
            }
        }

        return statusString;
    }
}
