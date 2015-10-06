package com.uphyca.gradle.android.aspectj;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyModule {

    @Provides
    Greeter provideGreeter(HelloWorld helloWorld) {
        return helloWorld;
    }
}
