package com.example.user.sceball.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Bencoleng on 12/11/2018.
 */
data class TeamDetail(
    @SerializedName("strTeamBadge")
    var teamId: String? = null,
    @SerializedName("strTeam")
    var teamNm: String? = null
)