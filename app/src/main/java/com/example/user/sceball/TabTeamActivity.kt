package com.example.user.sceball

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.user.sceball.fragment.*

class TabTeamActivity(fragmentManager: FragmentManager, private val count: Int) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TeamFragment()
            else -> SearchTeamFragment()
        }
    }

    override fun getCount() = count

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Team"
        else -> "Search"
    }
}