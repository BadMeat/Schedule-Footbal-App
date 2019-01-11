package com.example.user.sceball

import com.example.user.sceball.model.MatchEvent
import com.example.user.sceball.model.MatchResponse
import com.example.user.sceball.model.MatchView
import com.example.user.sceball.presenter.MatchPresenter
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Bencoleng on 27/11/2018.
 */
class MatchPresenterTest {
    @Mock
    private
    lateinit var view: MatchView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Mock
    private
    lateinit var presenter: MatchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(
            view,
            apiRepository,
            gson,
            TestContextProvider()
        )
    }

    @Test
    fun getNextList() {
        val teams: MutableList<MatchEvent> = mutableListOf()
        val response = MatchResponse(teams)

        GlobalScope.launch {
            Mockito.`when`(
                gson.fromJson(
                    apiRepository
                        .doRequest(ApiRepository.TheSportDBApi.getPassEvent("4328")).await(),
                    MatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getPassEvent("4328")


            Mockito.verify(view).showLoading()
            Mockito.verify(view).showPassEvent(teams)
            Mockito.verify(view).hideLoading()
        }
    }
}