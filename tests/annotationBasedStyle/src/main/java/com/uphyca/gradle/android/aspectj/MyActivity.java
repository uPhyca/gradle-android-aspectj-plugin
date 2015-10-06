package com.uphyca.gradle.android.aspectj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class MyActivity extends AppCompatActivity {

    String greet() {
        return "Hello, world!";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        TextView.class.cast(findViewById(R.id.greeting)).setText(greet());
    }
}
