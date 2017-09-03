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
    private Context context;

    public Preferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setCategoryPreferences(List<Category> cat) {
        for (int i = 0; i < cat.size(); ++i) {
            prefs.edit().putBoolean("selected_" + cat.get(i).getName().toLowerCase() , cat.get(i).isSelected()).apply();
        }
    }

    public HashMap<String , Boolean> getCategoryPreferences() {
        Task task = new Task();
        try {
            return task.execute(context).get();
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static class Task extends AsyncTask<Context , Void , HashMap<String , Boolean>> {
        @Override
        protected HashMap<String, Boolean> doInBackground(Context... params) {
            DatabaseHandler db = new DatabaseHandler(params[0]);
            List<String> cat = db.getCategories();
            HashMap<String , Boolean> map = new HashMap<>();
            for (int i = 0; i < cat.size(); ++i) {
                map.put(cat.get(i) , prefs.getBoolean("selected_" + cat.get(i).toLowerCase() , false));
            }
            return map;
        }
    }
}