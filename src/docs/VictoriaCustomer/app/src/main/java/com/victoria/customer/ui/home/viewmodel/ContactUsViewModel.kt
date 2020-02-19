package com.victoria.customer.ui.home.viewmodel


import com.victoria.customer.data.pojo.User
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
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