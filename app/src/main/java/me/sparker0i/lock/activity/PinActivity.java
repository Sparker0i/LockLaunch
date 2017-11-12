package me.sparker0i.lock.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.sparker0i.lawnchair.R;

public class PinActivity extends AppCompatActivity {
EditText text ;
String password = "1234";
Button okay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        text = findViewById(R.id.password);
        password = text.getText().toString();
        okay = findViewById(R.id.pass);
        final Context context = this;
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"this is you password: "+ text.getText().toString(),Toast.LENGTH_LONG).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("password", text.getText().toString());
                editor.apply();
            }
        });

    }
}
