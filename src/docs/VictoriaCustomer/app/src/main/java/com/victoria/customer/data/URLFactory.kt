package com.victoria.customer.data;

import okhttp3.HttpUrl

/**
 * Created by hlink21 on 11/5/17.
 */

object URLFactory {

    // server details
    //http://3.122.9.220:5180/v1/
    //http://192.168.1.101:5180/v1/Api_document/api_doc/
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

        const val SIGNUP = "customer/signup"
        const val RESEND_OTP = "customer/resend_otp"
        const val VERIFY_OTP = "customer/verify_otp"
        const val LOGIN = "customer/login"
        const val EDIT_PROFILE = "customer/edit_profile"
        const val CHANGE_PASSWORD = "customer/changepassword"
        const val FORGOT_PASSWORD = "customer/forgotpassword"
        const val LOGOUT = "customer/logout"
        const val PROFILE_IMAGE_UPLOAD = "customer/profile_image_upload"

        const val ADD_FAVOURITE_ADDRESS = "customer/add_favoriteaddress"
        const val FAVOURITE_ADDRESS_LIST = "customer/favoriteaddress_list"
        const val REMOVE_FAVOURITE_ADDRESS = "customer/remove_favoriteaddress"
        const val USERDETAIL = "trip/user_details"

        /**============TRIP =====================*/
        const val CONTACT_US_IMAGE_UPLOAD = "trip/contactus_image_upload"
        const val CONTACT_US = "trip/contact_us"
        const val NEAR_BY_DRIVERS = "trip/nearbydrivers"
        const val CANCEL_RIDE_REASON = "trip/reasonslist"
        const val VEHICLE_LIST = "trip/vehicle_list"
        const val FARE_ESTIMATION = "trip/getfare_estimation"
        const val VERIFY_PROMOCODE = "trip/verifypromocode"
        const val PLACE_ORDER = "trip/placeorder"
        const val TRIP_DETAILS = "trip/tripdetails"
        const val TRACK_TRIP = "trip/tracktrip"
        const val CANCEL_TRIP = "trip/cancel_trip"
        const val RECURRING_API = "trip/recurring_trip"
        const val PAYMENTDONE = "trip/payment_trip"
        const val RATEANDREVIEW = "trip/rate_review"
        const val PASTTRIP = "trip/past_trips"
        const val UPCOMINGTRIP = "trip/upcoming_trips"
        const val NOTIFICATION_LIST = "trip/notifications_list"
        const val NOTIFICATION_COUNT = "trip/notification_count"
        const val RATE_REVIEW_LIST = "trip/ratereview_list"
        const val GENERATE_ACCESSTOKEN = "trip/chataccesstoken"
        const val GETSDKTOKEN= "trip/getsdktoken"
        const val GETWALLETAMOUNT= "customer/getwalletamount"
        const val ADDWALLETAMOUNT= "customer/addwalletamount"
        const val REMOVECARD= "customer/removecard"
        const val CARDLIST= "customer/cardlist"
        const val WALLETHISTORY= "customer/wallethistory"
        const val CHANGELANGUAGE = "trip/changelanguage"

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
