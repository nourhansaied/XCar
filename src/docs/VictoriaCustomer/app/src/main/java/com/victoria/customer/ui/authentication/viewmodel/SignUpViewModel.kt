package com.victoria.customer.ui.authentication.viewmodel

import com.victoria.customer.data.pojo.User
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {
    val signUpLiveData = APILiveData<User>()

    fun signUpUser(parameter: Parameter) {
        userRepository.signUp(parameter).subscribe(withLiveData(signUpLiveData))
    }

}