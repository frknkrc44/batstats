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

    public static final int WIFI_SUPPL_STATE_INVALID = 0;
    public static final int WIFI_SUPPL_STATE_DISCONNECTED = 1;
    public static final int WIFI_SUPPL_STATE_INTERFACE_DISABLED = 2;
    public static final int WIFI_SUPPL_STATE_INACTIVE = 3;
    public static final int WIFI_SUPPL_STATE_SCANNING = 4;
    public static final int WIFI_SUPPL_STATE_AUTHENTICATING = 5;
    public static final int WIFI_SUPPL_STATE_ASSOCIATING = 6;
    public static final int WIFI_SUPPL_STATE_ASSOCIATED = 7;
    public static final int WIFI_SUPPL_STATE_FOUR_WAY_HANDSHAKE = 8;
    public static final int WIFI_SUPPL_STATE_GROUP_HANDSHAKE = 9;
    public static final int WIFI_SUPPL_STATE_COMPLETED = 10;
    public static final int WIFI_SUPPL_STATE_DORMANT = 11;
    public static final int WIFI_SUPPL_STATE_UNINITIALIZED = 12;

    public abstract long getStartClockTime();
    public abstract long computeBatteryRealtime(long curTime, int which);
    public abstract boolean startIteratingHistoryLocked();
    public abstract boolean getNextHistoryLocked(HistoryItem out);
    public abstract void finishIteratingHistoryLocked();
    public abstract long computeBatteryTimeRemaining(long curTime);
    public abstract long computeChargeTimeRemaining(long curTime);

    public static abstract class Uid {
        /**
         * Time this uid has any processes in the top state.
         */
        public static final int PROCESS_STATE_TOP = 0;
        /**
         * Time this uid has any process with a started foreground service, but
         * none in the "top" state.
         */
        public static final int PROCESS_STATE_FOREGROUND_SERVICE = 1;
        /**
         * Time this uid has any process in an active foreground state, but none in the
         * "foreground service" or better state. Persistent and other foreground states go here.
         */
        public static final int PROCESS_STATE_FOREGROUND = 2;
        /**
         * Time this uid has any process in an active background state, but none in the
         * "foreground" or better state.
         */
        public static final int PROCESS_STATE_BACKGROUND = 3;
        /**
         * Time this uid has any process that is top while the device is sleeping, but not
         * active for any other reason.  We kind-of consider it a kind of cached process
         * for execution restrictions.
         */
        public static final int PROCESS_STATE_TOP_SLEEPING = 4;
        /**
         * Time this uid has any process that is in the background but it has an activity
         * marked as "can't save state".  This is essentially a cached process, though the
         * system will try much harder than normal to avoid killing it.
         */
        public static final int PROCESS_STATE_HEAVY_WEIGHT = 5;
        /**
         * Time this uid has any processes that are sitting around cached, not in one of the
         * other active states.
         */
        public static final int PROCESS_STATE_CACHED = 6;

        public abstract int getUid();
        public abstract long getProcessStateTime(int state, long elapsedRealtimeUs, int which);
        public abstract Timer getForegroundActivityTimer();
        public abstract Timer getForegroundServiceTimer();
    }

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

        public static final int STATE2_POWER_SAVE_FLAG = 1<<31;
        public static final int STATE2_VIDEO_ON_FLAG = 1<<30;
        public static final int STATE2_WIFI_RUNNING_FLAG = 1<<29;
        public static final int STATE2_WIFI_ON_FLAG = 1<<28;
        public static final int STATE2_FLASHLIGHT_FLAG = 1<<27;
        public static final int STATE2_DEVICE_IDLE_SHIFT = 25;
        public static final int STATE2_DEVICE_IDLE_MASK = 0x3 << STATE2_DEVICE_IDLE_SHIFT;
        public static final int STATE2_CHARGING_FLAG = 1<<24;
        public static final int STATE2_PHONE_IN_CALL_FLAG = 1<<23;
        public static final int STATE2_BLUETOOTH_ON_FLAG = 1<<22;
        public static final int STATE2_CAMERA_FLAG = 1<<21;
        public static final int STATE2_BLUETOOTH_SCAN_FLAG = 1 << 20;
        public static final int STATE2_CELLULAR_HIGH_TX_POWER_FLAG = 1 << 19;
        public static final int STATE2_USB_DATA_LINK_FLAG = 1 << 18;

        public byte cmd = CMD_NULL;

        public long time;
        public long currentTime;

        public byte batteryLevel;
        public byte batteryStatus;
        public byte batteryHealth;
        public byte batteryPlugType;
        public int states;
        public int states2;

        public static final int STATE2_WIFI_SUPPL_STATE_SHIFT = 0;
        public static final int STATE2_WIFI_SUPPL_STATE_MASK = 0xf;

        public static final int STATE_PHONE_STATE_SHIFT = 6;
        public static final int STATE_PHONE_STATE_MASK = 0x7 << STATE_PHONE_STATE_SHIFT;

        public static final int STATE_PHONE_SIGNAL_STRENGTH_SHIFT = 3;
        public static final int STATE_PHONE_SIGNAL_STRENGTH_MASK = 0x7 << STATE_PHONE_SIGNAL_STRENGTH_SHIFT;

        public boolean isDeltaData() {
            return cmd == CMD_UPDATE;
        }
    }

    public static abstract class Timer {
        public abstract long getTotalTimeLocked(long elapsedRealtimeUs, int which);
    }
}
