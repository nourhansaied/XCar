package com.victoria.customer.ui.authentication.viewmodel

import com.victoria.customer.data.pojo.User
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val login = APILiveData<User>()
    fun loginApiCall(parameter: Parameter) {
        userRepository.login(parameter).subscribe(withLiveData(login))
    }
}