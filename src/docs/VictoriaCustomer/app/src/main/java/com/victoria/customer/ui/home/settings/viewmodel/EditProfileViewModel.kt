package com.victoria.customer.ui.home.settings.viewmodel

import com.victoria.customer.data.pojo.User
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.ImageData
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
        BaseViewModel() {

    var editProfile = APILiveData<User>()
    var uploadImage = APILiveData<ImageData>()
    val userData = APILiveData<User>()

    fun uploadImage(path: String) {
        userRepository.profileImageUpload(path).subscribe(withLiveData(uploadImage))
    }

    fun editProfileApiCall(parameter: Parameter) {
        userRepository.editProfile(parameter).subscribe(withLiveData(editProfile))
    }

    fun userDetailApiCall(parameter: Parameter) {
        userRepository.userDetail(parameter).subscribe(withLiveData(userData))
    }

}