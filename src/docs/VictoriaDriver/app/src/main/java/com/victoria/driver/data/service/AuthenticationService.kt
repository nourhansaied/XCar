package com.victoria.driver.data.service

import com.victoria.customer.data.URLFactory
import com.victoria.driver.data.AccessData
import com.victoria.driver.data.pojo.*
import com.victoria.driver.ui.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*

interface AuthenticationService {

    @POST(URLFactory.Method.SIGNUP)
    fun signUp(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.RESEND_OTP)
    fun resendOtp(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.VERIFY_OTP)
    fun verifyOtp(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.CHANGE_PASSWORD)
    fun changePassword(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.LOGIN)
    fun login(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.FORGOT_PASSWORD)
    fun forgotPassword(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.EDIT_PROFILE)
    fun editProfile(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.GETDRIVERREPORT)
    fun getdriverreport(@Body parameter: Parameter): Single<ResponseBody<EarningsMonth>>

    @POST(URLFactory.Method.WALLETHISTORY)
    fun wallethistory(@Body parameter: Parameter): Single<ResponseBody<List<PaymentHistory>>>

    @POST(URLFactory.Method.GETDRIVERREPORTYEAR)
    fun getdriverreportYear(@Body parameter: Parameter): Single<ResponseBody<EarningsYear>>

    @POST(URLFactory.Method.ADD_VEHICLE)
    fun addVehicle(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.ADD_BANK)
    fun addBank(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.UPDATE_DOCUMENT)
    fun updateDocument(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.UPDATE_VEHICLE_DATA)
    fun updateVehicleData(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.UPDATE_BANK)
    fun updateBankData(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.CHANGE_STATUS)
    fun changeStatus(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.ADD_DOCUMENT)
    fun addDocument(@Body parameter: Parameter): Single<ResponseBody<User>>

    @GET(URLFactory.Method.LOGOUT)
    fun logOut(): Single<ResponseBody<User>>

    @GET(URLFactory.Method.VEHICLE_LIST)
    fun vehicleList(): Single<ResponseBody<ArrayList<VehicleList>>>

    @Multipart
    @POST(URLFactory.Method.PROFILE_IMAGE_UPLOAD)
    fun profileImageUpload(@Part parameter: MultipartBody.Part): Single<ResponseBody<ImageData>>

    @Multipart
    @POST(URLFactory.Method.DOC_IMAGE_UPLOAD)
    fun documentImageUpload(@PartMap hashMap: HashMap<String, RequestBody>,
                            @Part pathLicence: MultipartBody.Part, @Part pathRegistration: MultipartBody.Part,
                            @Part pathCarFront: MultipartBody.Part,
                            @Part pathCarBack: MultipartBody.Part): Single<ResponseBody<CarDocuments>>


    @Multipart
    @POST(URLFactory.Method.DOC_IMAGE_UPLOAD)
    fun documentImageUploadEdit(@Part pathCarBack: List<MultipartBody.Part>): Single<ResponseBody<CarDocuments>>

    @Multipart
    @POST(URLFactory.Method.CONTACT_US_IMAGE_UPLOAD)
    fun contactUsImageUpload(@Part pathCarBack: List<MultipartBody.Part>): Single<ResponseBody<User>>

    @POST(URLFactory.Method.CONTACT_US)
    fun contactUs(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.UPDATELATLNG)
    fun updateLatLng(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.TRACK_TRIP)
    fun trackTrip(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.ACCEPT_TRIP_REQUEST)
    fun acceptTripRequest(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.DECLINE_TRIP_REQUEST)
    fun declineTripRequest(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.TRIP_ARRIVED)
    fun tripArrived(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.START_RIDE)
    fun startRide(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.CHANGEDROPOFF)
    fun changeDropOff(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.COMPLETERIDE)
    fun completeRide(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>


    @POST(URLFactory.Method.RATEANDREVIEW)
    fun rateAndReview(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.CONFIRMRECIEPT)
    fun confirmReciept(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.PASTTRIP)
    fun pastTrip(@Body parameter: Parameter): Single<ResponseBody<List<PastRide>>>

    @POST(URLFactory.Method.UPCOMINGTRIP)
    fun upcomingTrip(@Body parameter: Parameter): Single<ResponseBody<List<PastRide>>>

    @POST(URLFactory.Method.USERDETAIL)
    fun userDetail(@Body parameter: Parameter): Single<ResponseBody<User>>

    @POST(URLFactory.Method.NOTIFICATION_LIST)
    fun notificationList(@Body parameter: Parameter): Single<ResponseBody<List<NotificationData>>>

    @POST(URLFactory.Method.NOTIFICATION_COUNT)
    fun notificationCount(@Body parameter: Parameter): Single<ResponseBody<NotificationCount>>

    @POST(URLFactory.Method.TRIP_DETAIL)
    fun trackTripDetail(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.CANCEL_RIDE_REASON)
    fun cancelRideReasons(@Body parameter: Parameter): Single<ResponseBody<List<CancelReason>>>

    @POST(URLFactory.Method.CHANGELANGUAGE)
    fun changelanguage(@Body parameter: Parameter): Single<ResponseBody<Unit>>

    @POST(URLFactory.Method.CANCEL_TRIP)
    fun cancelRide(@Body parameter: Parameter): Single<ResponseBody<Unit>>

    @POST(URLFactory.Method.RATE_REVIEW_LIST)
    fun reteReviewList(@Body parameter: Parameter): Single<ResponseBody<List<RateReviewData>>>

    @POST(URLFactory.Method.GENERATE_ACCESSTOKEN)
    fun generateAccesstoken(@Body parameter: Parameter): Single<ResponseBody<AccessData>>

}