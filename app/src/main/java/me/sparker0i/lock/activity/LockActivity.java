package me.sparker0i.lock.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import me.sparker0i.lawnchair.R;
import me.sparker0i.lock.DBHelper;

@SuppressWarnings("deprecation")
public class LockActivity extends Activity implements OnClickListener{

    KeyguardManager km = null;
    KeyguardManager.KeyguardLock keyLock = null;
    private Handler windowCloseHandler;
    private Runnable windowCloserRunnable;
    int task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        windowCloseHandler = new Handler();
        task = getTaskId();

        if(km == null)
            km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        if(keyLock == null)
            keyLock = km.newKeyguardLock(Context.KEYGUARD_SERVICE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        findViewById(R.id.btn_on).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();

        switch (v.getId()) {
            case R.id.btn_on:
                finish();
                break;
        }

        db.close();
    }

    void setActivityOrder() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(!pm.isScreenOn()) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);

            if (activityManager != null)
                activityManager.moveTaskToFront(getTaskId(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            windowCloseHandler.post(windowCloserRunnable);
        }
    }

    private void toggleRecents() {
        Intent closeRecents = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
        closeRecents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        ComponentName recents = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
        closeRecents.setComponent(recents);
        this.startActivity(closeRecents);
    }

    @Override
    protected void onUserLeaveHint() {
        finish();
        super.onUserLeaveHint();
    }*/

    @Override
    public void onBackPressed() {

    }
}