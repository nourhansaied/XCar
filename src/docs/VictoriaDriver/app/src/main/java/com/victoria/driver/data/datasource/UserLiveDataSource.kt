package com.victoria.driver.data.datasource

import com.victoria.driver.core.Common
import com.victoria.driver.data.AccessData
import com.victoria.driver.data.pojo.*
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.data.service.AuthenticationService
import com.victoria.driver.ui.model.*
import io.reactivex.Single
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLiveDataSource @Inject constructor(private val authenticationService
                                             : AuthenticationService) : BaseDataSource(), UserRepository {

    override fun changelanguage(parameter: Parameter): Single<DataWrapper<Unit>> {
        return execute(authenticationService.changelanguage(parameter))
    }

    override fun trackTripDetail(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.trackTripDetail(parameter))
    }

    override fun notificationCount(parameter: Parameter): Single<DataWrapper<NotificationCount>> {
        return execute(authenticationService.notificationCount(parameter))
    }

    override fun notificationList(parameter: Parameter): Single<DataWrapper<List<NotificationData>>> {
        return execute(authenticationService.notificationList(parameter))
    }

    override fun userDetail(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.userDetail(parameter))
    }

    override fun pastTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>> {
        return execute(authenticationService.pastTrip(parameter))
    }

    override fun upcomingTrip(parameter: Parameter): Single<DataWrapper<List<PastRide>>> {
        return execute(authenticationService.upcomingTrip(parameter))
    }

    override fun completeRide(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.completeRide(parameter))
    }

    override fun changeDropOff(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.changeDropOff(parameter))
    }

    override fun startRide(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.startRide(parameter))
    }

    override fun tripArrived(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.tripArrived(parameter))
    }

    override fun acceptTripRequest(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.acceptTripRequest(parameter))
    }

    override fun declineTripRequest(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.declineTripRequest(parameter))
    }

    override fun trackTrip(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.trackTrip(parameter))
    }

    override fun updateLocation(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.updateLatLng(parameter))
    }

    override fun contactUs(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.contactUs(parameter))

    }

    override fun contactUsImageUpload(parameter: ArrayList<String>): Single<DataWrapper<User>> {
        return execute(authenticationService.contactUsImageUpload(arrayImagePart(Common.CONTACT_US_IMAGE_UPLOAD, parameter)))
    }

    override fun updateDocument(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.updateDocument(parameter))
    }

    override fun changeStatus(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.changeStatus(parameter))
    }

    override fun updateBankData(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.updateBankData(parameter))
    }

    override fun updateVehicleData(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.updateVehicleData(parameter))
    }

    override fun vehicleList(): Single<DataWrapper<ArrayList<VehicleList>>> {
        return execute(authenticationService.vehicleList())
    }

    override fun documentImageUpload(hashMap: HashMap<String, RequestBody>, path: String, pathRegistration: String, pathCarFront: String, pathCarBack: String): Single<DataWrapper<CarDocuments>> {
        return execute(authenticationService.documentImageUpload(hashMap, this.singleImagePart(Common.DRIVING_LICENCE, path)!!,
                this.singleImagePart(com.victoria.driver.core.Common.REGISTRATION_IMAGE, pathRegistration)!!,
                this.singleImagePart(com.victoria.driver.core.Common.CAR_FRONT_IMAGE, pathCarFront)!!,
                this.singleImagePart(com.victoria.driver.core.Common.CAR_BACK_IMAGE, pathCarBack)!!))
    }

    override fun addDocument(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.addDocument(parameter))
    }

    override fun addVehicle(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.addVehicle(parameter))
    }

    override fun addBank(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.addBank(parameter))
    }

    override fun updateDocument(parameter: HashMap<String, String>): Single<DataWrapper<CarDocuments>> {
        return execute(authenticationService.documentImageUploadEdit(this.arrayImagePart(parameter)))
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

    override fun getdriverreport(parameter: Parameter): Single<DataWrapper<EarningsMonth>> {
        return execute(authenticationService.getdriverreport(parameter))
    }

    override fun wallethistory(parameter: Parameter): Single<DataWrapper<List<PaymentHistory>>> {
        return execute(authenticationService.wallethistory(parameter))
    }

    override fun getdriverreportYear(parameter: Parameter): Single<DataWrapper<EarningsYear>> {
        return execute(authenticationService.getdriverreportYear(parameter))
    }


    override fun editProfile(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.editProfile(parameter))

    }

    override fun logOut(): Single<DataWrapper<User>> {
        return execute(authenticationService.logOut())

    }

    override fun profileImageUpload(path: String): Single<DataWrapper<ImageData>> {
        return execute(authenticationService.profileImageUpload(this.singleImagePart(com.victoria.driver.core.Common.PROFILE_UPLOAD_KEY, path)!!))

    }

    override fun signUp(parameter: Parameter): Single<DataWrapper<User>> {
        return execute(authenticationService.signUp(parameter))
    }

    override fun rateAndReview(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.rateAndReview(parameter))
    }

    override fun confirmReciept(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(authenticationService.confirmReciept(parameter))
    }

    override fun cancelRide(parameter: Parameter): Single<DataWrapper<Unit>> {
        return execute(authenticationService.cancelRide(parameter))
    }

    override fun cancelRideReasons(parameter: Parameter): Single<DataWrapper<List<CancelReason>>> {
        return execute(authenticationService.cancelRideReasons(parameter))
    }

    override fun rateReviewList(parameter: Parameter): Single<DataWrapper<List<RateReviewData>>> {
        return execute(authenticationService.reteReviewList(parameter))
    }

    override fun generateAccesstoken(parameter: Parameter): Single<DataWrapper<AccessData>> {
        return execute(authenticationService.generateAccesstoken(parameter))
    }
}
