package me.sparker0i.lock.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import java.util.List;

import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.R;
import me.sparker0i.lock.DBHelper;

@SuppressWarnings("deprecation")
public class LockActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        Launcher.setLocked(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        findViewById(R.id.btn_on).setOnClickListener(this);
    }

    private void exitAppAnimate() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (int i=0; i<recentTasks.size(); i++) {
            if (i == 1 && recentTasks.get(i).baseActivity.toShortString().contains(getPackageName())) {
                // home button pressed
                Log.i("Pressed" , "Home");
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {

        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();

        switch (v.getId()) {
            case R.id.btn_on:
                Launcher.setLocked(false);
                finish();
                break;
        }

        db.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null)
            activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        exitAppAnimate();
    }

    @Override
    public void onBackPressed() {

    }
}