package com.victoria.customer.data.service;

import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.pojo.*
import com.victoria.customer.model.FavouritesAddressParam
import com.victoria.customer.model.ImageData
import com.victoria.customer.model.NearByDriver
import com.victoria.customer.model.Parameter
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

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

    @POST(URLFactory.Method.CHANGELANGUAGE)
    fun changelanguage(@Body parameter: Parameter): Single<ResponseBody<Unit>>

    @POST(URLFactory.Method.EDIT_PROFILE)
    fun editProfile(@Body parameter: Parameter): Single<ResponseBody<User>>

    @GET(URLFactory.Method.LOGOUT)
    fun logOut(): Single<ResponseBody<User>>

    @POST(URLFactory.Method.NEAR_BY_DRIVERS)
    fun nearByDrivers(@Body parameter: Parameter): Single<ResponseBody<List<NearByDriver>>>

    @Multipart
    @POST(URLFactory.Method.PROFILE_IMAGE_UPLOAD)
    fun profileImageUpload(@Part parameter: MultipartBody.Part): Single<ResponseBody<ImageData>>

    @Multipart
    @POST(URLFactory.Method.CONTACT_US_IMAGE_UPLOAD)
    fun contactUsImageUpload(@Part pathCarBack: List<MultipartBody.Part>): Single<ResponseBody<User>>

    @POST(URLFactory.Method.CONTACT_US)
    fun contactUs(@Body parameter: Parameter): Single<ResponseBody<User>>


    @POST(URLFactory.Method.ADD_FAVOURITE_ADDRESS)
    fun addFavouriteAddress(@Body parameter: FavouritesAddressParam): Single<ResponseBody<String>>

    @GET(URLFactory.Method.FAVOURITE_ADDRESS_LIST)
    fun favouritesAddressList(): Single<ResponseBody<List<FavouriteAddress>>>

    @POST(URLFactory.Method.REMOVE_FAVOURITE_ADDRESS)
    fun removeFavouriteAddress(@Body parameter: FavouritesAddressParam): Single<ResponseBody<String>>

    @POST(URLFactory.Method.PAYMENTDONE)
    fun paymentDone(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.RATEANDREVIEW)
    fun rateAndReview(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

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

    @POST(URLFactory.Method.RATE_REVIEW_LIST)
    fun reteReviewList(@Body parameter: Parameter): Single<ResponseBody<List<RateReviewData>>>

}