// Copyright (C) 2020-2021 Furkan Karcıoğlu <https://github.com/frknkrc44>
//
// This file is part of BatStats project,
// and licensed under GNU Affero General Public License v3.
// See the GNU Affero General Public License for more details.
//
// All rights reserved. See COPYING, AUTHORS.
//

package com.android.internal.os;

import android.content.Context;
import android.os.BatteryStats;
import android.os.Bundle;
import android.os.UserHandle;

import java.util.List;

// Copied from https://android.googlesource.com/platform/frameworks/base/+/master/core/java/com/android/internal/os/BatteryStatsHelper.java
public class BatteryStatsHelper {
    public BatteryStatsHelper(Context ctx, boolean b) {}

    public void create(Bundle b) {}

    public void clearStats() {}

    public BatteryStats getStats() {
        return null;
    }

    public void refreshStats(int state, List<UserHandle> asUsers) {}

    public List<BatterySipper> getUsageList() {
        return null;
    }

}
