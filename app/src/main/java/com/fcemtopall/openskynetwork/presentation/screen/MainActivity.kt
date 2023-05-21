package com.fcemtopall.openskynetwork.presentation.screen

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.fcemtopall.openskynetwork.R
import com.fcemtopall.openskynetwork.databinding.ActivityMainBinding
import com.fcemtopall.openskynetwork.presentation.screen.home.fragment.MapFragment
import com.fcemtopall.openskynetwork.presentation.screen.viewmodel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : AppCompatActivity() {

    private lateinit var aircraftViewModel: MapViewModel
    private lateinit var _binding : ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var spinner: Spinner
    private lateinit var mapFragment: MapFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        navController = Navigation.findNavController(this, R.id.fragment)

        aircraftViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        aircraftViewModel.getAllAircraftStates()

        aircraftViewModel.getCountryList().let { countryList ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _binding.countrySpinner.adapter = adapter
        }

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment



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

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

}