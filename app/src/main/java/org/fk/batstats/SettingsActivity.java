package org.fk.batstats;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends Activity {
    private SharedPreferences sprefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sprefs = DatabaseUtils.getInstance(this);
        Switch showDetails = findViewById(R.id.fuelgauge_show_details);
        showDetails.setChecked(DatabaseUtils.isShowDetailsEnabled(sprefs));
        showDetails.setOnCheckedChangeListener((cb, val) -> DatabaseUtils.setShowDetailsEnabled(sprefs, val));
    }

}