package com.uphyca.gradle.android.aspectj;

import javax.inject.Inject;

public class HelloWorld implements Greeter {

    @Inject
    public HelloWorld() {
    }

    @Override
    public String greet() {
        return "Hello, world!";
    }
}
