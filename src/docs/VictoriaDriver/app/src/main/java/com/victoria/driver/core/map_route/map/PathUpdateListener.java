package com.victoria.driver.core.map_route.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hlink21 on 28/3/17.
 */

public interface PathUpdateListener {

    void updatePath(LatLng location);
}
