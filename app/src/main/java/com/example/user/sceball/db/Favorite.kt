package com.example.user.sceball.db

/**
 * Created by Bencoleng on 13/11/2018.
 */
data class Favorite(
    val id: Long?, val eventId: String?, val evenTime: String?,
    val homeId: Int?, val homeName: String?, val homeBadge: String?, val homeScore: Int?,
    val awayId: Int, val awayName: String?, val awayBadge: String?, val awayScore: Int?
) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val EVENT_TIME: String = "EVENT_TIME"

        const val HOME_ID: String = "HOME_ID"
        const val HOME_NAME: String = "HOME_NAME"
        const val HOME_BADGE: String = "HOME_BADGE"
        const val HOME_SCORE: String = "HOME_SCORE"

        const val AWAY_ID: String = "AWAY_ID"
        const val AWAY_NAME: String = "AWAY_NAME"
        const val AWAY_BADGE: String = "AWAY_BADGE"
        const val AWAY_SCORE: String = "AWAY_SCORE"
    }
}