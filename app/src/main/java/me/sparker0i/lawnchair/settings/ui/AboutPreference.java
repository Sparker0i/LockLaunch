package me.sparker0i.lawnchair.settings.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.Utilities;
import me.sparker0i.lawnchair.compat.LauncherAppsCompat;
import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.Utilities;
import me.sparker0i.lawnchair.compat.LauncherAppsCompat;
import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.Utilities;
import me.sparker0i.lawnchair.compat.LauncherAppsCompat;

public class AboutPreference extends Preference implements View.OnLongClickListener {

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/deletescape-media/lawnchair"));
        getContext().startActivity(i);
    }

    @Override
    public boolean onLongClick(View view) {
        ComponentName componentName = new ComponentName(getContext(), Launcher.class);
        LauncherAppsCompat.getInstance(getContext()).showAppDetailsForProfile(componentName, Utilities.myUserHandle());
        return true;
    }
}
