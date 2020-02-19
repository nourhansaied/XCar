package com.victoria.customer.di;

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.PlaceOrder
import com.victoria.customer.model.RideData
import java.util.*

class VictoriaCustomer : Application() {


    companion object {
        internal lateinit var sharedPreferences: SharedPreferences

        private var rideData: RideData = RideData()
        private var tripData: PlaceOrder = PlaceOrder()

        fun isRTL_ar(): Boolean {
            return Locale.getDefault().language == "ar"
        }

        fun getRideData(): RideData {
            return rideData
        }

        fun setRideData() {
            rideData = RideData()
        }

        fun getTripData(): PlaceOrder {
            return tripData
        }

        fun setTripData(data: PlaceOrder?) {
            tripData = PlaceOrder()
            tripData = data!!
        }
    }


    override fun onCreate() {
        super.onCreate()

        Injector.INSTANCE.initAppComponent(this, "VICTORIA")
    }


}
