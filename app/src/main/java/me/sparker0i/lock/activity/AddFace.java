package me.sparker0i.lock.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import me.sparker0i.lawnchair.R;
import me.sparker0i.lock.model.AddModel;

public class AddFace extends AppCompatActivity {
    CameraFragment cameraFragment;
    Permissions per;
    Context context;
    MaterialDialog dialog;
    AddModel posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_face);
        context = this;

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
        findViewById(R.id.aSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFragment.switchCameraTypeFrontBack();
            }
        });
        findViewById(R.id.captureandsave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultListener() {
                    @Override
                    public void onVideoRecorded(String filePath) {

                    }

                    @Override
                    public void onPhotoTaken(byte[] bytes, String filePath) {
                        Kairos mykairos = new Kairos();
                        mykairos.setAuthentication(context , "91e3fef9" , "25d5338d52c40ecc14324d2b7aa3fdf6");
                        KairosListener listener = new KairosListener() {
                            @Override
                            public void onSuccess(String s) {
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                                Gson gson = gsonBuilder.create();
                                posts = gson.fromJson(s, AddModel.class);
                                System.out.println(gson.toJson(posts));
                                dialog.dismiss();
                                if (posts.Errors == null) {
                                    Log.i("True" , "No Error");
                                    finish();
                                }
                                else {
                                    new MaterialDialog.Builder(context)
                                            .title("Error")
                                            .content(posts.Errors.get(0).Message)
                                            .positiveText("OK")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            }

                            @Override
                            public void onFail(String s) {
                                Log.i("Kairos Fail" , s);
                            }
                        };
                        EditText ed = findViewById(R.id.name);
                        Bitmap image = BitmapFactory.decodeFile(filePath);
                        String subjectId = ed.getText().toString() ;
                        String galleryId = "images";
                        String selector = "FULL";
                        String multipleFaces = "false";
                        String minHeadScale = "0.25";
                        try {
                            dialog = new MaterialDialog.Builder(context)
                                    .title("Please Wait")
                                    .content("Loading")
                                    .cancelable(false)
                                    .progress(true , 0)
                                    .build();
                            dialog.show();
                            mykairos.enroll(image,
                                    subjectId,
                                    galleryId,
                                    selector,
                                    multipleFaces,
                                    minHeadScale,
                                    listener);
                        }
                        catch (JSONException | UnsupportedEncodingException ex){
                            ex.printStackTrace();
                        }
                    }

                }, getFilesDir().getPath() , "face");
            }
        });
    }
}
