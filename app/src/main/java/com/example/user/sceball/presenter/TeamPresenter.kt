package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.*
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by Bencoleng on 13/11/2018.
 */
class TeamPresenter(
    private val view: TeamView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeam(league: String?) {
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                apiRepository.doRequest(ApiRepository.TheSportDBApi.getAllTeam(league)).await(),
                TeamResponse::class.java
            )

            view.hideLoading()
            val q: List<Team>? = data.teams
            val w: List<Team> = arrayListOf()
            if (q != null) {
                view.showTeam(q)
            } else {
                view.showTeam(w)
            }
        }
    }

    fun getLeague() {
        GlobalScope.launch(contextPool.main) {
            val tempLeague = gson.fromJson(
                apiRepository.doRequest(ApiRepository.TheSportDBApi.getAllLeague()).await(), LeaguesResponse::class.java
            )
            val e: List<League>? = tempLeague.leagues
            val q: List<League> = Arrays.asList()
            if(e != null){
                view.showAllLeague(e)
            }else{
                view.showAllLeague(q)
            }
            view.hideLoading()
        }
    }
}