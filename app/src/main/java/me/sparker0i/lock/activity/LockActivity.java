package me.sparker0i.lock.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import me.sparker0i.lawnchair.Launcher;
import me.sparker0i.lawnchair.R;

@SuppressWarnings("deprecation")
public class LockActivity extends AppCompatActivity {

    Context context;
    Handler handler;
    Permissions per;
    CameraFragment cameraFragment;

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
        findViewById(R.id.aSwitch).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFragment.switchCameraTypeFrontBack();
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
            cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, cameraFragment, "hh")
                    .commit();

        }
            //requestPermissions(new String[]{Manifest.permission.CAMERA},20);
        findViewById(R.id.capture).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultListener() {
                    @Override
                    public void onVideoRecorded(String filePath) {

                    }

                    @Override
                    public void onPhotoTaken(byte[] bytes, String filePath) {
                        Log.i("location", filePath);
                        Kairos kairos = new Kairos();
                        kairos.setAuthentication(context , "519b32f0" , "00ed607825eeb76283294e0c2ae1b3da");
                        KairosListener listener = new KairosListener() {
                            @Override
                            public void onSuccess(String s) {
                                Log.i("Kairos Response" , s);
                            }

                            @Override
                            public void onFail(String s) {
                                Log.i("Kairos Fail" , s);
                            }
                        };
                        /*
                        Bitmap image = BitmapFactory.decodeFile(filepath);
                        String subjectId = "Sparker0i";
                        String galleryId = "friends";
                        String selector = "FULL";
                        String multipleFaces = "false";
                        String minHeadScale = "0.25";
                        myKairos.enroll(image,
                                        subjectId,
                                        galleryId,
                                        selector,
                                        multipleFaces,
                                        minHeadScale,
                                        listener);
                        */
                        Bitmap image = BitmapFactory.decodeFile(filePath);
                        String galleryId = "friends";
                        String selector = "FULL";
                        String threshold = "0.75";
                        String minHeadScale = "0.25";
                        String maxNumResults = "25";
                        try {
                            kairos.recognize(image,
                                    galleryId,
                                    selector,
                                    threshold,
                                    minHeadScale,
                                    maxNumResults,
                                    listener);
                        }
                        catch (JSONException | UnsupportedEncodingException ex ) {
                            ex.printStackTrace();
                        }
                    }
                } , getFilesDir().getPath() , "face");
            }
        });
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