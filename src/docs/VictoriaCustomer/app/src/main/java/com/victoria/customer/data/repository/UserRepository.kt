package com.victoria.customer.data.repository;

import com.victoria.customer.data.pojo.*
import com.victoria.customer.model.FavouritesAddressParam
import com.victoria.customer.model.ImageData
import com.victoria.customer.model.NearByDriver
import com.victoria.customer.model.Parameter
import io.reactivex.Single

interface UserRepository {

    fun signUp(parameter: Parameter): Single<DataWrapper<User>>

    fun resendOtp(parameter: Parameter): Single<DataWrapper<User>>

    fun verifyOtp(parameter: Parameter): Single<DataWrapper<User>>

    fun changePassword(parameter: Parameter): Single<DataWrapper<User>>

    fun login(parameter: Parameter): Single<DataWrapper<User>>

    fun forgotPassword(parameter: Parameter): Single<DataWrapper<User>>
    fun changelanguage(parameter: Parameter): Single<DataWrapper<Unit>>

    fun editProfile(parameter: Parameter): Single<DataWrapper<User>>

    fun logOut(): Single<DataWrapper<User>>

    fun profileImageUpload(path: String): Single<DataWrapper<ImageData>>

    fun contactUsImageUpload(parameter: ArrayList<String>): Single<DataWrapper<User>>

    fun contactUs(parameter: Parameter): Single<DataWrapper<User>>

    fun nearByDrivers(parameter: Parameter): Single<DataWrapper<List<NearByDriver>>>

    fun addFavouriteAddress(params: FavouritesAddressParam): Single<DataWrapper<String>>

    fun favouritesAddressList(): Single<DataWrapper<List<FavouriteAddress>>>

    fun removeFavouriteAddress(params: FavouritesAddressParam): Single<DataWrapper<String>>

    fun paymentDone(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun rateAndReview(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun pastTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>>

    fun upcomingTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>>

    fun userDetail(parameter: Parameter): Single<DataWrapper<User>>

    fun notificationList(parameter: Parameter): Single<DataWrapper<List<NotificationData>>>

    fun notificationCount(parameter: Parameter): Single<DataWrapper<NotificationCount>>

    fun rateReviewList(parameter: Parameter): Single<DataWrapper<List<RateReviewData>>>

}
