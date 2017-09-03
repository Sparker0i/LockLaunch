package me.sparker0i.question;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.lang.reflect.Method;

import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Question;
import me.sparker0i.question.model.QuestionHelper;

public class Utilities {
    private static final String JSON_URL = "http://blog.sparker0i.me/MathQuizQuestions/Math/Vedic.json";

    public static void loadQuestions(final Context context) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Loading")
                .content("Please Wait")
                .cancelable(false)
                .progress(true , 0)
                .build();
        dialog.show();
        StringRequest stringRequest = new StringRequest(JSON_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.hide();
                        Toast.makeText(context,"Pro",Toast.LENGTH_LONG).show();
                        Log.i("J" , response);
                        showJSON(response , context);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private static void showJSON(String json , Context context){
        /*Do whatever you want with the String json*/
        QuestionHelper questionz = new Gson().fromJson(json , QuestionHelper.class);
        Log.i("Json" , new Gson().toJson(questionz));
        DatabaseHandler db = new DatabaseHandler(context);
        for (int i = 0; i < questionz.list.size(); ++i) {
            QuestionHelper.QuestionList qn = questionz.list.get(i);
            Question question = new Question(qn.getQN() , qn.getA() , qn.getB() , qn.getC() , qn.getD() , questionz.getCAT() , qn.getANS());
            db.addQuestion(question);
        }
    }

    public static boolean isLockEnabled(Context context) {
        KeyguardManager manager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && manager.isDeviceSecure()) || isDeviceSecured(context));
    }

    private static boolean isDeviceSecured(Context context)
    {
        String LOCKSCREEN_UTILS = "com.android.internal.widget.LockPatternUtils";
        try
        {
            Class<?> lockUtilsClass = Class.forName(LOCKSCREEN_UTILS);
            // "this" is a Context, in my case an Activity
            Object lockUtils = lockUtilsClass.getConstructor(Context.class).newInstance(context);
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

    public static void showLockEnabled(final Context context) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Lock Screen Enabled")
                .content("Please Disable Your Android Lock Screen before you continue")
                .cancelable(false)
                .progress(true , 0)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ((Activity) context).startActivityForResult(new Intent(Settings.ACTION_SECURITY_SETTINGS) , 101);
                    }
                })
                .build();
        dialog.show();
    }
}
