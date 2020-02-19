package com.victoria.customer.ui.home.viewmodel

import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {


}