package com.dicoding.githubsocial.ui.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.githubsocial.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomSplashScreenTest {

    @Before
    fun setup() {
        ActivityScenario.launch(CustomSplashScreen::class.java)
    }

    @Test
    fun cekGambar() {
        onView(withId(R.id.img_splash_logo)).check(matches(isDisplayed()))
    }
}