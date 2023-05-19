package com.fcemtopall.openskynetwork.presentation.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fcemtopall.openskynetwork.data.common.Resource
import com.fcemtopall.openskynetwork.databinding.ActivityMainBinding
import com.fcemtopall.openskynetwork.domain.entity.States
import com.fcemtopall.openskynetwork.utils.StringUtils
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private val viewModel: MapViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        yandexMapInitialize()

        getAllPlaneStates()


    }


    init {
        binding.maps.map.move(
            CameraPosition(Point(0.0, 0.0), 4.0f, 0.0f, 0.0f)
        )
        mapView = binding.maps
        mapView.map.isRotateGesturesEnabled = false
    }


    private fun getAllPlaneStates() {
        viewModel.getAllAircraftStates().observe(this) { response ->
            when (response.status) {
                Resource.Status.LOADING -> binding.loadingBar.playAnimation()
                Resource.Status.SUCCESS -> {
                    viewModel.aircraftList = response.data?.states
                    showAircraftOnMap(viewModel.aircraftList, mapView)

                }
                else -> {}
            }
        }
    }

    private fun showAircraftOnMap(aircraftList: List<States>?, mapView: MapView) {
        val mapObjects = mapView.map.mapObjects.addCollection()

        if (aircraftList != null) {
            for (aircraft in aircraftList) {
                val point = aircraft.latitude?.let {
                    aircraft.longitude?.let { it1 ->
                        Point(
                            it.toFloat()
                                .toDouble(), it1.toFloat().toDouble()
                        )
                    }
                }
                val placemark = point?.let { mapObjects.addPlacemark(it) }
                if (placemark != null) {
                    placemark.userData = aircraft
                }

            }
        }

    }


    private fun yandexMapInitialize() {
        MapKitFactory.setApiKey(StringUtils.getValueFromLocalProperties("yandex_api_key"))
        MapKitFactory.initialize(this)
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


}