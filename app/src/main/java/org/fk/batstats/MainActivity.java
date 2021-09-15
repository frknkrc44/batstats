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
import android.widget.ListView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView mListView = new ListView(this);
        mListView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        BatteryStatsListAdapter mListAdapter = new BatteryStatsListAdapter(this);
        mListView.setAdapter(mListAdapter);
        setContentView(mListView);
        final Handler mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                mListAdapter.notifyDataSetChanged();
                removeMessages(0);
                sendEmptyMessageDelayed(0, 250);
            }
        };
        mHandler.sendEmptyMessage(0);
    }

}