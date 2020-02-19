package com.victoria.customer.data.datasource;

import com.victoria.customer.core.Common
import com.victoria.customer.core.Common.PROFILE_UPLOAD_KEY
import com.victoria.customer.data.pojo.*
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.data.service.AuthenticationService
import com.victoria.customer.model.FavouritesAddressParam
import com.victoria.customer.model.ImageData
import com.victoria.customer.model.NearByDriver
import com.victoria.customer.model.Parameter
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserLiveDataSource @Inject constructor(private val authenticationService: AuthenticationService) : BaseDataSource(), UserRepository {

    override fun notificationCount(parameter: Parameter): Single<DataWrapper<NotificationCount>> {
        return execute(authenticationService.notificationCount(parameter))
    }

    override fun notificationList(parameter: Parameter): Single<DataWrapper<List<NotificationData>>> {
        return execute(authenticationService.notificationList(parameter))
    }

    override fun pastTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>> {
        return execute(authenticationService.pastTrip(parameter))
    }

    override fun upcomingTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>> {
        return execute(authenticationService.upcomingTrip(parameter))
    }

    override fun rateAndReview(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.rateAndReview(parameter))
    }

    override fun paymentDone(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.paymentDone(parameter))
    }

    override fun nearByDrivers(parameter: Parameter): Single<DataWrapper<List<NearByDriver>>> {
        return execute(authenticationService.nearByDrivers(parameter))
    }

    override fun resendOtp(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.resendOtp(parameter))
    }

    override fun verifyOtp(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.verifyOtp(parameter))

    }

    override fun changePassword(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.changePassword(parameter))

    }

    override fun login(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.login(parameter))

    }

    override fun forgotPassword(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.forgotPassword(parameter))

    }

    override fun changelanguage(parameter: Parameter): Single<DataWrapper<Unit>> {
        return execute(authenticationService.changelanguage(parameter))
    }


    override fun editProfile(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.editProfile(parameter))

    }

    override fun logOut(): Single<DataWrapper<User>> {
        return execute(authenticationService.logOut())

    }

    override fun profileImageUpload(parameter: String): Single<DataWrapper<ImageData>> {
        return execute(authenticationService.profileImageUpload(this.singleImagePart(PROFILE_UPLOAD_KEY, parameter)!!))
    }

    override fun signUp(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.signUp(parameter))
    }

    override fun contactUs(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.contactUs(parameter))

    }

    override fun contactUsImageUpload(parameter: ArrayList<String>): Single<DataWrapper<User>> {
        return execute(authenticationService.contactUsImageUpload(arrayImagePart(Common.CONTACT_US_IMAGE_UPLOAD, parameter)))
    }

    override fun addFavouriteAddress(params: FavouritesAddressParam): Single<DataWrapper<String>> {
        return execute(authenticationService.addFavouriteAddress(params))
    }

    override fun favouritesAddressList(): Single<DataWrapper<List<FavouriteAddress>>> {
        return execute(authenticationService.favouritesAddressList())
    }

    override fun removeFavouriteAddress(params: FavouritesAddressParam): Single<DataWrapper<String>> {
        return execute(authenticationService.removeFavouriteAddress(params))
    }


    override fun userDetail(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.userDetail(parameter))
    }
    override fun rateReviewList(parameter: Parameter): Single<DataWrapper<List<RateReviewData>>> {
        return execute(authenticationService.reteReviewList(parameter))
    }
}
