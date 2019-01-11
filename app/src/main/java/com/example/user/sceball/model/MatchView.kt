package com.example.user.sceball.model

/**
 * Created by Bencoleng on 11/11/2018.
 */
interface MatchView {
    fun showLoading()
    fun hideLoading()
    fun showPassEvent(data: List<MatchEvent>)
    fun showAllLeague(data: List<League>)
}