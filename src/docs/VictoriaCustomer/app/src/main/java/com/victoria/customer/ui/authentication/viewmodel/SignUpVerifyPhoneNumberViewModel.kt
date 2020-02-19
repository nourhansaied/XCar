package com.victoria.customer.ui.authentication.viewmodel

import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class SignUpVerifyPhoneNumberViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {


}