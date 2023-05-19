package com.fcemtopall.openskynetwork.data.remote

import com.fcemtopall.openskynetwork.data.api.ApiService
import com.fcemtopall.openskynetwork.domain.entity.BaseDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource() {

    suspend fun getAllAircraftStates() = getResult { apiService.getAllAircraftStates() }


}