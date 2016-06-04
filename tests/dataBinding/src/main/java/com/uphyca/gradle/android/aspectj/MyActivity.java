package com.uphyca.gradle.android.aspectj;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.uphyca.gradle.android.aspectj.databinding.ActivityMyBinding;


public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_my);
        Greeter greeter = new Greeter();
        greeter.setMessage("Hello, world!");
        binding.setGreeter(greeter);
    }
}
