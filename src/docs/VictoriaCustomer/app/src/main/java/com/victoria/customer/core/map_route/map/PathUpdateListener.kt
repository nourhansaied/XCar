package com.victoria.customer.core.map_route.map

import com.google.android.gms.maps.model.LatLng

/**
 * Created by hlink21 on 28/3/17.
 */

interface PathUpdateListener {

    fun updatePath(location: LatLng)
}
