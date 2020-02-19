package com.victoria.driver.data.repository

import com.victoria.driver.data.AccessData
import com.victoria.driver.data.pojo.*
import com.victoria.driver.ui.model.*
import io.reactivex.Single
import okhttp3.RequestBody
import java.util.*

interface UserRepository {

    fun signUp(parameter: Parameter): Single<DataWrapper<User>>

    fun resendOtp(parameter: Parameter): Single<DataWrapper<User>>

    fun verifyOtp(parameter: Parameter): Single<DataWrapper<User>>

    fun changePassword(parameter: Parameter): Single<DataWrapper<User>>

    fun login(parameter: Parameter): Single<DataWrapper<User>>

    fun forgotPassword(parameter: Parameter): Single<DataWrapper<User>>

    fun getdriverreport(parameter: Parameter): Single<DataWrapper<EarningsMonth>>

    fun wallethistory(parameter: Parameter): Single<DataWrapper<List<PaymentHistory>>>

    fun getdriverreportYear(parameter: Parameter): Single<DataWrapper<EarningsYear>>

    fun editProfile(parameter: Parameter): Single<DataWrapper<User>>

    fun logOut(): Single<DataWrapper<User>>

    fun vehicleList(): Single<DataWrapper<ArrayList<VehicleList>>>

    fun profileImageUpload(path: String): Single<DataWrapper<ImageData>>

    fun documentImageUpload(hashMap: HashMap<String, RequestBody>, path: String, pathRegistration: String,
                            pathCarFront: String, pathCarBack: String): Single<DataWrapper<CarDocuments>>

    fun addDocument(parameter: Parameter): Single<DataWrapper<User>>

    fun addVehicle(parameter: Parameter): Single<DataWrapper<User>>

    fun addBank(parameter: Parameter): Single<DataWrapper<User>>

    fun updateDocument(parameter: HashMap<String, String>): Single<DataWrapper<CarDocuments>>

    fun changeStatus(parameter: Parameter): Single<DataWrapper<User>>

    fun updateBankData(parameter: Parameter): Single<DataWrapper<User>>

    fun updateVehicleData(parameter: Parameter): Single<DataWrapper<User>>

    fun updateDocument(parameter: Parameter): Single<DataWrapper<User>>

    fun contactUsImageUpload(parameter: ArrayList<String>): Single<DataWrapper<User>>

    fun contactUs(parameter: Parameter): Single<DataWrapper<User>>

    fun trackTrip(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun trackTripDetail(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun updateLocation(parameter: Parameter): Single<DataWrapper<User>>

    fun acceptTripRequest(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun declineTripRequest(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun tripArrived(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun startRide(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun changeDropOff(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun completeRide(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun rateAndReview(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun confirmReciept(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun pastTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>>

    fun upcomingTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>>

    fun userDetail(parameter: Parameter): Single<DataWrapper<User>>

    fun notificationList(parameter: Parameter): Single<DataWrapper<List<NotificationData>>>

    fun notificationCount(parameter: Parameter): Single<DataWrapper<NotificationCount>>

    fun cancelRideReasons(parameter: Parameter): Single<DataWrapper<List<CancelReason>>>

    fun changelanguage(parameter: Parameter): Single<DataWrapper<Unit>>

    fun cancelRide(parameter: Parameter): Single<DataWrapper<Unit>>

    fun rateReviewList(parameter: Parameter): Single<DataWrapper<List<RateReviewData>>>

    fun generateAccesstoken(parameter: Parameter): Single<DataWrapper<AccessData>>

}
