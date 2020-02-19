package com.victoria.customer.ui.home.viewmodel

import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.AccessData
import com.victoria.customer.data.pojo.PlaceOrder
import com.victoria.customer.data.repository.TripRepository
import com.victoria.customer.model.NearByDriver
import com.victoria.customer.model.Parameter
import com.victoria.customer.model.VehicleData
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val tripRepository: TripRepository) : BaseViewModel() {

    var nearByDriver = APILiveData<List<NearByDriver>>()
    var vehicleData = APILiveData<VehicleData>()
    val trackTrip = APILiveData<PlaceOrder>()
    val generateAccesstoken = APILiveData<AccessData>()

    fun nearByDriverListing(parameter: Parameter) {

        tripRepository.nearByDrivers(parameter).subscribe(withLiveData(nearByDriver))
    }

    fun generateAccesstoken(parameter: Parameter) {
        tripRepository.generateAccesstoken(parameter).subscribe(withLiveData(generateAccesstoken))
    }

    fun vehicleListing() {
        tripRepository.vehicleList().subscribe(withLiveData(vehicleData))
    }

    fun trackTripApiCall() {
        val parameter = Parameter()
        parameter.user_type = Common.CUSTOMER
        tripRepository.trackTrip(parameter).subscribe(withLiveData(trackTrip))
    }
}