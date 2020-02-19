package com.victoria.customer.ui.authentication.viewmodel

import com.victoria.customer.data.pojo.User
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
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
