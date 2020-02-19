package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class OtpVerifyViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val otpVerify = APILiveData<User>()
    val otpResend = APILiveData<User>()

    fun otpVerifyApiCall(parameter: Parameter) {
        userRepository.verifyOtp(parameter).subscribe(withLiveData(otpVerify))
    }

    fun resendOtpApiCall(parameter: Parameter) {
        userRepository.resendOtp(parameter).subscribe(withLiveData(otpResend))
    }
}
