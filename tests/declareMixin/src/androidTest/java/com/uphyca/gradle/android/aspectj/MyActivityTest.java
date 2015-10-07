package com.uphyca.gradle.android.aspectj;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyActivityTest {

    @Rule
    public ActivityTestRule<MyActivity> activityTestRule = new ActivityTestRule<>(MyActivity.class);

    private MyActivity underTest;

    @Before
    public void setUp() throws Exception {
        underTest = activityTestRule.getActivity();
    }

    @Test
    public void feelingHappy() throws Exception {
        assertThat(((MoodIndicator.Moody) underTest).getMood()).isEqualTo(Mood.HAPPY);
    }
}
