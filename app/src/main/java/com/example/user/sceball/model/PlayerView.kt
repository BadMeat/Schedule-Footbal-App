package com.example.user.sceball.model

/**
 * Created by Bencoleng on 18/11/2018.
 */
interface PlayerView {
    fun showLoading()
    fun hideLoading()
    fun showPlayer(data: List<Player>)
    fun showTeam(data: List<Team>)
}