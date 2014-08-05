package com.uphyca.gradle.android.aspectj;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import junit.framework.TestCase;

public class MyActivityTest extends ActivityUnitTestCase<MyActivity> {

    Intent mStartIntent = new Intent();

    public MyActivityTest() {
        super(MyActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();

    }

    public void testSayHello() throws Exception {
        MyActivity underTest = startActivity(mStartIntent, null, null);
        assertEquals("Hello, @Aspect", underTest.sayHello());
    }

    public void testSayGoodbye() throws Exception {
        MyActivity underTest = startActivity(mStartIntent, null, null);
        assertEquals("Goodbye, Aspect", underTest.sayGoodbye());
    }
}
