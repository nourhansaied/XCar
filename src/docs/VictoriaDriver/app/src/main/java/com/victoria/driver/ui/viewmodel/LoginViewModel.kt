package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val loginLiveData = APILiveData<User>()

}