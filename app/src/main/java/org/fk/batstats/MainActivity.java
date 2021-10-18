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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.settings.fuelgauge.BatteryHistoryChart;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private boolean mPaused = false;
    private SharedPreferences sprefs;
    BatteryHistoryChart mBatteryChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        BatteryStatsListAdapter mListAdapter = new BatteryStatsListAdapter(this);
        mBatteryChart = new BatteryHistoryChart(this, null);
        sprefs = DatabaseUtils.getInstance(this, this);
        mBatteryChart.setShowDetails(DatabaseUtils.isShowDetailsEnabled(sprefs));
        float density = getResources().getDisplayMetrics().density;
        mBatteryChart.setStats(mListAdapter.getHiddenApiUtils().getStats());
        mBatteryChart.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1));
        mBatteryChart.setTextSize((int) (density * 16));
        mainLayout.addView(mBatteryChart);
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
                    mBatteryChart.setStats(mListAdapter.getHiddenApiUtils().getStats());
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String s) {
        switch (s) {
            case DatabaseUtils.FUELGAUGE_SHOW_DETAILS:
                mBatteryChart.setShowDetails(sp.getBoolean(s, false));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}