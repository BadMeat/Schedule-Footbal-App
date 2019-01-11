package com.example.user.sceball.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Bencoleng on 18/11/2018.
 */
data class Player(

    @SerializedName("idPlayer")
    var idPlayer: String? = null,

    @SerializedName("strPlayer")
    var strPlayer: String? = null,

    @SerializedName("dateBorn")
    var dateBorn: String? = null,

    @SerializedName("dateSigned")
    var dateSigned: String? = null,

    @SerializedName("strSigning")
    var strSigning: String? = null,

    @SerializedName("strWage")
    var strWage: String? = null,

    @SerializedName("strBirthLocation")
    var strBirthLocation: String? = null,

    @SerializedName("strCutout")
    var strCutout: String? = null,

    @SerializedName("strDescriptionEN")
    var strDescriptionEN: String? = null,

    @SerializedName("strTeam")
    var strTeam: String? = null,

    @SerializedName("strPosition")
    var strPosition: String? = null,

    @SerializedName("strNationality")
    var strNationality: String? = null
)