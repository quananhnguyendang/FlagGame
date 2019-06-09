package com.son.myapplication.Activity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;

import com.son.myapplication.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class activity_levelTest {

private activity_level ac=null;
    @Before
    public void setUp() throws Exception {
        activity_level ac=new activity_level();
    }

    @Test
    public void testLaunchOfSecondActivityOnButtonClick()
    {
        assertNotNull(ac.findViewById(R.id.txtEasy));
        Espresso.onView(withId(R.id.txtEasy)).perform(ViewActions.click());
    }
    @After
    public void tearDown() throws Exception {
    }
}