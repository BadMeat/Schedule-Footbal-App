package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.TeamResponse
import com.example.user.sceball.model.TeamView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamDetailPresenter(
    private val view: TeamView,
    private val repo: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeamById(idTeam: String?) {
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                repo.doRequest(ApiRepository.TheSportDBApi.getTeamById(idTeam)).await(), TeamResponse::class.java
            )
                view.showTeam(data.teams)
        }
    }
}