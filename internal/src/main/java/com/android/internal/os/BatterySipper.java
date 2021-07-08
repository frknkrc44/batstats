// Copyright (C) 2020-2021 Furkan Karcıoğlu <https://github.com/frknkrc44>
//
// This file is part of BatStats project,
// and licensed under GNU Affero General Public License v3.
// See the GNU Affero General Public License for more details.
//
// All rights reserved. See COPYING, AUTHORS.
//

package com.android.internal.os;

import android.os.BatteryStats;

// Copied from https://android.googlesource.com/platform/frameworks/base/+/master/core/java/com/android/internal/os/BatterySipper.java
public class BatterySipper {
    // public double screenPowerMah;
    public DrainType drainType;
    public long usageTimeMs;
    // public BatteryStats.Uid uidObj;

    public enum DrainType {
        AMBIENT_DISPLAY,
        APP,
        BLUETOOTH,
        CAMERA,
        CELL,
        FLASHLIGHT,
        IDLE,
        MEMORY,
        OVERCOUNTED,
        PHONE,
        SCREEN,
        UNACCOUNTED,
        USER,
        WIFI,
    }

    public BatterySipper(DrainType drainType, BatteryStats.Uid uid, double value) {}

/*
    public int getUid() {
        return 0;
    }
*/
}
