package com.example.user.sceball.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Bencoleng on 11/11/2018.
 */
data class MatchEvent(
    @SerializedName("idEvent")
    var idEvent: String? = null,

    @SerializedName("strHomeTeam")
    var nmHome: String? = null,

    @SerializedName("strAwayTeam")
    var nmAway: String? = null,

    @SerializedName("dateEvent")
    var dateEvent: String? = null,

    @SerializedName("strTime")
    var strTime: String? = null,

    /*
     * Jenis
     */
    @SerializedName("strSport")
    var strSport: String? = null,

    /*
     * Home
     */
    @SerializedName("idHomeTeam")
    var idHome: Int? = null,

    @SerializedName("idAwayTeam")
    var idAway: Int? = null,

    @SerializedName("intHomeScore")
    var intHomeScore: Int? = null,

    @SerializedName("strHomeGoalDetails")
    var strHomeGoalDetails: String? = null,

    @SerializedName("intHomeShots")
    var intHomeShots: Int? = null,

    @SerializedName("strHomeLineupGoalkeeper")
    var strHomeLineupGoalkeeper: String? = null,

    @SerializedName("strHomeLineupDefense")
    var strHomeLineupDefense: String? = null,

    @SerializedName("strHomeLineupMidfield")
    var strHomeLineupMidfield: String? = null,

    @SerializedName("strHomeLineupForward")
    var strHomeLineupForward: String? = null,

    @SerializedName("strHomeLineupSubstitutes")
    var strHomeLineupSubstitutes: String? = null,

    /*
     * Away
     */

    @SerializedName("intAwayScore")
    var intAwayScore: Int? = null,

    @SerializedName("strAwayGoalDetails")
    var strAwayGoalDetails: String? = null,

    @SerializedName("intAwayShots")
    var intAwayShots: Int? = null,

    @SerializedName("strAwayLineupGoalkeeper")
    var strAwayLineupGoalkeeper: String? = null,

    @SerializedName("strAwayLineupDefense")
    var strAwayLineupDefense: String? = null,

    @SerializedName("strAwayLineupMidfield")
    var strAwayLineupMidfield: String? = null,

    @SerializedName("strAwayLineupForward")
    var strAwayLineupForward: String? = null,

    @SerializedName("strAwayLineupSubstitutes")
    var strAwayLineupSubstitutes: String? = null
)