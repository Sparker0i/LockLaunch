package me.sparker0i.lock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Category;

public class Preferences {
    private static SharedPreferences prefs;

    public Preferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setCategoryPreferences(List<Category> cat) {
        for (int i = 0; i < cat.size(); ++i) {
            prefs.edit().putBoolean("selected_" + cat.get(i).getName().toLowerCase() , cat.get(i).isSelected()).apply();
        }
    }

    public void setLockEnabled(boolean value) {
        prefs.edit().putBoolean("lock" , value).apply();
    }

    public boolean isLockEnabled() {
        return prefs.getBoolean("lock" , false);
    }
}