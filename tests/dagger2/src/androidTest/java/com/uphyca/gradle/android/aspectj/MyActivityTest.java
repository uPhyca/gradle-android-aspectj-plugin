package com.uphyca.gradle.android.aspectj;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.ContextThemeWrapper;

public class MyActivityTest extends ActivityUnitTestCase<MyActivity> {

    Intent mStartIntent = new Intent();

    public MyActivityTest() {
        super(MyActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();


        ContextThemeWrapper context = new ContextThemeWrapper(getInstrumentation().getTargetContext(), R.style.AppTheme);
        setActivityContext(context);

        MyApplication myApplication = new MyApplication();
        myApplication.onCreate();
        setApplication(myApplication);

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
