package com.uphyca.gradle.android.aspectj;

import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.android.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MyActivityTest {

    @Test
    public void greet() throws Exception {
        MyActivity underTest = Robolectric.setupActivity(MyActivity.class);

        TextView greetingView = (TextView) underTest.findViewById(R.id.greeting);
        assertThat(greetingView).hasText("Hello, aspect!");
    }
}
