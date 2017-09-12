package me.sparker0i.lock.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.R;
import me.sparker0i.lawnchair.databinding.ActivityLockBinding;
import me.sparker0i.lock.DBHelper;
import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Question;

@SuppressWarnings("deprecation")
public class LockActivity extends Activity implements OnClickListener{

    ActivityLockBinding binding;
    RadioButton optA , optB , optC , optD;
    CardView incorrect;
    TextView quesText;
    Question question;
    Context context;
    static DatabaseHandler db;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        handler = new Handler();
        binding = DataBindingUtil.setContentView(this , R.layout.activity_lock);
        optA = binding.radio1;
        optB = binding.radio2;
        optC = binding.radio3;
        optD = binding.radio4;
        incorrect = binding.incorrect;
        quesText = binding.question;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        findViewById(R.id.btn_on).setOnClickListener(this);
        db = new DatabaseHandler(this);
        try {
            Log.i("Started" , "Background Thread");
            question = new BackgroundThread().doInBackground();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                quesText.setText(question.getQN());
                optA.setText(question.getA());
                optB.setText(question.getB());
                optC.setText(question.getC());
                optD.setText(question.getD());
            }
        });
        Launcher.setLocked(true);
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
                checkAnswer(binding.ansGroup.getCheckedRadioButtonId());
                break;
        }

        db.close();
    }

    public void checkAnswer(int selected) {
        switch(question.getANS()) {
            case "a" :
                if (selected == optA.getId())
                    unlock();
                else
                    incorrect.setVisibility(View.VISIBLE);
                break;
            case "b" :
                if (selected == optB.getId())
                    unlock();
                else
                    incorrect.setVisibility(View.VISIBLE);
                break;
            case "c" :
                if (selected == optC.getId())
                    unlock();
                else
                    incorrect.setVisibility(View.VISIBLE);
                break;
            case "d" :
                if (selected == optD.getId())
                    unlock();
                else
                    incorrect.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void unlock() {

        Launcher.setLocked(false);
        finish();
        Log.i("Answer" , question.getANS());
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

    static class BackgroundThread extends AsyncTask<Void , Void , Question> {

        @Override
        protected Question doInBackground(Void... params) {
            return db.getRandomQuestion();
        }
    }
}