package com.example.user.sceball.model

/**
 * Created by Bencoleng on 12/11/2018.
 */
interface DetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<TeamDetail>)
    fun showAwayList(data: List<TeamDetail>)
    fun showEventList(data: List<MatchEvent>)
}