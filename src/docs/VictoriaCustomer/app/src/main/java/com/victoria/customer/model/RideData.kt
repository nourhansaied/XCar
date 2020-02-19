package com.victoria.customer.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

/**
 * Created on 30/11/18.
 */
@Parcelize
data class RideData(
        var pickup_address: String? = null,
        var dropoff_address: String? = null,
        var vehicle_id: String? = null,
        var vehicle_name: String? = null,
        var pickup_latitude: String? = null,
        var pickup_longitude: String? = null,
        var dropoff_latitude: String? = null,
        var dropoff_longitude: String? = null,
        @Expose
        var isCarSelected: Boolean = false,
        @Expose
        var carType: Int = 0,
        var trip_type: String? = null,
        var payment_mode: String? = null,
        var rideby: String? = null,
        @Expose
        var pickup_dropoff: Boolean = false,
        var promocode: String? = null,
        var tripdatetime: String? = null,
        var guest_name: String? = null,
        var guest_country_code: String? = null,
        var guest_phone: String? = null,
        var ride_type: String? = null,
        var isNearByDriver: Boolean = false

        /**
         * check_circle
        vehicle_id,pickup_address,pickup_latitude,pickup_longitude,dropoff_address,dropoff_latitude,dropoff_longitude,trip_type[Now,Later],payment_mode[wallet,cash,card],rideby[Me,Other] */


) : Parcelable
