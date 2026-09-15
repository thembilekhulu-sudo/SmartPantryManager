package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "pantry_settings";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    public static final String KEY_METRIC_UNITS = "metric_units";

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Switch switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        Switch switchMetricUnits = findViewById(R.id.switchMetricUnits);

        // Load saved values (default: alerts on, metric units on)
        switchExpiryAlerts.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true));
        switchMetricUnits.setChecked(prefs.getBoolean(KEY_METRIC_UNITS, true));

        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply();
            Toast.makeText(this, isChecked ? "Expiry alerts on" : "Expiry alerts off", Toast.LENGTH_SHORT).show();
        });

        switchMetricUnits.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_METRIC_UNITS, isChecked).apply();
            Toast.makeText(this, isChecked ? "Metric units on" : "Metric units off", Toast.LENGTH_SHORT).show();
        });
    }
}