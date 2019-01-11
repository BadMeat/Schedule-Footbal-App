package com.example.user.sceball

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.user.sceball.fragment.FavoriteMatchFragment
import com.example.user.sceball.fragment.FavoritePlayerFragment
import com.example.user.sceball.fragment.FavoriteTeamFragment

class TabFavoriteAdapter(fragmentManager: FragmentManager, private val count: Int) :
    FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteMatchFragment()
            1 -> FavoriteTeamFragment()
            else -> FavoritePlayerFragment()
        }
    }

    override fun getCount() = count

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Match"
        1 -> "Team"
        else -> "Player"
    }
}