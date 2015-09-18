package com.uphyca.gradle.android.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class HelloAspect {

    @Around("execution(String com.uphyca.gradle.android.aspectj.MyActivity.sayHello())")
    public String around(ProceedingJoinPoint pj) throws Throwable {
        return "Hello, @Aspect";
    }
}
