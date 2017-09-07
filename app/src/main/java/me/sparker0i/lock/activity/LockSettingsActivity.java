package me.sparker0i.lock.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import me.sparker0i.lawnchair.R;
import me.sparker0i.lawnchair.databinding.ActivityLockSettingsBinding;
import me.sparker0i.lock.preferences.LockType;
import me.sparker0i.lock.preferences.Preferences;
import me.sparker0i.lock.service.ScreenService;
import me.sparker0i.question.Utilities;
import me.sparker0i.question.activity.CategoryChooser;

public class LockSettingsActivity extends AppCompatActivity{

    Switch aSwitch;
    LinearLayout lockLayout , loadLayout , selectLayout;
    ActivityLockSettingsBinding binding;
    Preferences prefs;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_lock_settings);
        aSwitch = binding.aSwitch;
        prefs = new Preferences(this);
        loadLayout = binding.refreshLayout;
        selectLayout = binding.selectLayout;
        aSwitch.setChecked(prefs.isLockEnabled());
        loadLayout.setEnabled(aSwitch.isChecked());
        selectLayout.setEnabled(aSwitch.isChecked());
        lockLayout = binding.enableLockLayout;
        context = this;
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!compoundButton.isChecked()) {
                    Log.i("Yeah" , "Is Not Selected");
                    invertLock(-1);
                }
                else {
                    if (Utilities.isLockEnabled(context)) {
                        Log.i("Yeah" , "Is Locked");
                        Utilities.showLockEnabled(context);
                    }
                    else {
                        Log.i("Yeah" , "Is Not Locked");
                        invertLock(1);
                    }
                }
            }
        });
        lockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aSwitch.isChecked()) {
                    invertLock(-1);
                }
                else {
                    if (isDeviceSecured()) {
                        Utilities.showLockEnabled(context);
                    }
                    else {
                        invertLock(1);
                    }
                }
            }
        });
        loadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.loadQuestions(context);
            }
        });
        selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context , CategoryChooser.class));
            }
        });
    }

    private boolean isDeviceSecured()
    {
        return LockType.isLockEnabled(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Yeah" , "Called");
        if (requestCode == 101) {
            if (Utilities.isLockEnabled(this)) {
                invertLock(-1);
            }
            else {
                invertLock(1);
            }
        }
    }

    private void invertLock(int code) {
        if (code == -1) {
            aSwitch.setChecked(false);
            prefs.setLockEnabled(false);
            stopService(new Intent(this , ScreenService.class));
            loadLayout.setEnabled(false);
            selectLayout.setEnabled(false);
        }
        else if (code == 1) {
            aSwitch.setChecked(true);
            prefs.setLockEnabled(true);
            startService(new Intent(this , ScreenService.class));
            loadLayout.setEnabled(true);
            selectLayout.setEnabled(true);
        }
    }
}
