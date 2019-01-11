package com.example.user.sceball

import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Bencoleng on 27/11/2018.
 */
@RunWith(AndroidJUnit4::class)
class FavoriteActivityTest {

    @JvmField
    @Rule
    var activityRule = ActivityTestRule(DetailActivity::class.java, true, false)

    private val i = Intent()

    @Before
    fun setup() {
        i.putExtra("idEvent", "441613")
        i.putExtra("idHome", 133616)
        i.putExtra("idAway", 133610)
    }


    @Test
    fun testAddFavorite() {
        activityRule.launchActivity(i)
        Espresso.onView(ViewMatchers.withId(R.id.test_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(3000)
        activityRule.activity.addToFavorite()
    }

    @Test
    fun testRemoveFavorite() {
        activityRule.launchActivity(i)
        Espresso.onView(ViewMatchers.withId(R.id.test_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(3000)
        activityRule.activity.removeFromFavorite()
    }
}