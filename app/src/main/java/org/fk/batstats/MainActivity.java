// Copyright (C) 2020-2021 Furkan Karcıoğlu <https://github.com/frknkrc44>
//
// This file is part of BatStats project,
// and licensed under GNU Affero General Public License v3.
// See the GNU Affero General Public License for more details.
//
// All rights reserved. See COPYING, AUTHORS.
//

package org.fk.batstats;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatterySipper.DrainType;
import com.android.internal.os.BatteryStatsHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private BatteryStatsHelper mBatteryStats;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                showUsageTime();
                removeMessages(0);
                sendEmptyMessageDelayed(0, 250);
            }
        };
        mHandler.sendEmptyMessage(0);
    }

    private void showUsageTime() {
        if(mBatteryStats == null) {
            mBatteryStats = new BatteryStatsHelper(this, true);
            mBatteryStats.create(null);
            refreshStats();
            ListView mListView = new ListView(this);
            mListView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            setContentView(mListView);
            mListView.setAdapter(mListAdapter = new BatteryStatsListAdapter(this, getMainUsageList()));
        } else {
            refreshStats();
            ((BatteryStatsListAdapter) mListAdapter).notifyDataSetChanged(getMainUsageList());
        }
    }

    private void refreshStats() {
        mBatteryStats.clearStats();
        UserManager userManager = (UserManager) getSystemService(USER_SERVICE);
        mBatteryStats.refreshStats(0, userManager.getUserProfiles());
    }

    private long calculateScreenUsageTime() {
        BatterySipper sipper = findBatterySipperByType(mBatteryStats.getUsageList(), DrainType.SCREEN);
        if (sipper != null) return sipper.usageTimeMs;
        return -1;
    }

    private long calculateLastFullChargeTime() {
        return System.currentTimeMillis() - mBatteryStats.getStats().getStartClockTime();
    }

    private List<SipperHolder> getMainUsageList() {
        List<SipperHolder> sipperList = new ArrayList<>();
        sipperList.add(new SipperHolder(SipperHolder.USAGE_SCREEN, calculateScreenUsageTime()));
        sipperList.add(new SipperHolder(SipperHolder.USAGE_LAST_CHARGE, calculateLastFullChargeTime()));
        return sipperList;
    }

    private BatterySipper findBatterySipperByType(List<BatterySipper> usageList, DrainType type) {
        for(int i = 0;i < usageList.size();i++) {
            BatterySipper sipper = usageList.get(i);
            if(sipper.drainType == type) {
                return sipper;
            }
        }
        return null;
    }

    static class SipperHolder {
        public static final String USAGE_SCREEN = ":sot",
                                    USAGE_LAST_CHARGE = ":lc";
        final String appName;
        final long usageTimeMs;
/*
        private SipperHolder(BatterySipper sipper) {
            this(
                    sipper.drainType == DrainType.SCREEN
                            ? USAGE_SCREEN
                            : String.valueOf(sipper.getUid()),
                    sipper.usageTimeMs
            );
        }
*/
        private SipperHolder(String appName, long usageTimeMs) {
            this.appName = appName;
            this.usageTimeMs = usageTimeMs;
        }
    }

}