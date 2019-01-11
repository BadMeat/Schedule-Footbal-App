package com.example.user.sceball.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Bencoleng on 11/11/2018.
 */
data class League(
    @SerializedName("idLeague")
    var idLeague: String? = null,

    @SerializedName("strLeague")
    var strLeague: String? = null,

    @SerializedName("strSport")
    var strSport: String? = null
)