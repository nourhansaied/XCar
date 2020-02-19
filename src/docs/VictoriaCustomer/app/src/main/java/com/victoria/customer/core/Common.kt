package com.victoria.customer.core;

import android.Manifest
import com.payfort.fort.android.sdk.base.FortSdk

public object Common {

    val IS_FROM_RECEIPT: String? = "IsFromReceipt"
    val SELECTED_LANGUAGE: String = "selected_language"
    val IS_OPEN_CHAT: String? = "IsOpenChat"
    val DATE_TIME_FORMAT_TIMER: String? = "hh:mm a"
    val PAYFORT_ENVIRONMENT = FortSdk.ENVIRONMENT.PRODUCTION
    val NAME: String = "name"
    val PROFILE: String? = "profile"
    val SENDER_ID: String = "sender_id"
    const val ERRORLINK = "http://132.148.17.145/~hyperlinkserver/carshreport/crashcall.php"
    const val URL_GOOGLE = "https://www.google.co.in"
    var WHICH_LANGUAGE_SELECTED = ""
    val ARABIC_LANGUAGE = "ar"
    @JvmField

    val WHICH_LANGUAGE = "which_language"
    @JvmField
    val LANGUAGE_SELECTION = "language_selection"

    const val REQUEST_CAMERA_PERMISSION = 1
    const val REQUEST_GALLERY_PERMISSION = 2
    const val PICKEUP_LOCATION = 1
    const val DROPOFF_LOCATION = 2
    const val SERVER_TIMEZONE = "UTC"

    const val IS_PICKUP = 1
    const val IS_DROPOFF = 2


    val PERMISSIONS_GALLERY = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    var IS_END_DATE_SELECTED = false


    val EMAILREGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    const val FIRST_NAME_REGEX = "[a-zA-Z]+"
    const val CARD_NAME_REGEX = "[a-zA-Z ]+"


    @JvmField
    var latitudeCurrent: kotlin.Double = 23.0752
    @JvmField
    var longitudeCurrent: kotlin.Double = 72.5257

    val MALE: String? = "Male"
    val FEMALE: String? = "Female"

    var screenText = "Screen"
    val DEVICE_TYPE: String = "A"
    val PROFILE_UPLOAD_KEY: String = "profile_image"
    val IS_FROM_SETTING: String = "is_from_setting"
    val DATEFORMATUTC: String = "yyyy-MM-dd HH:mm:ss"
    val DATEFORMATONE: String = "dd MMMM, yyyy"
    val DATEFORMATTWO: String = "hh:mm a"
    val CONTACT_US_IMAGE_UPLOAD: String = "cotactus_images"
    val CUSTOMER: String = "Customer"
    val VEHICLE_DATA: String? = "vehicleData"
    val PICKEUP_LOCATION_: String? = "pickUpLocation"
    val DROPOFF_LOCATION_: String? = "dropOffLocation"


    val TRIP_TAG_WAITING: String? = "Waiting"
    val TRIP_TAG_TRIP_NOW: String? = "Now"
    val TRIP_TAG_ASSIGNED: String? = "Assigned"
    val TRIP_TAG_ARRIVED: String? = "Arrived"
    val TRIP_TAG_PROCESSING: String? = "Processing"
    val TRIP_TAG_COMPLETED: String? = "Completed"
    val TRIP_TAG_PAYMENT_STATUS_UNPAID = "unpaid"


    val DATE_TIME_FORMAT_OUT: String? = "dd MMMM, yyyy"
    val DATE_TIME_FORMAT_UTC: String? = "yyyy-MM-dd HH:mm:ss"
    val DATE_TIME_FORMAT_OUT_TWO: String? = "hh:mm a"
    val DATE_TIME_FORMAT_OUT_THREE: String? = "dd MMMM, yyyy - hh:mm a"

    val DRIVER: String = "Driver"

    object Language {
        val ENGLISH = "en"
        val ARABIC = "ar"
    }

    object RequestCode {

        const val REQUEST_TAKE_PHOTO = 1
        const val RESULT_LOAD_IMAGE = 2
        const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 123
    }

    enum class AddressType(val addressType: String) {

        HOME("home"),
        WORK("work"),
        OTHER("other")
    }

    enum class RideType(val rideType: String) {
        NOW("Now"),
        LATER("Later")
    }


    @JvmField
    var TAG: String = "tag"
    @JvmField
    var TITLE: String = "title"
    @JvmField
    var BODY: String = "body"
    @JvmField
    var PUSH_TAG_DECLINE_ORDER: String = "decline_order"
    @JvmField
    var PUSH_TAG_ACCEPT_ORDER: String = "accept_order"
    @JvmField
    var PUSH_TAG_ARRIVED_ORDER: String = "arrived_order"
    @JvmField
    var PUSH_TAG_REFER_USER: String = "refer_user"
    @JvmField
    var PUSH_TAG_START_ORDER: String = "start_order"
    @JvmField
    var PUSH_TAG_CHANGE_DROP_OFF_ORDER: String = "changedropoff_trip"
    @JvmField
    var PUSH_TAG_CANCEL_TRIP: String = "cancel_trip"
    @JvmField
    var PUSH_TAG_CANCEL_LATE_TRIP: String = "cancel_latertrip"
    @JvmField
    var PUSH_TAG_TWILLIO: String = "PushTagTwillio"
    @JvmField
    var PUSH_TAG_COMPLETE_ORDER: String = "complete_trip"
    @JvmField
    var ACTIVITY_FIRST_PAGE: String = "activity_first_page"
    val SHOW_MESSGE_FOR_NO_DRIVER_AVAILABLE: String? = "showMessageForNoDriver"
    val MESSAGE: String? = "message"
    val TOTAL_COST: String? = "totalCost"
    val TRIP_ID: String? = "trip_id"
    val TRIP_TAG_TRIP_LATER: String? = "Later"

}