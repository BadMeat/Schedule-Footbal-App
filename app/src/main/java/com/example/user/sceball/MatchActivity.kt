package com.example.user.sceball

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import io.armcha.playtablayout.core.TouchableTabLayout
import kotlinx.android.synthetic.main.activity_with_icon.*

/**
 * Created by Bencoleng on 11/11/2018.
 */
class MatchActivity : AppCompatActivity(), TouchableTabLayout.OnTabSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_icon)

        supportActionBar?.title = "Match"

        playTabLayout.colors = intArrayOf(
            R.color.f,
            R.color.s,
            R.color.t
        )

        val tabLayout = playTabLayout.tabLayout
        viewPager.adapter = TabMatchAdapter(supportFragmentManager, playTabLayout.colors.size)

        with(tabLayout) {
            setupWithViewPager(viewPager)
            setSelectedTabIndicatorHeight(7)
            setSelectedTabIndicatorColor(Color.WHITE)
            tabMode = TabLayout.MODE_FIXED
            tabGravity = TabLayout.GRAVITY_FILL
            setTabTextColors(ContextCompat.getColor(this@MatchActivity, R.color.unselected_tab_color), Color.WHITE)
            addOnTabSelectedListener(this@MatchActivity)
        }

        fun icon(index: Int, drawableId: Int) {
            tabLayout.getTabAt(index)?.setIcon(drawableId)
        }
        icon(0, R.drawable.ic_queue_play_next_black_24dp)
        icon(1, R.drawable.ic_timer_black_24dp)
        icon(2, R.drawable.ic_search_black_24dp)

        fun Drawable.tint(color: Int) {
            setColorFilter(ContextCompat.getColor(this@MatchActivity, color), PorterDuff.Mode.SRC_IN)
        }
        (0 until (viewPager.adapter as TabMatchAdapter).count)
            .map { tabLayout.getTabAt(it) }
            .map { it?.getIcon() }
            .doWhen({ it?.tint(R.color.selected_tab_color) }, { it == viewPager.currentItem })
            .doWhen({ it?.tint(R.color.unselected_tab_color) }, { it != viewPager.currentItem })
    }

    private inline fun <T> Iterable<T>.doWhen(body: (T) -> Unit, predicate: (Int) -> Boolean): Iterable<T> {
        var index = 0
        return apply {
            for (element in this) {
                if (predicate(index++)) {
                    body(element)
                }
            }
        }
    }

    override fun onTabSelected(tab: TouchableTabLayout.Tab) {
        tab.getIcon()?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
    }

    override fun onTabUnselected(tab: TouchableTabLayout.Tab) {
        tab.getIcon()
            ?.setColorFilter(ContextCompat.getColor(this, R.color.unselected_tab_color), PorterDuff.Mode.SRC_IN);
    }

    override fun onTabReselected(tab: TouchableTabLayout.Tab) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }
}