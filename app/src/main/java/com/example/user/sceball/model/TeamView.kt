package com.example.user.sceball.model

/**
 * Created by Bencoleng on 13/11/2018.
 */
interface TeamView {
    fun showLoading()
    fun hideLoading()
    fun showTeam(data: List<Team>)
    fun showAllLeague(data: List<League>)
}