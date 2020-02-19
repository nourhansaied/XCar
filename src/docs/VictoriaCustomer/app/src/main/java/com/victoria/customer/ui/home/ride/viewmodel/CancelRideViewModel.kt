package com.victoria.customer.ui.home.ride.viewmodel

import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.CancelReason
import com.victoria.customer.data.repository.TripRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class CancelRideViewModel @Inject constructor(private val tripRepository: TripRepository) : BaseViewModel() {

    var cancelRideReason = APILiveData<List<CancelReason>>()
    var cancelRide = APILiveData<Unit>()

    fun cancelRideReasons() {
        var parameter = Parameter()
        parameter.user_type = Common.CUSTOMER
        tripRepository.cancelRideReasons(parameter).subscribe(withLiveData(cancelRideReason))
    }

    fun cancelRide(parameter: Parameter) {
        parameter.user_type = Common.CUSTOMER
        tripRepository.cancelRide(parameter).subscribe(withLiveData(cancelRide))
    }
}