package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.*
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Bencoleng on 11/11/2018.
 */
class MatchPresenter(
    private val view: MatchView,
    private val apiRepo: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getPassEvent(id: String?) {
        view.showLoading()
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                apiRepo.doRequest(ApiRepository.TheSportDBApi.getPassEvent(id)).await(), MatchResponse::class.java
            )
            view.hideLoading()
            val e: List<MatchEvent>? = data.events
            val q: List<MatchEvent> = Arrays.asList()
            if (e != null) {
                view.showPassEvent(e)
            } else {
                view.showPassEvent(q)
            }
        }
    }

    fun getNextEvent(id: String?) {
        view.showLoading()
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                apiRepo.doRequest(ApiRepository.TheSportDBApi.getNextEvent(id)).await(), MatchResponse::class.java
            )
            view.hideLoading()
            val e: List<MatchEvent>? = data.events
            val q: List<MatchEvent> = Arrays.asList()
            if (e != null) {
                view.showPassEvent(e)
            } else {
                view.showPassEvent(q)
            }
        }
    }


    fun getAllLeague() {
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                apiRepo.doRequest(ApiRepository.TheSportDBApi.getAllLeague()).await(), LeaguesResponse::class.java
            )
            val e: List<League>? = data.leagues
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