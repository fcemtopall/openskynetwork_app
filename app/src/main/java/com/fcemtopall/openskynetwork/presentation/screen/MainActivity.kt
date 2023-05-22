package com.fcemtopall.openskynetwork.presentation.screen

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fcemtopall.openskynetwork.R
import com.fcemtopall.openskynetwork.data.common.Resource
import com.fcemtopall.openskynetwork.databinding.ActivityMainBinding
import com.fcemtopall.openskynetwork.domain.entity.States
import com.fcemtopall.openskynetwork.presentation.screen.constant.MapConstants
import com.fcemtopall.openskynetwork.presentation.screen.constant.MapConstants.Companion.DEFAULT_ANIMATION_DURATION
import com.fcemtopall.openskynetwork.presentation.screen.constant.MapConstants.Companion.INTERVAL
import com.fcemtopall.openskynetwork.presentation.screen.viewmodel.MapViewModel
import com.fcemtopall.openskynetwork.utils.DialogUtils
import com.fcemtopall.openskynetwork.utils.StringUtils
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class MainActivity : AppCompatActivity() {

    private lateinit var aircraftViewModel: MapViewModel
    private lateinit var _binding: ActivityMainBinding

    private lateinit var mapView: MapView
    private val viewModel: MapViewModel by viewModels()
    private lateinit var countDownTimer: CountDownTimer

    private lateinit var spinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        mapView = _binding.maps
        mapView.map.isRotateGesturesEnabled = false

        cameraPositionMove()
        getAllPlaneStates()
        yandexMapInitialize()
        startCountDown()

        _binding.zoomIn.setOnClickListener { zoomIn() }
        _binding.zoomOut.setOnClickListener { zoomOut() }


        aircraftViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        aircraftViewModel.getAllAircraftStates()

        aircraftViewModel.getCountryList().let { countryList ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _binding.countrySpinner.adapter = adapter
        }




        _binding.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = spinner.getItemAtPosition(position).toString()
                    // mapFragment.processSelectedItem(selectedItem)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }


            }


    }

    private fun cameraPositionMove() {
        _binding.maps.map.move(
            CameraPosition(Point(0.0, 0.0), 4.0f, 0.0f, 0.0f)
        )
    }


    private fun getAllPlaneStates() {
        viewModel.getAllAircraftStates().observe(this) { response ->
            when (response.status) {
                Resource.Status.LOADING -> _binding.loadingBar.playAnimation()
                Resource.Status.SUCCESS -> {
                    viewModel.aircraftList = response.data?.states
                    showAircraftOnMap(viewModel.aircraftList, mapView)

                }
                Resource.Status.ERROR -> this.let {
                    DialogUtils.showAlert(
                        it,
                        R.string.alert_dialog_error_text,
                        R.string.api_error_message,
                        R.string.alert_dialog_refresh_text
                    ) { getAllPlaneStates() }
                }
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
        MapKitFactory.initialize(applicationContext)
    }

    private fun zoomIn() {
        val currentZoom = mapView.map.cameraPosition.zoom
        val newZoom = currentZoom + 1.0f
        mapView.map.move(
            CameraPosition(
                mapView.map.cameraPosition.target,
                newZoom,
                mapView.map.cameraPosition.azimuth,
                mapView.map.cameraPosition.tilt
            ),
            Animation(Animation.Type.SMOOTH, DEFAULT_ANIMATION_DURATION),
            null
        )
    }

    private fun zoomOut() {
        val currentZoom = mapView.map.cameraPosition.zoom
        val newZoom = currentZoom - 1.0f
        mapView.map.move(
            CameraPosition(
                mapView.map.cameraPosition.target,
                newZoom,
                mapView.map.cameraPosition.azimuth,
                mapView.map.cameraPosition.tilt
            ),
            Animation(Animation.Type.SMOOTH, DEFAULT_ANIMATION_DURATION),
            null
        )
    }

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(
            MapConstants.INTERVAL_TIME.toLong(),
            INTERVAL.toLong()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                _binding.countdownTextView.text =
                    resources.getString(R.string.countdown_message, seconds.toString())
            }

            override fun onFinish() {
                _binding.countdownTextView.text =
                    resources.getString(R.string.countdown_end_message)
                getAllPlaneStates()
            }
        }

        countDownTimer.start()
    }


}