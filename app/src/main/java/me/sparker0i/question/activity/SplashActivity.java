package me.sparker0i.question.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.lawnchair.R;
import me.sparker0i.question.model.Question;
import me.sparker0i.question.model.QuestionHelper;

public class SplashActivity extends Activity {

    public static final String JSON_URL = "http://blog.sparker0i.me/MathQuizQuestions/Math/Vedic.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
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
                        Toast.makeText(SplashActivity.this,"Pro",Toast.LENGTH_LONG).show();
                        Log.i("J" , response);
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        /*Do whatever you want with the String json*/
        QuestionHelper questionz = new Gson().fromJson(json , QuestionHelper.class);
        Log.i("Json" , new Gson().toJson(questionz));
        DatabaseHandler db = new DatabaseHandler(this);
        for (int i = 0; i < questionz.list.size(); ++i) {
            QuestionHelper.QuestionList qn = questionz.list.get(i);
            Question question = new Question(qn.getQN() , qn.getA() , qn.getB() , qn.getC() , qn.getD() , questionz.getCAT() , qn.getANS());
            db.addQuestion(question);
        }
        startActivity(new Intent(this , CategoryChooser.class));
    }
}
