package com.killer.annoy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public String regex;
    public SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    public void setup() {
        pref = getSharedPreferences("pref", MODE_PRIVATE);

        ((TextView) findViewById(R.id.historyTextView)).setText(pref.getString("log", "No log"));
        regex = pref.getString("regex", ".*youtube.*");
        ((EditText) findViewById(R.id.regexEditText)).setText(regex);
        ((TextView) findViewById(R.id.regexView)).setText(regex);

        findViewById(R.id.setRegexButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regex = ((EditText) findViewById(R.id.regexEditText)).getText().toString();
                        ((TextView) findViewById(R.id.regexView)).setText(regex);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("regex", regex);
                        editor.commit();
                    }
                }
        );
    }
}
