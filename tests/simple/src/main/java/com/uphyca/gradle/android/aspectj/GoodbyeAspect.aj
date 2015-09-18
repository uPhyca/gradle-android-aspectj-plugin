package com.uphyca.gradle.android.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


public aspect GoodbyeAspect {

    pointcut sayGoodbye() : execution(String com.uphyca.gradle.android.aspectj.MyActivity.sayGoodbye());

    String around() : sayGoodbye() {
        return "Goodbye, Aspect";
    }
}
