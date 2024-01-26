package com.mobappdev.courier

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobappdev.courier.databinding.ActivityMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var addButton: Button
    private lateinit var routeButton: Button
    private lateinit var mapHelper: MapHelper
    private lateinit var mapObjects: MapObjectCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mapview.map.move(CameraPosition
            (Point(41.326666, 69.285369), 5.0f, 0.0f, 0.0f),
        Animation(Animation.Type.SMOOTH, 1f), null)

        mapHelper = MapHelper.getInstance(application)
        val mapView = mapHelper.mapView

        storeAdapter = StoreAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storeAdapter
        }

        //fetchStoreData() uncomment it when base_url has been decided

        binding.addButton.setOnClickListener {
            showAddPointDialog(mapView)
        }

        binding.routeButton.setOnClickListener {
            val enabledStores = storeAdapter.differ.currentList.filter { it.isEnabled == true }
            displayStoresOnMap(enabledStores)
        }
    }

    private fun displayStoresOnMap(stores: List<Store>) {
        // to clear existing map objects
        mapObjects.clear()

        // Display current location with pointer

        val currentLocation = binding.mapview.map.cameraPosition.target
        val currentLocationPlacemark = mapObjects.addPlacemark(currentLocation)
        currentLocationPlacemark.userData = PointUserData("Current Location", "", "")

    }

    private fun fetchStoreData() {
        val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
        val call = apiService.getStores()

        call.enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                if (response.isSuccessful) {
                    val storeList = response.body()
                    storeList?.let {
                        // Update the RecyclerView adapter with the fetched data
                        storeAdapter.differ.submitList(it)
                    }
                }
            }
            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                // Handle failure
                null
            }
        })
    }

    private fun showAddPointDialog(mapView: MapView) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextContactPerson = dialogView.findViewById<EditText>(R.id.editTextContactPerson)
        val editTextPhone = dialogView.findViewById<EditText>(R.id.editTextPhone)
        val textViewLocation = dialogView.findViewById<TextView>(R.id.textViewLocation)

        // Get the current map center as the default location
        val mapCenter = mapView.map.cameraPosition.target
        textViewLocation.text = "Location: ${mapCenter.latitude}, ${mapCenter.longitude}"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Point")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->

                // Handle the data entered by the user (e.g., save to database)
                val name = editTextName.text.toString()
                val contactPerson = editTextContactPerson.text.toString()
                val phone = editTextPhone.text.toString()

                val mapCenter = mapView.map.cameraPosition.target
                val latitude = mapCenter.latitude
                val longitude = mapCenter.longitude

                val store = Store(
                    id = 0, // You may need to adjust this depending on your API requirements
                    name = name,
                    latitude = latitude,
                    longitude = longitude,
                    contactPerson = contactPerson,
                    phone = phone,
                    isEnabled = true // You can adjust this based on your needs
                )

                // Make the Retrofit API call to add the store
                val apiService = RetrofitInstance.retrofit.create(ApiService::class.java)
                val call = apiService.addStore(store)
                call.enqueue(object:retrofit2.Callback<Store>{
                    override fun onResponse(call: Call<Store>, response: Response<Store>) {
                        response.body()?.let {
                            //uspex
                        }?: {
                        //neuspex
                        }
                    }

                    override fun onFailure(call: Call<Store>, t: Throwable) {

                    }
                })

                // Create a map marker at the current location
                val mapObject = mapObjects.addPlacemark(mapCenter)
                mapObject.userData = PointUserData(name, contactPerson, phone)

                Toast.makeText(
                    this,
                    "Name: $name\nContact Person: $contactPerson\nPhone: $phone\nLocation: $mapCenter",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }



    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        binding.mapview.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }


    private data class PointUserData(val name: String, val contactPerson: String, val phone: String)

}


/*Handling Route Path:
To handle the route path, you would need to use the Yandex MapKit's routing API.
You would need to make API calls to get the route information and display it on the map.
Please refer to the Yandex MapKit documentation for routing for more details on how to achieve this.*/