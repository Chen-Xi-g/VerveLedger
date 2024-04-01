package com.griffin.feature.map

import com.amap.api.maps.MapsInitializer
import com.griffin.core.utils.Utils


object MapView {
    fun initMap(){
        MapsInitializer.updatePrivacyShow(Utils.context, true, true)
        MapsInitializer.updatePrivacyAgree(Utils.context, true)
    }
}