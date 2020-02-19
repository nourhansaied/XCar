package com.victoria.driver.ui.viewmodel

import com.victoria.driver.data.pojo.PigListing
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {
    val homeLiveData = APILiveData<PigListing>()

    lateinit var map: HashMap<String, String>

    fun getPigListing(param: String) {
        map = HashMap()
        map["page"] = param


    }
}