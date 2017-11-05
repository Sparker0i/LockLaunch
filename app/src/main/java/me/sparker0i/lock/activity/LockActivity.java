package me.sparker0i.lock.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;

import java.util.List;

import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.R;
import me.sparker0i.lock.DBHelper;
import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Question;

@SuppressWarnings("deprecation")
public class LockActivity extends AppCompatActivity {

    Context context;
    Handler handler;
    Permissions per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        handler = new Handler();
        setContentView(R.layout.activity_lock);
        Launcher.setLocked(true);
        findViewById(R.id.unlock).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Launcher.setLocked(false);
                finish();
            }
        });
        per = new Permissions(this);
        if (ContextCompat.checkSelfPermission(this , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                Log.i("In" , "Show Rationale");

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                Log.i("In" , "Request Permissions");

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        20);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else {
            CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, cameraFragment, "hh")
                    .commit();
        }
            //requestPermissions(new String[]{Manifest.permission.CAMERA},20);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 20:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, cameraFragment, "hh")
                            .commit();
                }
                else{
                    per.permissionDenied();
                }

        }
    }

    private void unlock() {
        Launcher.setLocked(false);
        finish();
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
    }

    @Override
    public void onBackPressed() {

    }
}