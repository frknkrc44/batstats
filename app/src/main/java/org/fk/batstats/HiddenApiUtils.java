// Copyright (C) 2020-2021 Furkan Karcıoğlu <https://github.com/frknkrc44>
//
// This file is part of BatStats project,
// and licensed under GNU Affero General Public License v3.
// See the GNU Affero General Public License for more details.
//
// All rights reserved. See COPYING, AUTHORS.
//

package org.fk.batstats;

import static android.content.Context.USER_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.BatteryStats;
import android.os.UserHandle;
import android.os.UserManager;

import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatteryStatsHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HiddenApiUtils {
    private final Context mContext;
    private final BatteryStatsHelper mBatteryStats;
    private List<UserHandle> userCache;

    HiddenApiUtils(Context context) {
        tryToBypassRestrictions();
        mContext = context;
        mBatteryStats = new BatteryStatsHelper(context, true);
        mBatteryStats.create(null);
    }

    void refreshStats() {
        mBatteryStats.clearStats();
        if(userCache == null) {
            UserManager userManager = (UserManager) mContext.getSystemService(USER_SERVICE);
            userCache = userManager.getUserProfiles();
        }
        mBatteryStats.refreshStats(0, userCache);
    }

    long calculateScreenUsageTime() {
        BatterySipper sipper = null;
        try {
            sipper = findBatterySipperByType(mBatteryStats.getUsageList(), BatterySipper.DrainType.SCREEN);
        } catch (Throwable t) {
            try {
                Object[] objs = BatterySipper.DrainType.class.getEnumConstants();
                if (objs == null) throw new RuntimeException("objs is null");

                Object screenEnum = null;
                for(Object o : objs) {
                    if(o.toString().contains("SCREEN")) {
                        screenEnum = o;
                        break;
                    }
                }

                if (screenEnum != null)
                    sipper = findBatterySipperByType(mBatteryStats.getUsageList(), screenEnum);
            } catch (Throwable x) {
                // ignore
            }
        }
        if (sipper != null) return sipper.usageTimeMs;
        return -1;
    }

    @SuppressLint("SoonBlockedPrivateApi")
    long getStartClockTime() {
        BatteryStats stats = mBatteryStats.getStats();

        try {
            return stats.getStartClockTime();
        } catch (Throwable t) {
            try {
                Field field = stats.getClass().getDeclaredField("mStartClockTimeMs");
                field.setAccessible(true);
                return field.getLong(stats);
            } catch (Throwable x){
                // ignored
            }

            return System.currentTimeMillis();
        }
    }

    void tryToBypassRestrictions() {
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            assert getRuntime != null;
            Object vmRuntime = getRuntime.invoke(null);
            assert setHiddenApiExemptions != null;
            setHiddenApiExemptions.invoke(vmRuntime, (Object) new String[]{"L"});
        } catch(Throwable t) {
            // ignored
        }
    }

    long calculateLastFullChargeTime() {
        return System.currentTimeMillis() - getStartClockTime();
    }

    List<SipperHolder> getMainUsageList() {
        List<SipperHolder> sipperList = new ArrayList<>();
        sipperList.add(new SipperHolder(SipperHolder.USAGE_SCREEN, calculateScreenUsageTime()));
        sipperList.add(new SipperHolder(SipperHolder.USAGE_LAST_CHARGE, calculateLastFullChargeTime()));
        return sipperList;
    }

    BatterySipper findBatterySipperByType(List<BatterySipper> usageList, Object type) {
        for(int i = 0;i < usageList.size();i++) {
            BatterySipper sipper = usageList.get(i);
            if(sipper.drainType == type) {
                return sipper;
            }
        }
        return null;
    }

    public static class SipperHolder {
        public static final String USAGE_SCREEN = ":sot",
                USAGE_LAST_CHARGE = ":lc";
        final String appName;
        final long usageTimeMs;

        private SipperHolder(String appName, long usageTimeMs) {
            this.appName = appName;
            this.usageTimeMs = usageTimeMs;
        }
    }
}
