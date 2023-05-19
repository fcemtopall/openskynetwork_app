package com.fcemtopall.openskynetwork.presentation.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fcemtopall.openskynetwork.data.common.Resource
import com.fcemtopall.openskynetwork.data.repository.ApiRepository
import com.fcemtopall.openskynetwork.domain.entity.OpenSkyModel
import com.fcemtopall.openskynetwork.domain.entity.States
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private var apiRepository: ApiRepository) : ViewModel() {

    var aircraftList: List<States>? = null

    fun getAllAircraftStates(): LiveData<Resource<OpenSkyModel<States>>> =
        apiRepository.getAllAircraftStates()
}