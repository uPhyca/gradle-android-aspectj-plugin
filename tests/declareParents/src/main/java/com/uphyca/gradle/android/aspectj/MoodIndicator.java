package com.uphyca.gradle.android.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;

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

    // the field type must be the introduced interface. It can't be a class.
    @DeclareParents(value = "com.uphyca.gradle.android.aspectj..*", defaultImpl = MoodyImpl.class)
    private Moody implementedInterface;

    @Before("execution(* *.*(..)) && !within(MoodIndicator) && this(m)")
    public void feelingMoody(Moody m) {
        System.out.println("I'm feeling " + m.getMood());
    }
}