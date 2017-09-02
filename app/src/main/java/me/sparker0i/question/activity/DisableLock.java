package me.sparker0i.question.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Method;

import me.sparker0i.lawnchair.R;
import me.sparker0i.lock.activity.LockActivity;

public class DisableLock extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_lock);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivityForResult(intent , 0);
            }
        });
    }

    private boolean isDeviceSecured()
    {
        String LOCKSCREEN_UTILS = "com.android.internal.widget.LockPatternUtils";
        try
        {
            Class<?> lockUtilsClass = Class.forName(LOCKSCREEN_UTILS);
            // "this" is a Context, in my case an Activity
            Object lockUtils = lockUtilsClass.getConstructor(Context.class).newInstance(this);
            Method method = lockUtilsClass.getMethod("getActivePasswordQuality");

            int lockProtectionLevel = (Integer)method.invoke(lockUtils); // Thank you esme_louise for the cast hint
            if(lockProtectionLevel >= DevicePolicyManager.PASSWORD_QUALITY_NUMERIC)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            Log.e("reflectInternalUtils", "ex:"+e);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if(!((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && manager.isDeviceSecure()) || isDeviceSecured())) {
                startActivity(new Intent(this , LockActivity.class));
            }
        }
    }
}
