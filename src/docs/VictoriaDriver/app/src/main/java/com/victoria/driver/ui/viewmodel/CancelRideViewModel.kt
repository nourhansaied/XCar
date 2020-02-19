package com.victoria.driver.ui.viewmodel

import com.victoria.driver.core.Common
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.CancelReason
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class CancelRideViewModel @Inject constructor(private val tripRepository: UserRepository) : BaseViewModel() {

    var cancelRideReason = APILiveData<List<CancelReason>>()
    var cancelRide = APILiveData<Unit>()

    fun cancelRideReasons() {
        var parameter = Parameter()
        parameter.user_type = Common.DRIVER
        tripRepository.cancelRideReasons(parameter).subscribe(withLiveData(cancelRideReason))
    }

    fun cancelRide(parameter: Parameter) {
        parameter.user_type = Common.DRIVER
        tripRepository.cancelRide(parameter).subscribe(withLiveData(cancelRide))
    }
}