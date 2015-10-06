package com.uphyca.gradle.android.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


public aspect MyAspect {

    pointcut callGreet() : call(String com.uphyca.gradle.android.aspectj.MyActivity.greet());

    String around() : callGreet() {
        String result = proceed();
        return result.replace("world", "aspect");
    }
}
