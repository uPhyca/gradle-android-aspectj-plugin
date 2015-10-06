package com.uphyca.gradle.android.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MyAspect {

    @Around("execution(String com.uphyca.gradle.android.aspectj.MyActivity.greet())")
    public String around(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        String result = (String) thisJoinPoint.proceed();
        return result.replace("world", "aspect");
    }
}
