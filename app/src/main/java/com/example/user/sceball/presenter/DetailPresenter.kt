package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.DetailView
import com.example.user.sceball.model.MatchResponse
import com.example.user.sceball.model.TeamDetail
import com.example.user.sceball.model.TeamDetailResponse
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Bencoleng on 12/11/2018.
 */
class DetailPresenter(
    private val view: DetailView,
    private val apiRepository: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getTeam(idTeam: Int?, idAway: Int?) {
        view.showLoading()
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequest(ApiRepository.TheSportDBApi.getTeam(idTeam)).await(),
                TeamDetailResponse::class.java
            )
            val data2 = gson.fromJson(
                apiRepository
                    .doRequest(ApiRepository.TheSportDBApi.getTeam(idAway)).await(),
                TeamDetailResponse::class.java
            )
            view.hideLoading()

            val e: List<TeamDetail>? = data.teams
            val q: List<TeamDetail> = Arrays.asList()
            if (e != null) {
                view.showTeamList(e)
            } else {
                view.showTeamList(q)
            }

            val r: List<TeamDetail>? = data2.teams
            val t: List<TeamDetail> = Arrays.asList()
            if (r != null) {
                view.showAwayList(r)
            } else {
                view.showAwayList(t)
            }
        }
    }

    fun getDetailEvent(idEvent: String?) {
        view.showLoading()
        GlobalScope.launch(contextPool.main) {
            val data =
                gson.fromJson(
                    apiRepository
                        .doRequest(ApiRepository.TheSportDBApi.getDetailEvent(idEvent)).await(),
                    MatchResponse::class.java
                )
            view.showEventList(data.events)
            view.hideLoading()
        }
    }
}