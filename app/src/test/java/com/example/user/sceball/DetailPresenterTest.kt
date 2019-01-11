package com.example.user.sceball

import com.example.user.sceball.model.DetailView
import com.example.user.sceball.model.MatchEvent
import com.example.user.sceball.model.MatchResponse
import com.example.user.sceball.presenter.DetailPresenter
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
class DetailPresenterTest {
    @Mock
    private
    lateinit var view: DetailView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Mock
    private
    lateinit var presenter: DetailPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailPresenter(
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
                        .doRequest(ApiRepository.TheSportDBApi.getDetailEvent("441613")).await(),
                    MatchResponse::class.java
                )
            ).thenReturn(response)

            presenter.getDetailEvent("441613")


            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventList(teams)
            Mockito.verify(view).hideLoading()
        }
    }
}