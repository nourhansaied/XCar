package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    var changePassword = APILiveData<User>()
    var changelanguage = APILiveData<Unit>()

    fun changePasswordApiCall(parameter: Parameter) {
        userRepository.changePassword(parameter).subscribe(withLiveData(changePassword))
    }

    fun changelanguage(parameter: Parameter) {
        userRepository.changelanguage(parameter).subscribe(withLiveData(changelanguage))
    }
}