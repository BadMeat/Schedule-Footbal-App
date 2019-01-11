package com.example.user.sceball.presenter

import com.example.user.sceball.ApiRepository
import com.example.user.sceball.CoroutineContextProvider
import com.example.user.sceball.model.Player
import com.example.user.sceball.model.PlayerDetailResponse
import com.example.user.sceball.model.PlayerView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Bencoleng on 20/11/2018.
 */
class PlayerDetailPresenter(
    private val view: PlayerView,
    private val repo: ApiRepository,
    private val gson: Gson,
    private val contextPool: CoroutineContextProvider = CoroutineContextProvider()
) {
    fun getDetailPlayer(idPlayer: String?) {
        GlobalScope.launch(contextPool.main) {
            val data = gson.fromJson(
                repo.doRequest(ApiRepository.TheSportDBApi.getPlayerById(idPlayer)).await(), PlayerDetailResponse::class.java
            )
                view.hideLoading()
                val list: List<Player> = data.players
                if (list.isEmpty()) {
                    view.showPlayer(mutableListOf())
                } else {
                    view.showPlayer(list)
                }
        }
    }
}