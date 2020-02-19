package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class SignInViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val login = APILiveData<User>()

    fun loginApiCall(parameter: Parameter) {
        userRepository.login(parameter).subscribe(withLiveData(login))
    }
}