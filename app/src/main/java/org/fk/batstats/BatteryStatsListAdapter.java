// Copyright (C) 2020-2021 Furkan Karcıoğlu <https://github.com/frknkrc44>
//
// This file is part of BatStats project,
// and licensed under GNU Affero General Public License v3.
// See the GNU Affero General Public License for more details.
//
// All rights reserved. See COPYING, AUTHORS.
//

package org.fk.batstats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.List;

public class BatteryStatsListAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<HiddenApiUtils.SipperHolder> mSippers;
    private final HiddenApiUtils mHiddenApiUtils;

    private final int SECOND = 1000;
    private final int MINUTE = 60 * SECOND;
    private final int HOUR = 60 * MINUTE;

    public BatteryStatsListAdapter(Context ctx) {
        mContext = ctx;
        mHiddenApiUtils = new HiddenApiUtils(ctx);
        mSippers = mHiddenApiUtils.getMainUsageList();
    }

    @Override
    public int getCount() {
        return mSippers.size();
    }

    @Override
    public HiddenApiUtils.SipperHolder getItem(int position) {
        return mSippers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HiddenApiUtils.SipperHolder sipper = getItem(position);
        String title = "";
        switch (sipper.appName) {
            case HiddenApiUtils.SipperHolder.USAGE_SCREEN:
                title = "Screen";
                break;
            case HiddenApiUtils.SipperHolder.USAGE_LAST_CHARGE:
                title = "Last full charge";
                break;
            default:
                // TODO: Add app usage list
                break;
        }

        @SuppressLint("ViewHolder") TwoLineListItem twoLineListItem = (TwoLineListItem) LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2, parent, false);
        TextView tv1 = (TextView) twoLineListItem.findViewById(android.R.id.text1);
        tv1.setText(title);
        TextView tv2 = (TextView) twoLineListItem.findViewById(android.R.id.text2);
        tv2.setText(getFormattedTime(sipper.usageTimeMs));
        return twoLineListItem;
    }

    private String getFormattedTime(long ms){
        if (ms < 1) {
            return "N/A";
        }

        StringBuilder text = new StringBuilder();
        text.append(ms / HOUR).append("h ");
        ms %= HOUR;
        text.append(ms / MINUTE).append("m ");
        ms %= MINUTE;
        text.append(ms / SECOND).append("s ");
        // ms %= SECOND;
        // text.append(ms + "ms");
        return text.toString();
    }

    public void notifyDataSetChanged() {
        mSippers.clear();
        mHiddenApiUtils.refreshStats();
        mSippers.addAll(mHiddenApiUtils.getMainUsageList());
        super.notifyDataSetChanged();
    }

    public HiddenApiUtils getHiddenApiUtils() {
        return mHiddenApiUtils;
    }
}
