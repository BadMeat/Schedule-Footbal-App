package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.MatchEvent
import com.example.user.sceball.model.SearchMatchResponse
import com.example.user.sceball.model.SearchMatchView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Bencoleng on 11/11/2018.
 */
class SearchPresenter(
    private val view: SearchMatchView,
    private val apiRepo: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getSearchEvent(name: String?) {
        view.showLoading()
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                apiRepo.doRequest(ApiRepository.TheSportDBApi.searchMatch(name)).await(), SearchMatchResponse::class.java
            )
                view.hideLoading()
                val e : List<MatchEvent>? = data.event
                val q : List<MatchEvent> = Arrays.asList()
                if (e != null) {
                    view.showSearchEvent(e)
                }else{
                    view.showSearchEvent(q)
                }
        }
    }
}