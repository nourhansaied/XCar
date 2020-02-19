package com.victoria.driver.ui.viewmodel


import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.PlaceOrder
import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val changeStatus = APILiveData<User>()
    val trackTrip = APILiveData<PlaceOrder>()

    fun changeStatusApiCall(parameter: Parameter) {
        userRepository.changeStatus(parameter).subscribe(withLiveData(changeStatus))
    }

    fun trackTripApiCall() {
        val parameter = Parameter()
        parameter.user_type = Common.DRIVER
        userRepository.trackTrip(parameter).subscribe(withLiveData(trackTrip))
    }

}
