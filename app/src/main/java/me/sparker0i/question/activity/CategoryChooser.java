package me.sparker0i.question.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.R;
import me.sparker0i.lock.preferences.Preferences;
import me.sparker0i.lock.service.ScreenService;
import me.sparker0i.lock.widget.CategoryAdapter;
import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Category;

public class CategoryChooser extends Activity {
    static DatabaseHandler db;
    Handler handler;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_chooser);

        button = findViewById(R.id.button);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHandler(this);
        handler = new Handler();
        List<Category> categories;

        BackgroundThread bt = new BackgroundThread();
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Loading")
                .content("Please Wait")
                .cancelable(false)
                .progress(true , 0)
                .build();
        dialog.show();

        try {
            categories = bt.execute().get();
            dialog.hide();

            mAdapter = new CategoryAdapter(categories);
            mRecyclerView.setAdapter(mAdapter);
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }

        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Category> catList = ((CategoryAdapter) mAdapter).getCategoriesList();
                new Preferences(context).setCategoryPreferences(catList);
                finish();
            }
        });
    }

    private static class BackgroundThread extends AsyncTask<Void , Void , List<Category>> {

        @Override
        protected List<Category> doInBackground(Void... params) {
            return db.getCategories();
        }
    }
}
