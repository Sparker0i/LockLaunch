package me.sparker0i.question;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import me.sparker0i.lock.preferences.LockType;
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
        return ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && manager.isDeviceSecure()) || LockType.isLockEnabled(context));
    }
}