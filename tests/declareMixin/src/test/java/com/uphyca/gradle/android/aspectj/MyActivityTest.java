package com.uphyca.gradle.android.aspectj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MyActivityTest {

    @Test
    public void feelingHappy() throws Exception {
        MyActivity underTest = Robolectric.setupActivity(MyActivity.class);

        assertThat(((MoodIndicator.Moody) underTest).getMood()).isEqualTo(Mood.HAPPY);
    }
}
