package com.mobappdev.courier

import android.app.Application
import com.yandex.mapkit.mapview.MapView

class MapHelper private constructor(application: Application){

    companion object {
        private var instance: MapHelper? = null

        fun getInstance(application: Application): MapHelper {
            if (instance == null) {
                instance = MapHelper(application)
            }
            return instance!!
        }
    }

    val mapView: MapView by lazy {
        MapView(application)
    }

}