package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.Team
import com.example.user.sceball.model.TeamResponse
import com.example.user.sceball.model.TeamView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Bencoleng on 19/11/2018.
 */
class SearchTeamPresenter(
    private val view: TeamView,
    private val repo: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeamByName(name: String?) {
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                repo.doRequest(ApiRepository.TheSportDBApi.searchTeamByName(name)).await(), TeamResponse::class.java
            )
            view.hideLoading()
            val listTeam: List<Team>? = data.teams
            if (listTeam != null) {
                view.showTeam(data.teams)
            } else {
                view.showTeam(mutableListOf())
            }
        }
    }
}