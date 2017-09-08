package me.sparker0i.lock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Category;

public class Preferences {
    private static SharedPreferences prefs;
    private Context context;

    public Preferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setCategoryPreferences(List<Category> cat) {
        DatabaseHandler db = new DatabaseHandler(context);
        for (int i = 0; i < cat.size(); ++i) {
            db.updateCategory(cat.get(i).getName() , cat.get(i).getSelected());
        }
    }

    public void setLockEnabled(boolean value) {
        prefs.edit().putBoolean("lock" , value).apply();
    }

    public boolean isLockEnabled() {
        return prefs.getBoolean("lock" , false);
    }
}