package com.griffin.ledger

import com.griffin.core.base.App
import com.griffin.feature.map.MapView
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : App() {

    override fun onCreate() {
        super.onCreate()
        MapView.initMap()
    }

}