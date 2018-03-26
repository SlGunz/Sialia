package com.slgunz.root.sialia.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;

import com.slgunz.root.sialia.R;
import com.slgunz.root.sialia.data.service.PollService;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(SettingsActivity.this,
                    R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SwitchCompat switcherNotification = findViewById(R.id.switcher_show_notification);
        switcherNotification.setChecked(PollService.isAlarmOn(this));
        switcherNotification.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            PollService.startServiceAlarm(this, isChecked);
        });
    }
}
