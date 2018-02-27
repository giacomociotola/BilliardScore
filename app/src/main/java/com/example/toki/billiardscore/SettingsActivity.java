package com.example.toki.billiardscore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Toki on 24/02/2018.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class BilliardScorePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        public BilliardScorePreferenceFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference setPlayer1 = findPreference(getString(R.string.settings_player_1_name_key));
            bindPreferenceSummaryToValue(setPlayer1);

            Preference setPlayer2 = findPreference(getString(R.string.settings_player_2_name_key));
            bindPreferenceSummaryToValue(setPlayer2);

            Preference setPointsPerSet = findPreference(getString(R.string.settings_points_per_set_key));
            bindPreferenceSummaryToValue(setPointsPerSet);

            Preference setSetNumber = findPreference(getString(R.string.settings_set_number_key));
            bindPreferenceSummaryToValue(setSetNumber);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }


        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
