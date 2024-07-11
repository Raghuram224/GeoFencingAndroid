package com.example.geofencing

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.geofencing.ui.theme.GeoFencingTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoFencingTheme {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { getCurrentLocation() }) {
                        Text(text = "Get location")
                    }
                }
            }
        }
    }

    private fun getCurrentLocation() {
        // Checking Location Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            resultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, OnSuccessListener { location: Location? ->
            if (location != null) {
                val lat = location.latitude
                val lon = location.longitude
                Log.i("location","$lat,$lon")
            }
        })
    }

    private val resultLauncher = registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
        }
    }
}

