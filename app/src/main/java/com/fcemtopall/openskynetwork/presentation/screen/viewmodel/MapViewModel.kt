package com.fcemtopall.openskynetwork.presentation.screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    lateinit var aircraftListOriginCountry : MutableLiveData<List<States>>

    fun getAllAircraftStates(): LiveData<Resource<OpenSkyModel<States>>> =
        apiRepository.getAllAircraftStates()

    fun getCountryList(): List<String> {
        val countryList = mutableListOf<String>()
        aircraftListOriginCountry.value?.forEach { aircraft ->
            if (!countryList.contains(aircraft.origin_country)) {
                aircraft.origin_country?.let { countryList.add(it) }
            }
        }
        return countryList
    }

}