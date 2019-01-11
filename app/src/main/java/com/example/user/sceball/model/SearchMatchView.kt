package com.example.user.sceball.model

/**
 * Created by Bencoleng on 11/11/2018.
 */
interface SearchMatchView {
    fun showLoading()
    fun hideLoading()
    fun showSearchEvent(data: List<MatchEvent>)
}