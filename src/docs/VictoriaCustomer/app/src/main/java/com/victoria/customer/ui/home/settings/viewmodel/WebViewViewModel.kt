package com.victoria.customer.ui.home.settings.viewmodel

import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class WebViewViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {


}