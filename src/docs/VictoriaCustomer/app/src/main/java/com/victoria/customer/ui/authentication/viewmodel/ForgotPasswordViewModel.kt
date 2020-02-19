package com.victoria.customer.ui.authentication.viewmodel

import com.victoria.customer.data.pojo.User
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val forgotPassword = APILiveData<User>()

    fun forgotPasswordApiCall(parameter: Parameter) {
        userRepository.forgotPassword(parameter).subscribe(withLiveData(forgotPassword))
    }
}