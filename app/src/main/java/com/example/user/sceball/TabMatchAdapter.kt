package com.example.user.sceball

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.user.sceball.fragment.NextMatchFragment
import com.example.user.sceball.fragment.PassMatchFragment
import com.example.user.sceball.fragment.SearchMatchFragment

/**
 * Created by Bencoleng on 11/11/2018.
 */
class TabMatchAdapter(fragmentManager: FragmentManager, private val count: Int) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NextMatchFragment()
            1 -> PassMatchFragment()
            else -> SearchMatchFragment()
        }
    }

    override fun getCount() = count

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Next Match"
        1 -> "Prev Match"
        else -> "Search Match"
    }
}