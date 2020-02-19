package com.victoria.driver.core

import android.Manifest

object Common {

    val DATE_TIME_FORMAT_TIMER: String? = "hh:mm a"
    val IS_OPEN_CHAT: String? = "IsOpenChat"
    @JvmField
    var PUSH_TAG_TWILLIO: String = "PushTagTwillio"
    const val IS_LOGIN = "is_login"
    const val SERVER_TIMEZONE = "UTC"
    val NAME: String = "name"
    val PROFILE: String? = "profile"
    val SENDER_ID: String = "sender_id"
    var WHICH_LANGUAGE_SELECTED = ""
    val ARABIC_LANGUAGE = "ar"
    val WHICH_LANGUAGE = "which_language"
    val LANGUAGE_SELECTION = "language_selection"
    val REQUEST_PERMISSION = 1
    val REQUEST_CAMERA_PERMISSION = 1
    val REQUEST_GALLERY_PERMISSION = 2
    val DATEFORMATUTC: String = "yyyy-MM-dd HH:mm:ss"
    val DATEFORMATONE: String = "dd MMMM, yyyy"
    val DATEFORMATTWO: String = "hh:mm a"

    val PERMISSIONS_GALLERY = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val ERRORLINK = "http://132.148.17.145/~hyperlinkserver/carshreport/crashcall.php"
    val EMAILREGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    const val FIRST_NAME_REGEX = "[a-zA-Z]+"
    //android:digits="0123456789qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM"
    val PROFILE_UPLOAD_KEY: String = "profile_image"
    val DEVICE_TYPE: String = "A"
    val DRIVING_LICENCE: String = "driving_license"
    val REGISTRATION_IMAGE: String = "registration_image"
    val CAR_FRONT_IMAGE: String = "car_frontimage"
    val CAR_BACK_IMAGE: String = "car_backimage"
    val ONLINE: String? = "Online"
    val CONTACT_US_IMAGE_UPLOAD: String = "cotactus_images"
    val DRIVER: String = "Driver"

    var screenText = "Screen"
    val MALE: String? = "Male"
    val FEMALE: String? = "Female"
    val BOTH: String? = "Both"
    val DRIVER_ID: String = "driver_id"


    val TRIP_TAG_WAITING: String = "Waiting"
    val TRIP_TAG_TRIP_NOW: String = "Now"
    val TRIP_TAG_ASSIGNED: String = "Assigned"
    val TRIP_TAG_ARRIVED: String = "Arrived"
    val TRIP_TAG_PROCESSING: String = "Processing"
    val ISCHANGEROUTE: String = "isChangeRoute"
    val TRIP_TAG_COMPLETE: String = "Completed"
    val TRIP_TAG_DRIVER_CONFIRMATION_STATUS_UNPAID: String = "unpaid"


    val DATE_TIME_FORMAT_OUT: String? = "dd MMMM, yyyy"
    val DATE_TIME_FORMAT_OUT_TWO: String? = "hh:mm a"
    val DATE_TIME_FORMAT_OUT_THREE: String? = "dd MMMM, yyyy - hh:mm a"
    val DATE_TIME_FORMAT_UTC: String? = "yyyy-MM-dd HH:mm:ss"
    val CUSTOMER: String = "Customer"


    @JvmField
    var TAG: String = "tag"
    @JvmField
    var TITLE: String = "title"
    @JvmField
    var BODY: String = "body"
    @JvmField
    var PUSH_TAG_PLACE_ORDER: String = "placeorder" //occurs when new request fire from customer side
    @JvmField
    var PUSH_TAG_PAYMENT: String = "payment"
    @JvmField
    var ACTIVITY_FIRST_PAGE: String = "activity_first_page"
    val TRIP_DISTANCE: String = "totalTripDistance"
    @JvmField
    var TRIP_ID: String = "trip_id"
    val TRIP_TAG_TRIP_LATER: String? = "Later"
    val TRIP_TIME_TO_CONTINUE: String = "TripTypeToContinue"
    val TRIP_PROGRESS: String = "TripProgress"
    val TRIP_TIME_TO_CONTINUE_BOOL: String = "TripTypeToContinueBool"
    @JvmField
    var PUSH_TAG_CANCEL_TRIP: String = "cancel_trip"
    @JvmField
    var PUSH_TAG_CANCEL_LATE_TRIP: String = "cancel_latertrip"

    @JvmField
    var PUSH_TAG_COMPLETE_ORDER: String = "complete_trip"

    object RequestCode {

        val REQUEST_TAKE_PHOTO = 1
        val RESULT_LOAD_IMAGE = 2
        val REQUEST_IMAGE_AND_VIDEO = 3
        val REQUEST_FROM_CAMERA = 4
        val REQUEST_TO_FINISH = 5
        val REQUEST = 10
        val REQUEST_TRIM_VIDEO = 11
        val CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203

        val PLACE_AUTOCOMPLETE_REQUEST_CODE = 123
    }

    object Language {
        val ENGLISH = "en"
        val ARABIC = "ar"
    }
}