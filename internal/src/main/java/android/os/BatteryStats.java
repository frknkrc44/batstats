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
    public abstract long getStartClockTime();
    public static abstract class Uid {}
}
