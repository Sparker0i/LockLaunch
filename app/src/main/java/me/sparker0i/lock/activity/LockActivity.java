package me.sparker0i.lock.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.R;
import me.sparker0i.lawnchair.databinding.ActivityLockBinding;
import me.sparker0i.lock.DBHelper;
import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Category;
import me.sparker0i.question.model.Question;

@SuppressWarnings("deprecation")
public class LockActivity extends Activity implements OnClickListener{

    ActivityLockBinding binding;
    RadioButton optA , optB , optC , optD;
    TextView quesText;
    Question question;
    Context context;
    static DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this , R.layout.activity_lock);
        optA = binding.radio1;
        optB = binding.radio2;
        optC = binding.radio3;
        optD = binding.radio4;
        quesText = binding.question;
        db = new DatabaseHandler(this);
        try {
            question = new BackgroundThread().get();
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        Launcher.setLocked(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        findViewById(R.id.btn_on).setOnClickListener(this);

        quesText.setText(question.getQN());
        optA.setText(question.getA());
        optB.setText(question.getB());
        optC.setText(question.getC());
        optD.setText(question.getD());
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

    static class BackgroundThread extends AsyncTask<Void , Void , Question> {

        @Override
        protected Question doInBackground(Void... params) {
            return db.getRandomQuestion();
        }
    }
}