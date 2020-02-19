package com.victoria.driver.di

import android.app.Application
import com.victoria.driver.data.pojo.PlaceOrder

class App : Application() {
    companion object {

        private var tripData: PlaceOrder = PlaceOrder()

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
