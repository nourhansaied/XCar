package com.victoria.customer.data;

import okhttp3.HttpUrl

/**
 * Created by hlink21 on 11/5/17.
 */

object URLFactory {

    // server details
    //http://3.122.9.220:5180/v1/

    //clear text ma url update karvu
    //
    //
    //
    private const val IS_LOCAL = false
    private const val SCHEME = "http"
    private val HOST = if (IS_LOCAL) "192.168.1.159" else "3.122.9.220"
    private val API_PATH = if (IS_LOCAL) "v1/" else "v1/"
    private const val PORT = 5180


    fun provideHttpUrl(): HttpUrl {
        return HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
                .addPathSegments(API_PATH)
                .port(PORT)
                .build()
    }


    const val ABOUT_US = "http://3.122.9.220/victoria/content/aboutus/english"
    const val FAQ = "http://3.122.9.220/victoria/content/faqslist/english/driver"
    const val PRIVACY_POLICY = "http://3.122.9.220/victoria/content/privacypolicy/english/driver"
    const val TANDC = "http://3.122.9.220/victoria/content/termscondition/english/driver"


    // API Methods
    object Method {
        const val SIGNUP = "driver/signup"
        const val RESEND_OTP = "driver/resend_otp"
        const val VERIFY_OTP = "driver/verify_otp"
        const val LOGIN = "driver/login"
        const val EDIT_PROFILE = "driver/edit_profile"
        const val GETDRIVERREPORT = "driver/datewiseearnings"
        const val WALLETHISTORY = "driver/transactionlist"

        const val GETDRIVERREPORTYEAR = "driver/yearlyearnings"
        const val CHANGE_PASSWORD = "driver/changepassword"
        const val FORGOT_PASSWORD = "driver/forgotpassword"
        const val LOGOUT = "driver/logout"
        const val PROFILE_IMAGE_UPLOAD = "driver/profile_image_upload"
        const val DOC_IMAGE_UPLOAD = "driver/document_image_upload"
        const val VEHICLE_LIST = "driver/vehiclelist" //GET
        const val ADD_VEHICLE = "driver/add_vehicle"
        const val ADD_DOCUMENT = "driver/add_document"
        const val ADD_BANK = "driver/add_bank"
        const val UPDATE_DOCUMENT = "driver/update_document"
        const val UPDATE_VEHICLE_DATA = "driver/update_vehicle"
        const val UPDATE_BANK = "driver/update_bank"
        const val CHANGE_STATUS = "driver/driverchangeservice"
        const val CONTACT_US_IMAGE_UPLOAD = "trip/contactus_image_upload"
        const val CONTACT_US = "trip/contact_us"
        const val UPDATELATLNG = "driver/updatelatlong"
        const val USERDETAIL = "trip/user_details"

        /**=================Trip ================*/
        const val TRACK_TRIP = "trip/tracktrip"
        const val ACCEPT_TRIP_REQUEST = "trip/accept_triprequest"
        const val DECLINE_TRIP_REQUEST = "trip/decline_triprequest"
        const val TRIP_ARRIVED = "trip/arrived_trip"
        const val START_RIDE = "trip/start_trip"
        const val CHANGEDROPOFF = "trip/change_dropoff"
        const val COMPLETERIDE = "trip/complete_trip"
        const val RATEANDREVIEW = "trip/rate_review"
        const val CONFIRMRECIEPT = "trip/trip_confirmpayment"
        const val PASTTRIP = "trip/past_trips"
        const val UPCOMINGTRIP = "trip/upcoming_trips"

        const val NOTIFICATION_LIST = "trip/notifications_list"
        const val NOTIFICATION_COUNT = "trip/notification_count"
        const val TRIP_DETAIL = "trip/tripdetails"
        const val CANCEL_RIDE_REASON = "trip/reasonslist"
        const val CANCEL_TRIP = "trip/cancel_trip"
        const val GENERATE_ACCESSTOKEN = "trip/chataccesstoken"
        const val CHANGELANGUAGE = "trip/changelanguage"

        const val RATE_REVIEW_LIST = "trip/ratereview_list"
    }


    object ResponseCode {

        const val SOCIAL_ID_NOT_REGISTER = 11
        const val INVALID_REQUEST_FAIL_REQUESt = 0
        const val SUCCESS = 1
        const val NO_DATA_FOUND = 2
        const val ACCOUNT_INACTIVE = 3
        const val OTP_VERIFICATION = 4
        const val EMAIL_VERIFICATOIN = 5
        const val FORCE_UPDATE = 6
        const val WAIT_FOR_CAR_DOCUEMENT = 12
        const val WAIT_FOR_DOCUEMENT = 13
        const val ADD_SUBSCRIPTION = 14
        const val ADD_BANKINFO = 15

    }

}
