package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class ContactUsViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val contactUsPhotoUpload = APILiveData<User>()
    val contactUs = APILiveData<User>()


    fun contactUsPhotoUploadApiCall(parameter: ArrayList<String>) {
        userRepository.contactUsImageUpload(parameter).subscribe(withLiveData(contactUsPhotoUpload))
    }

    fun contactUsApiCall(parameter: Parameter) {
        userRepository.contactUs(parameter).subscribe(withLiveData(contactUs))
    }
}