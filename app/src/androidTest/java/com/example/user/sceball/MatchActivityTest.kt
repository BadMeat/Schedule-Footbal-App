package com.example.user.sceball

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.example.user.sceball.R.id.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Bencoleng on 27/11/2018.
 */
@RunWith(AndroidJUnit4::class)
class MatchActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MatchActivity::class.java)

    @Test
    fun testRecyclerViewBehaviourNext() {
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(nextfrag))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(nextfrag))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        Espresso.onView(ViewMatchers.withId(nextfrag)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5, ViewActions.click())
        )
    }
}