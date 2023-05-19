package com.fcemtopall.openskynetwork.domain.entity

import com.google.gson.annotations.SerializedName
import retrofit2.Response

open class OpenSkyModel<DataModel> {

    @SerializedName("time")
    val time : Long? = null
    @SerializedName("states")
    val states : List<States>? = null
}