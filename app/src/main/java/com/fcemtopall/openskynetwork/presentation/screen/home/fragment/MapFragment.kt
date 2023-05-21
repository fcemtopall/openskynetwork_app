package com.fcemtopall.openskynetwork.presentation.screen.home.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.fcemtopall.openskynetwork.R
import com.fcemtopall.openskynetwork.data.common.Resource
import com.fcemtopall.openskynetwork.databinding.FragmentMapBinding
import com.fcemtopall.openskynetwork.domain.entity.States
import com.fcemtopall.openskynetwork.presentation.screen.constant.MapConstants
import com.fcemtopall.openskynetwork.presentation.screen.constant.MapConstants.Companion.DEFAULT_ANIMATION_DURATION
import com.fcemtopall.openskynetwork.presentation.screen.constant.MapConstants.Companion.INTERVAL
import com.fcemtopall.openskynetwork.presentation.screen.constant.MapConstants.Companion.INTERVAL_TIME
import com.fcemtopall.openskynetwork.presentation.screen.viewmodel.MapViewModel
import com.fcemtopall.openskynetwork.utils.DialogUtils
import com.fcemtopall.openskynetwork.utils.StringUtils
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment() {

    private lateinit var _binding: FragmentMapBinding

    private lateinit var mapView: MapView
    private val viewModel: MapViewModel by viewModels()
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return _binding.root




    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = _binding.maps
        mapView.map.isRotateGesturesEnabled = false

        cameraPositionMove()
        getAllPlaneStates()
        yandexMapInitialize()
        startCountDown()

        _binding.zoomIn.setOnClickListener { zoomIn() }
        _binding.zoomOut.setOnClickListener { zoomOut() }

    }


    private fun cameraPositionMove(){
        _binding.maps.map.move(
            CameraPosition(Point(0.0, 0.0), 4.0f, 0.0f, 0.0f)
        )
    }


    private fun getAllPlaneStates() {
        viewModel.getAllAircraftStates().observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.LOADING -> _binding.loadingBar.playAnimation()
                Resource.Status.SUCCESS -> {
                    viewModel.aircraftList = response.data?.states
                    showAircraftOnMap(viewModel.aircraftList, mapView)

                }
                Resource.Status.ERROR -> context?.let {
                    DialogUtils.showAlert(
                        it,R.string.alert_dialog_error_text,R.string.api_error_message,R.string.alert_dialog_refresh_text
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
        MapKitFactory.initialize(context)
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

    private fun startCountDown(){
        countDownTimer = object : CountDownTimer(MapConstants.INTERVAL_TIME.toLong(),
            INTERVAL.toLong()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                _binding.countdownTextView.text = resources.getString(R.string.countdown_message,seconds.toString())
            }

            override fun onFinish() {
                _binding.countdownTextView.text = resources.getString(R.string.countdown_end_message)
                getAllPlaneStates()
            }
        }

        countDownTimer.start()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        countDownTimer.cancel()
    }

}