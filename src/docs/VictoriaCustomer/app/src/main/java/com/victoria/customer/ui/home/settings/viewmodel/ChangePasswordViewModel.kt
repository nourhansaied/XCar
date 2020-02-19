package com.victoria.customer.ui.home.settings.viewmodel

import com.victoria.customer.data.pojo.User
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
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