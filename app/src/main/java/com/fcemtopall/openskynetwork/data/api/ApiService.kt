package com.fcemtopall.openskynetwork.data.api

import com.fcemtopall.openskynetwork.domain.entity.OpenSkyModel
import com.fcemtopall.openskynetwork.domain.entity.States
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(ApiConstants.STATES_ALL)
    suspend fun getAllAircraftStates() : Response<OpenSkyModel<States>>
}