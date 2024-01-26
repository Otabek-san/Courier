package com.mobappdev.courier

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("1f326866-ce82-4ef3-8b02-3a7d6208e32a")
        MapKitFactory.initialize(this)
        MapHelper.getInstance(this)

    }
}