package cu.IntegratedLanguages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;


public class PreferencesPlurilingual extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener{


    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {



    }
}
