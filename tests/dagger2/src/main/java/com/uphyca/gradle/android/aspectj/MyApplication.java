package com.uphyca.gradle.android.aspectj;

import android.app.Application;

public class MyApplication extends Application {

    private MyComponent myComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.myComponent = DaggerMyComponent.create();
    }

    public MyComponent getComponent() {
        return myComponent;
    }
}
