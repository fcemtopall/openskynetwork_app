package com.fcemtopall.openskynetwork.presentation.screen

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.fcemtopall.openskynetwork.R
import com.fcemtopall.openskynetwork.databinding.ActivityMainBinding
import com.fcemtopall.openskynetwork.presentation.screen.viewmodel.MapViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var aircraftViewModel: MapViewModel
    private lateinit var _binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.fragment)

        aircraftViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        aircraftViewModel.getAllAircraftStates()

        aircraftViewModel.getCountryList().let { countryList ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            _binding.countrySpinner.adapter = adapter
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }


}