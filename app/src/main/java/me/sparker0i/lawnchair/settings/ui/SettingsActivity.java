/*
 * Copyright (C) 2015 The Android Open Source Project
 * Copyright (C) 2017 The MoKee Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.sparker0i.lawnchair.settings.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;

import me.sparker0i.lawnchair.BuildConfig;
import me.sparker0i.lawnchair.DumbImportExportTask;
import me.sparker0i.lawnchair.LauncherAppState;
import me.sparker0i.lawnchair.LauncherFiles;
import me.sparker0i.lawnchair.R;
import me.sparker0i.lawnchair.Utilities;
import me.sparker0i.lawnchair.blur.BlurWallpaperProvider;
import me.sparker0i.lawnchair.config.FeatureFlags;
import me.sparker0i.lawnchair.graphics.IconShapeOverride;
import me.sparker0i.lawnchair.preferences.IPreferenceProvider;
import me.sparker0i.lawnchair.preferences.PreferenceFlags;
import me.sparker0i.lock.activity.LockSettingsActivity;
import me.sparker0i.lock.preferences.Preferences;
import me.sparker0i.lock.service.ScreenService;
import me.sparker0i.question.activity.CategoryChooser;

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 */
public class SettingsActivity extends AppCompatActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    private static IPreferenceProvider sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FeatureFlags.INSTANCE.applyDarkTheme(this);
        super.onCreate(savedInstanceState);

        if (FeatureFlags.INSTANCE.getCurrentTheme() != 2)
            BlurWallpaperProvider.Companion.applyBlurBackground(this);

        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new LauncherSettingsFragment())
                    .commit();
        }

        sharedPrefs = Utilities.getPrefs(this);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        updateUpButton();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragment caller, Preference pref) {
        if (pref instanceof SubPreference) {
            Fragment fragment = SubSettingsFragment.newInstance(((SubPreference) pref));
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            setTitle(pref.getTitle());
            transaction.setCustomAnimations(R.animator.fly_in, R.animator.fade_out, R.animator.fade_in, R.animator.fly_out);
            transaction.replace(android.R.id.content, fragment);
            transaction.addToBackStack("PreferenceFragment");
            transaction.commit();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUpButton();
    }

    private void updateUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getFragmentManager().getBackStackEntryCount() != 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (FeatureFlags.KEY_PREF_THEME.equals(key)) {
            FeatureFlags.INSTANCE.loadThemePreference(this);
            recreate();
        }
    }

    private abstract static class BaseFragment extends PreferenceFragment implements AdapterView.OnItemLongClickListener {

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if (view == null) return null;
            ListView listView = view.findViewById(android.R.id.list);
            listView.setOnItemLongClickListener(this);
            return view;
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView) parent;
            ListAdapter listAdapter = listView.getAdapter();
            Object item = listAdapter.getItem(position);

            if (item instanceof SubPreference) {
                SubPreference subPreference = (SubPreference) item;
                if (subPreference.onLongClick(null)) {
                    ((SettingsActivity) getActivity()).onPreferenceStartFragment(this, subPreference);
                    return true;
                } else {
                    return false;
                }
            }
            return item != null && item instanceof View.OnLongClickListener && ((View.OnLongClickListener) item).onLongClick(view);
        }
    }

    /**
     * This fragment shows the launcher preferences.
     */
    public static class LauncherSettingsFragment extends BaseFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(LauncherFiles.SHARED_PREFERENCES_KEY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                addPreferencesFromResource(R.xml.launcher_preferences);
            else
                addPreferencesFromResource(R.xml.launcher_preferences_upto_22);
        }

        @Override
        public void onResume() {
            super.onResume();
            getActivity().setTitle(R.string.settings_button_text);
        }
    }

    public static class SubSettingsFragment extends BaseFragment implements Preference.OnPreferenceChangeListener {

        private static final String TITLE = "title";
        private static final String CONTENT_RES_ID = "content_res_id";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName(LauncherFiles.SHARED_PREFERENCES_KEY);
            addPreferencesFromResource(getContent());
            if (getContent() == R.xml.launcher_pixel_style_preferences) {
                Preference prefWeatherEnabled = findPreference(FeatureFlags.KEY_PREF_WEATHER);
                prefWeatherEnabled.setOnPreferenceChangeListener(this);
                Preference prefWeatherProvider = findPreference(PreferenceFlags.KEY_WEATHER_PROVIDER);
                prefWeatherProvider.setEnabled(BuildConfig.AWARENESS_API_ENABLED);
                prefWeatherProvider.setOnPreferenceChangeListener(this);
                updateEnabledState(Utilities.getPrefs(getActivity()).getWeatherProvider());
                Preference overrideShapePreference = findPreference(PreferenceFlags.KEY_OVERRIDE_ICON_SHAPE);
                if (IconShapeOverride.Companion.isSupported(getActivity())) {
                    IconShapeOverride.Companion.handlePreferenceUi((ListPreference) overrideShapePreference);
                } else {
                    ((PreferenceCategory) findPreference("prefCat_homeScreen"))
                            .removePreference(overrideShapePreference);
                }
                if (Utilities.ATLEAST_NOUGAT) {
                    ((PreferenceCategory) findPreference("prefCat_homeScreen"))
                        .removePreference(findPreference(PreferenceFlags.KEY_PREF_PIXEL_STYLE_ICONS));
                }
            }
            else if (getContent() == R.xml.launcher_lock_preferences) {
                SwitchPreference prefLockEnabled = (SwitchPreference) findPreference(FeatureFlags.KEY_ENABLE_LOCK);
                prefLockEnabled.setOnPreferenceChangeListener(this);
            }
            else if (getContent() == R.xml.launcher_about_preferences) {
                findPreference("about_version").setSummary(BuildConfig.VERSION_NAME);
                if (BuildConfig.TRAVIS && !BuildConfig.TAGGED_BUILD) {
                    findPreference("about_changelog").setSummary(Utilities.getChangelog());
                }
            } else if (getContent() == R.xml.launcher_behavior_preferences) {
                if (Utilities.ATLEAST_NOUGAT_MR1 && BuildConfig.TRAVIS) {
                    getPreferenceScreen().removePreference(findPreference(FeatureFlags.KEY_PREF_ENABLE_BACKPORT_SHORTCUTS));
                }
            } else if (getContent() == R.xml.launcher_hidden_preferences) {
                Preference eminemPref = findPreference("random_eminem_quote");
                String[] eminemQuotes = getResources().getStringArray(R.array.eminem_quotes);
                int index = new Random().nextInt(eminemQuotes.length);
                eminemPref.setSummary(eminemQuotes[index]);
            }
        }

        private void updateEnabledState(String weatherProvider) {
            boolean awarenessApiEnabled = weatherProvider.equals(PreferenceFlags.PREF_WEATHER_PROVIDER_AWARENESS);
            Preference prefWeatherCity = findPreference(PreferenceFlags.KEY_WEATHER_CITY);
            Preference prefWeatherApiKey = findPreference(PreferenceFlags.KEY_WEATHER_API_KEY);
            prefWeatherCity.setEnabled(!awarenessApiEnabled);
            prefWeatherApiKey.setEnabled(!awarenessApiEnabled);
        }

        private boolean isMyServiceRunning(Context context , Class<?> serviceClass) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey() != null) {
                switch (preference.getKey()) {
                    case PreferenceFlags.KEY_WEATHER_PROVIDER:
                        updateEnabledState((String) newValue);
                        break;
                    case FeatureFlags.KEY_PREF_WEATHER:
                        Context context = getActivity();
                        if (Utilities.getPrefs(context).getShowWeather() && Utilities.isAwarenessApiEnabled(context)) {
                            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                        break;
                    case FeatureFlags.KEY_ENABLE_LOCK:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!((SwitchPreference) preference).isChecked()) {
                                if (me.sparker0i.question.Utilities.isLockEnabled(getContext())) {
                                    ((SwitchPreference) preference).setChecked(false);
                                    showLockEnabled();
                                }
                                else {
                                    SwitchPreference prefLockEnabled = (SwitchPreference) findPreference(FeatureFlags.KEY_ENABLE_LOCK);
                                    if (!prefLockEnabled.isChecked())
                                        getContext().startService(new Intent(getContext(), ScreenService.class));
                                    else
                                        getContext().stopService(new Intent(getContext(), ScreenService.class));
                                }
                            } else {
                                getContext().stopService(new Intent(getContext(), ScreenService.class));
                            }
                        }
                        break;
                }
                return true;
            }
            return false;
        }

        public void showLockEnabled() {
            final MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                    .title("Lock Screen Enabled")
                    .content("Please Disable Your Android Lock Screen before you continue")
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivityForResult(new Intent(Settings.ACTION_SECURITY_SETTINGS) , 101);
                        }
                    })
                    .build();
            dialog.show();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.i("Yeah" , "Called");
            if (requestCode == 101) {
                if (me.sparker0i.question.Utilities.isLockEnabled(getContext())) {
                    getPreferenceManager().setSharedPreferencesName(LauncherFiles.SHARED_PREFERENCES_KEY);
                    SwitchPreference prefLockEnabled = (SwitchPreference) findPreference(FeatureFlags.KEY_ENABLE_LOCK);
                    prefLockEnabled.setChecked(false);
                }
                else {
                    getContext().startService(new Intent(getContext() , ScreenService.class));
                }
            }
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if (preference.getKey() != null) {
                switch (preference.getKey()) {
                    case "kill":
                        LauncherAppState.getInstance().getLauncher().scheduleKill();
                        break;
                    case "rebuild_icondb":
                        LauncherAppState.getInstance().getLauncher().scheduleReloadIcons();
                        break;
                    case "export_db":
                        if (checkStoragePermission())
                            DumbImportExportTask.exportDB(getActivity());
                        break;
                    case "import_db":
                        if (checkStoragePermission()) {
                            DumbImportExportTask.importDB(getActivity());
                            LauncherAppState.getInstance().getLauncher().scheduleKill();
                        }
                        break;
                    case "export_prefs":
                        if (checkStoragePermission())
                            DumbImportExportTask.exportPrefs(getActivity());
                        break;
                    case "import_prefs":
                        if (checkStoragePermission()) {
                            DumbImportExportTask.importPrefs(getActivity());
                            LauncherAppState.getInstance().getLauncher().scheduleKill();
                        }
                        break;
                    case PreferenceFlags.KEY_WEATHER_PROVIDER:
                        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Toast.makeText(getActivity(), R.string.location_permission_warn, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "about_translators":
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setTitle(R.string.about_translators);
                        dialog.setContentView(R.layout.dialog_translators);
                        dialog.show();
                        break;
                    case FeatureFlags.KEY_LOAD_QUESTIONS:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            new me.sparker0i.question.Utilities(getContext()).loadQuestions();
                        else
                            startActivity(new Intent(getActivity() , LockSettingsActivity.class));
                        break;
                    case FeatureFlags.KEY_SELECT_CATEGORIES:
                        startActivity(new Intent(getActivity() , CategoryChooser.class));
                    default:
                        return false;
                }
                return true;
            }
            return false;
        }

        private boolean checkStoragePermission() {
            return checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        private boolean checkPermission(String permission) {
            boolean granted = ContextCompat.checkSelfPermission(
                    getActivity(),
                    permission) == PackageManager.PERMISSION_GRANTED;
            if (granted) return true;
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{permission},
                    0);
            return false;
        }

        private int getContent() {
            return getArguments().getInt(CONTENT_RES_ID);
        }

        @Override
        public void onResume() {
            super.onResume();
            getActivity().setTitle(getArguments().getString(TITLE));
        }

        public static SubSettingsFragment newInstance(SubPreference preference) {
            SubSettingsFragment fragment = new SubSettingsFragment();
            Bundle b = new Bundle(2);
            b.putString(TITLE, (String) preference.getTitle());
            b.putInt(CONTENT_RES_ID, preference.getContent());
            fragment.setArguments(b);
            return fragment;
        }

    }
}
