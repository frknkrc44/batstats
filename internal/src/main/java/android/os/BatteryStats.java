// Copyright (C) 2020-2021 Furkan Karcıoğlu <https://github.com/frknkrc44>
//
// This file is part of BatStats project,
// and licensed under GNU Affero General Public License v3.
// See the GNU Affero General Public License for more details.
//
// All rights reserved. See COPYING, AUTHORS.
//

package android.os;

// Copied from https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/os/BatteryStats.java
public abstract class BatteryStats {
    public static final int STATS_SINCE_CHARGED = 0;

    @Deprecated
    public static final int STATS_CURRENT = 1;

    @Deprecated
    public static final int STATS_SINCE_UNPLUGGED = 2;

    public abstract long getStartClockTime();
    public abstract long computeBatteryRealtime(long curTime, int which);
    public abstract boolean startIteratingHistoryLocked();
    public abstract boolean getNextHistoryLocked(HistoryItem out);
    public static abstract class Uid {}
    public static final class HistoryItem {
        public static final byte CMD_UPDATE = 0;        // These can be written as deltas
        public static final byte CMD_NULL = -1;
        public static final byte CMD_START = 4;
        public static final byte CMD_CURRENT_TIME = 5;
        public static final byte CMD_OVERFLOW = 6;
        public static final byte CMD_RESET = 7;
        public static final byte CMD_SHUTDOWN = 8;

        public static final int STATE_CPU_RUNNING_FLAG = 1<<31;
        public static final int STATE_WAKE_LOCK_FLAG = 1<<30;
        public static final int STATE_GPS_ON_FLAG = 1<<29;
        public static final int STATE_WIFI_FULL_LOCK_FLAG = 1<<28;
        public static final int STATE_WIFI_SCAN_FLAG = 1<<27;
        public static final int STATE_WIFI_RADIO_ACTIVE_FLAG = 1<<26;
        public static final int STATE_MOBILE_RADIO_ACTIVE_FLAG = 1<<25;
        // Do not use, this is used for coulomb delta count.
        private static final int STATE_RESERVED_0 = 1<<24;
        // These are on the lower bits used for the command; if they change
        // we need to write another int of data.
        public static final int STATE_SENSOR_ON_FLAG = 1<<23;
        public static final int STATE_AUDIO_ON_FLAG = 1<<22;
        public static final int STATE_PHONE_SCANNING_FLAG = 1<<21;
        public static final int STATE_SCREEN_ON_FLAG = 1<<20;       // consider moving to states2
        public static final int STATE_BATTERY_PLUGGED_FLAG = 1<<19; // consider moving to states2
        public static final int STATE_SCREEN_DOZE_FLAG = 1 << 18;
        // empty slot
        public static final int STATE_WIFI_MULTICAST_ON_FLAG = 1<<16;
        public static final int MOST_INTERESTING_STATES =
                STATE_BATTERY_PLUGGED_FLAG | STATE_SCREEN_ON_FLAG | STATE_SCREEN_DOZE_FLAG;

        public byte cmd = CMD_NULL;

        public long time;

        public byte batteryLevel;
        public byte batteryStatus;
        public byte batteryHealth;
        public byte batteryPlugType;
        public int states;

        public static final int STATE_PHONE_STATE_SHIFT = 6;
        public static final int STATE_PHONE_STATE_MASK = 0x7 << STATE_PHONE_STATE_SHIFT;

        public static final int STATE_PHONE_SIGNAL_STRENGTH_SHIFT = 3;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_MASK = 0x7 << STATE_PHONE_SIGNAL_STRENGTH_SHIFT;
    }
}
