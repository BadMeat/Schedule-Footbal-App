package com.example.user.sceball.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Bencoleng on 13/11/2018.
 */
data class Team(
    @SerializedName("idTeam")
    var idTeam: String? = null,

    @SerializedName("strTeam")
    var strTeam: String? = null,

    @SerializedName("strTeamBadge")
    var strTeamBadge: String? = null,

    @SerializedName("strDescriptionEN")
    var strDescriptionEN: String? = null,

    @SerializedName("strSport")
    var strSport: String? = null,

    @SerializedName("strLeague")
    var strLeague: String? = null,

    @SerializedName("intFormedYear")
    var intFormedYear: String? = null,

    @SerializedName("strManager")
    var strManager: String? = null,

    @SerializedName("strStadium")
    var strStadium: String? = null
)