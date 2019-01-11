package com.example.user.sceball

import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Created by Bencoleng on 27/11/2018.
 */
class ApiRepositoryTest {
    @Test
    fun testDoRequest() {
        val apiRepository = mock(ApiRepository::class.java)
        val url = "https://www.thesportsdb.com/api/v1/json/1/search_all_teams.php?l=English%20Premier%20League"
        apiRepository.doRequest(url)
        verify(apiRepository).doRequest(url)
    }

    @Test
    fun testDoRequestPass() {
        val apiRepository = mock(ApiRepository::class.java)
        val url = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328"
        apiRepository.doRequest(url)
        verify(apiRepository).doRequest(url)
    }

    @Test
    fun testListPlayerRequestPass() {
        val apiRepository = mock(ApiRepository::class.java)
        val url = " https://www.thesportsdb.com/api/v1/json/1/lookup_all_players.php?id=133604"
        apiRepository.doRequest(url)
        verify(apiRepository).doRequest(url)
    }
}