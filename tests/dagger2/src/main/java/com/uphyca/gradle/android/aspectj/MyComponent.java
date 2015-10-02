package com.uphyca.gradle.android.aspectj;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MyModule.class)
public interface MyComponent {

    void inject(MyActivity myActivity);
}
