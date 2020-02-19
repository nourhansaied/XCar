package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.BaseViewModel
import javax.inject.Inject

class ReferenceViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

}