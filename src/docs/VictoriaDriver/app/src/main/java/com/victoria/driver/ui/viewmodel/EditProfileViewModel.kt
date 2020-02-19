package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.pojo.EarningsMonth
import com.victoria.driver.data.pojo.EarningsYear
import com.victoria.driver.data.pojo.PaymentHistory
import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.ImageData
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
        BaseViewModel() {

    var editProfile = APILiveData<User>()
    var uploadImage = APILiveData<ImageData>()
    val userData = APILiveData<User>()
    val getdriverreport = APILiveData<EarningsMonth>()
    val getdriverreportYearApi = APILiveData<EarningsYear>()
    val paymentHistoryList = APILiveData<List<PaymentHistory>>()

    fun paymentHistoryListApi(parameter: Parameter) {
        userRepository.wallethistory(parameter).subscribe(withLiveData(paymentHistoryList))
    }

    fun getdriverreportApi(parameter: Parameter) {
        userRepository.getdriverreport(parameter).subscribe(withLiveData(getdriverreport))
    }

    fun getdriverreportYearApi(parameter: Parameter) {
        userRepository.getdriverreportYear(parameter).subscribe(withLiveData(getdriverreportYearApi))
    }


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