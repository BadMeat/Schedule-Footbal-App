package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.*
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Bencoleng on 18/11/2018.
 */
class PlayerPresenter(
    private val view: PlayerView,
    private val repo: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getPlayer(teamName: String?) {
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                repo.doRequest(ApiRepository.TheSportDBApi.getPlayerByTeam(teamName)).await(),
                PlayerResponse::class.java
            )
            view.hideLoading()
            val b: MutableList<Player> = mutableListOf()
            @Suppress("SENSELESS_COMPARISON")
            if (null != data.player) {
                b.addAll(data.player)
            }

            if (b.isEmpty()) {
                view.showPlayer(mutableListOf())
            } else {
                view.showPlayer(data.player)
            }

        }
    }

    fun getTeam(teamId: String?) {
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                repo.doRequest(ApiRepository.TheSportDBApi.getTeamById(teamId)).await(),
                TeamResponse::class.java
            )
            view.hideLoading()
            val e: List<Team>? = data.teams
            if (e != null) {
                view.showTeam(data.teams)
            } else {
                view.showTeam(mutableListOf())
            }
        }
    }
}