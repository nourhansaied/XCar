package com.victoria.driver.ui.viewmodel


import com.victoria.driver.core.Common
import com.victoria.driver.data.AccessData
import com.victoria.driver.data.pojo.*
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.Parameter
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor(private val userRepository: UserRepository)
    : BaseViewModel() {

    val logOut = APILiveData<User>()
    val trackTrip = APILiveData<PlaceOrder>()
    val acceptOrReject = APILiveData<PlaceOrder>()
    val arrived = APILiveData<PlaceOrder>()
    val rideStart = APILiveData<PlaceOrder>()
    val changeDropOff = APILiveData<PlaceOrder>()
    val completeRide = APILiveData<PlaceOrder>()
    val rateAndReview = APILiveData<PlaceOrder>()
    val confirmReciept = APILiveData<PlaceOrder>()
    val pastRideList = APILiveData<List<PastRide>>()
    val userData = APILiveData<User>()
    val notification = APILiveData<List<NotificationData>>()
    val notificationCount = APILiveData<NotificationCount>()
    val trackTripApiTwoCall = APILiveData<PlaceOrder>()
    val rateReviewListLiveData = APILiveData<List<RateReviewData>>()
    val generateAccesstoken = APILiveData<AccessData>()

    fun notificationListApi(parameter: Parameter) {
        userRepository.notificationList(parameter).subscribe(withLiveData(notification))
    }

    fun notificationCountApi(parameter: Parameter) {
        userRepository.notificationCount(parameter).subscribe(withLiveData(notificationCount))
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
        parameter.user_type = Common.DRIVER

        userRepository.trackTrip(parameter).subscribe(withLiveData(trackTrip))
    }

    fun trackTripApiTwoCall(parameter: Parameter) {
        userRepository.trackTripDetail(parameter).subscribe(withLiveData(trackTripApiTwoCall))
    }

    fun acceptOrRejectApiCall(parameter: Parameter, isAccept: Boolean) {
        if (isAccept)
            userRepository.acceptTripRequest(parameter).subscribe(withLiveData(acceptOrReject))
        else
            userRepository.declineTripRequest(parameter).subscribe(withLiveData(acceptOrReject))
    }

    fun arrivedApiCall(parameter: Parameter) {
        userRepository.tripArrived(parameter).subscribe(withLiveData(arrived))
    }

    fun rideStartApiCall(parameter: Parameter) {
        userRepository.startRide(parameter).subscribe(withLiveData(rideStart))
    }

    fun changeDropOff(parameter: Parameter) {
        userRepository.changeDropOff(parameter).subscribe(withLiveData(changeDropOff))
    }

    fun completeRide(parameter: Parameter) {
        userRepository.completeRide(parameter).subscribe(withLiveData(completeRide))
    }


    fun rateAndReviewApi(parameter: Parameter) {
        userRepository.rateAndReview(parameter).subscribe(withLiveData(rateAndReview))
    }


    fun confirmRecieptApi(parameter: Parameter) {
        userRepository.confirmReciept(parameter).subscribe(withLiveData(confirmReciept))
    }

    fun getRateReviewList(parameter: Parameter) {
        userRepository.rateReviewList(parameter).subscribe(withLiveData(rateReviewListLiveData))
    }

    fun generateAccesstoken(parameter: Parameter) {
        userRepository.generateAccesstoken(parameter).subscribe(withLiveData(generateAccesstoken))
    }
}

