package com.fcemtopall.openskynetwork.data.repository

import com.fcemtopall.openskynetwork.data.performNetworkOperation
import com.fcemtopall.openskynetwork.data.remote.RemoteDataSource
import javax.inject.Inject

class ApiRepository @Inject constructor(private var remoteDataSource: RemoteDataSource) {

    fun getAllAircraftStates() = performNetworkOperation {
        remoteDataSource.getAllAircraftStates()
    }
}