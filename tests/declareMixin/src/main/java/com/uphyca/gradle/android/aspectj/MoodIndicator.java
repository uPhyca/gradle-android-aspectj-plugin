package com.uphyca.gradle.android.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareMixin;

/**
 * <a href="https://eclipse.org/aspectj/doc/released/adk15notebook/ataspectj-itds.html">Inter-type Declarations</a>
 */
@Aspect
public class MoodIndicator {

    // this interface can be outside of the aspect
    public interface Moody {
        Mood getMood();
    }

    // this implementation can be outside of the aspect
    public static class MoodyImpl implements Moody {
        private Mood mood = Mood.HAPPY;

        public Mood getMood() {
            return mood;
        }
    }


    // The DeclareMixin annotation is attached to a factory method that can return instances of the delegate
    // which offers an implementation of the mixin interface.  The interface that is mixed in is the
    // return type of the method.
    @DeclareMixin("com.uphyca.gradle.android.aspectj..*")
    public static Moody createMoodyImplementation() {
        return new MoodyImpl();
    }

    @Before("execution(* *.*(..)) && !within(MoodIndicator) && this(m)")
    public void feelingMoody(Moody m) {
        System.out.println("I'm feeling " + m.getMood());
    }
}