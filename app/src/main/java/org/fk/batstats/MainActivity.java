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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.settings.fuelgauge.BatteryHistoryChart;

public class MainActivity extends Activity {
    private boolean mPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        BatteryStatsListAdapter mListAdapter = new BatteryStatsListAdapter(this);
        BatteryHistoryChart batteryChart = new BatteryHistoryChart(this, null);
        float density = getResources().getDisplayMetrics().density;
        batteryChart.setStats(mListAdapter.getHiddenApiUtils().getStats());
        batteryChart.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1));
        batteryChart.setTextSize((int) (density * 16));
        mainLayout.addView(batteryChart);
        ListView mListView = new ListView(this);
        mListView.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 0));
        mListView.setAdapter(mListAdapter);
        mainLayout.addView(mListView);
        setContentView(mainLayout);
        final Handler mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if(!mPaused) {
                    mListAdapter.notifyDataSetChanged();
                    batteryChart.setStats(mListAdapter.getHiddenApiUtils().getStats());
                }
                removeMessages(0);
                sendEmptyMessageAtTime(0, msg.getWhen() + 1000);
            }
        };
        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onPause() {
        mPaused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        mPaused = false;
        super.onResume();
    }
}