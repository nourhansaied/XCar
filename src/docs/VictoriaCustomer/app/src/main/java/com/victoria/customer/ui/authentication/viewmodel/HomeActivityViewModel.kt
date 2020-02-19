package com.victoria.customer.ui.authentication.viewmodel

import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.*
import com.victoria.customer.data.repository.TripRepository
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor(private val userRepository: UserRepository, private val tripRepository: TripRepository)
    : BaseViewModel() {

    val logOut = APILiveData<User>()
    val trackTrip = APILiveData<PlaceOrder>()
    val paymentConfirmation = APILiveData<PlaceOrder>()
    val rateAndReview = APILiveData<PlaceOrder>()
    val pastRideList = APILiveData<List<PastRide>>()
    val userData = APILiveData<User>()
    val notification= APILiveData<List<NotificationData>>()
    val notificationCount= APILiveData<NotificationCount>()
    val getsdktoken= APILiveData<PayfortAccess>()
    val rateReviewListLiveData=APILiveData<List<RateReviewData>>()

    fun getsdktoken(parameter: Parameter) {
        tripRepository.getsdktoken(parameter).subscribe(withLiveData(getsdktoken))
    }

    fun userDetailApiCall(parameter: Parameter) {
        userRepository.userDetail(parameter).subscribe(withLiveData(userData))
    }

    fun upcomingRide(parameter: Parameter) {
        userRepository.upcomingTrip(parameter).subscribe(withLiveData(pastRideList))
    }

    fun pastRideApiCall(parameter: Parameter) {
        userRepository.pastTrip(parameter).subscribe(withLiveData(pastRideList))
    }
    fun logOutApiCall() {
        userRepository.logOut().subscribe(withLiveData(logOut))
    }

    fun trackTripApiCall() {
        val parameter = Parameter()
        parameter.user_type = Common.CUSTOMER
        tripRepository.trackTrip(parameter).subscribe(withLiveData(trackTrip))
    }

    fun paymentConfirmation(parameter: Parameter) {
        userRepository.paymentDone(parameter).subscribe(withLiveData(paymentConfirmation))
    }

    fun rateAndReviewApi(parameter: Parameter) {
        userRepository.rateAndReview(parameter).subscribe(withLiveData(rateAndReview))
    }

    fun notificationListApi(parameter: Parameter) {
        userRepository.notificationList(parameter).subscribe(withLiveData(notification))
    }

    fun notificationCountApi(parameter: Parameter) {
        userRepository.notificationCount(parameter).subscribe(withLiveData(notificationCount))
    }

    fun getRateReviewList(parameter: Parameter){
        userRepository.rateReviewList(parameter).subscribe(withLiveData(rateReviewListLiveData))
    }
}
