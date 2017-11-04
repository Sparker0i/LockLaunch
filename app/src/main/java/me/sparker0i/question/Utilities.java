package me.sparker0i.question;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import me.sparker0i.question.database.DatabaseHandler;
import me.sparker0i.question.model.Category;
import me.sparker0i.question.model.Question;
import me.sparker0i.question.model.QuestionHelper;
import me.sparker0i.question.model.UrlModel;

public class Utilities {
    private static final String JSON_URL = "http://blog.sparker0i.me/MathQuizQuestions/Urls.json";
    private MaterialDialog dialog;
    private Context context;

    public Utilities(Context context) {
        this.context = context;
    }

    public void loadQuestions() {
        dialog = new MaterialDialog.Builder(context)
                .title("Loading")
                .content("Please Wait")
                .cancelable(false)
                .progress(true , 0)
                .build();
        dialog.show();
        processQuestions("{\n" +
                "  \"cat\": \"Vedic\",\n" +
                "  \"list\": [\n" +
                "    {\n" +
                "      \"qn\": \"What is sin(π) ?\",\n" +
                "      \"a\": \"1\",\n" +
                "      \"b\": \"0\",\n" +
                "      \"c\": \"1.414\",\n" +
                "      \"d\": \"0.678\",\n" +
                "      \"ans\": \"b\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"qn\": \"What is sin(π/6) ?\",\n" +
                "      \"a\": \"1\",\n" +
                "      \"b\": \"0\",\n" +
                "      \"c\": \"0.5\",\n" +
                "      \"d\": \"0.678\",\n" +
                "      \"ans\": \"c\"\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }

    private void processQuestions(String json) {
        QuestionHelper questionz = new Gson().fromJson(json , QuestionHelper.class);
        Log.i("Json" , new Gson().toJson(questionz));
        DatabaseHandler db = new DatabaseHandler(context);
        for (int i = 0; i < questionz.list.size(); ++i) {
            QuestionHelper.QuestionList qn = questionz.list.get(i);
            Question question = new Question(qn.getQN() , qn.getA() , qn.getB() , qn.getC() , qn.getD() , questionz.getCAT() , qn.getANS());
            db.addQuestion(question);
            db.addCategory(new Category(questionz.getCAT() , false));
        }
        dialog.hide();
    }

    public static boolean isLockEnabled(Context context) {
        /*KeyguardManager manager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return manager.isDeviceSecure();
        else return LockType.isLockEnabled(context);*/
        return false;
    }
}
