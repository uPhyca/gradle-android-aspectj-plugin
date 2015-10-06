package com.uphyca.gradle.android.aspectj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;


public class MyActivity extends AppCompatActivity {

    @Inject
    Greeter greeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_my);
        TextView.class.cast(findViewById(R.id.greeting)).setText(greeter.greet());
    }
}
