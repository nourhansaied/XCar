package com.victoria.customer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Parameter : Serializable {

    @SerializedName("user_id")
    @Expose
    lateinit var userId: String
    @SerializedName("certificates")
    lateinit var certificates: String
    @SerializedName("login_type")
    lateinit var loginType: String
    @SerializedName("identity")
    lateinit var identity: String

    @SerializedName("password")
    lateinit var password: String

    @SerializedName("username")
    lateinit var userName: String

    @SerializedName("social_id")
    lateinit var socialId: String

    @SerializedName("device_token")
    lateinit var deviceToken: String

    @SerializedName("device_type")
    lateinit var devieType: String

    @SerializedName("country_code")
    lateinit var countryCode: String

    @SerializedName("phone")
    lateinit var phone: String

    @SerializedName("email")
    lateinit var email: String

    @SerializedName("contact_image")
    lateinit var contact_image: String

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("account_type")
    lateinit var accountType: String

    @SerializedName("profile_image")
    lateinit var profileImage: String

    @SerializedName("location")
    lateinit var location: String

    @SerializedName("latitude")
    lateinit var latitude: String

    @SerializedName("longitude")
    lateinit var longitude: String

    @SerializedName("os_version")
    lateinit var osVersion: String

    @SerializedName("app_language")
    lateinit var appLanguage: String


    @SerializedName("device_name")
    lateinit var deviceName: String

    @SerializedName("device_id")
    lateinit var device_id: String

    @SerializedName("model_name")
    lateinit var modelName: String

    @SerializedName("age")
    lateinit var age: String

    @SerializedName("gender")
    lateinit var gender: String

    @SerializedName("drink")
    lateinit var drink: String

    @SerializedName("about_me")
    lateinit var aboutMe: String

    @SerializedName("company_name")
    lateinit var companyName: String

    @SerializedName("about_business")
    lateinit var aboutBusiness: String

    @SerializedName("old_password")
    lateinit var oldPassword: String


    @SerializedName("date")
    lateinit var date: String

    @SerializedName("time")
    lateinit var time: String

    @SerializedName("age_range")
    lateinit var ageRange: String

    @SerializedName("image")
    lateinit var image: ArrayList<String>

    @SerializedName("description")
    lateinit var description: String

    @SerializedName("customer_id")
    lateinit var customer_id: String


    @SerializedName("joining_fees")
    lateinit var joiningFees: String

    @SerializedName("page")
    lateinit var page: Number

    @SerializedName("member_limit")
    lateinit var memberLimit: Number

    @SerializedName("party_id")
    @Expose
    lateinit var partyId: Number

    @SerializedName("status")
    @Expose
    lateinit var status: String

    @SerializedName("request_id")
    @Expose
    lateinit var requestId: String

    @SerializedName("friend_id")
    @Expose
    lateinit var friendId: Number

    @SerializedName("reason")
    @Expose
    lateinit var reason: String

    @SerializedName("range")
    @Expose
    lateinit var range: Number

    @SerializedName("type")
    @Expose
    lateinit var type: String

    @SerializedName("otp_code")
    @Expose
    lateinit var otpCode: String

    @SerializedName("email_phone")
    lateinit var email_phone: String

    @SerializedName("uuid")
    lateinit var uuid: String

    @SerializedName("first_name")
    lateinit var first_name: String

    @SerializedName("last_name")
    lateinit var last_name: String

    @SerializedName("ip")
    lateinit var ip: String

    @SerializedName("address")
    lateinit var address: String

    @SerializedName("brand_id")
    lateinit var brand_id: String

    @SerializedName("type_id")
    lateinit var type_id: String

    @SerializedName("vehicle_id")
    lateinit var vehicle_id: String

    @SerializedName("model_id")
    lateinit var model_id: String

    @SerializedName("registration_model")
    lateinit var registration_model: String

    @SerializedName("color")
    lateinit var color: String

    @SerializedName("national_id")
    lateinit var national_id: String

    @SerializedName("back_image")
    lateinit var back_image: String

    @SerializedName("front_image")
    lateinit var front_image: String

    @SerializedName("side_image")
    lateinit var side_image: String

    @SerializedName("registration_image")
    lateinit var registration_image: String

    @SerializedName("inspection_image")
    lateinit var inspection_image: String

    @SerializedName("police_report_image")
    lateinit var police_report_image: String

    @SerializedName("owner_id_image")
    lateinit var owner_id_image: String

    @SerializedName("card_holder_name")
    lateinit var card_holder_name: String

    @SerializedName("card_number")
    lateinit var card_number: String

    @SerializedName("exp_month")
    lateinit var exp_month: String
    @SerializedName("expiry_date")
    lateinit var expiry_date: String
    @SerializedName("card_type")
    lateinit var card_type: String


    @SerializedName("exp_year")
    lateinit var exp_year: String

    @SerializedName("cvv")
    lateinit var cvv: String

    @SerializedName("plan_id")
    lateinit var plan_id: String

    @SerializedName("card_id")
    lateinit var card_id: String

    @SerializedName("bank_name")
    lateinit var bank_name: String

    @SerializedName("account_holder_name")
    lateinit var account_holder_name: String

    @SerializedName("account_number")
    lateinit var account_number: String

    @SerializedName("bank_address")
    lateinit var bank_address: String

    @SerializedName("bank_branch")
    lateinit var bank_branch: String

    @SerializedName("user_type")
    lateinit var user_type: String

    @SerializedName("subject")
    lateinit var subject: String

    @SerializedName("message")
    lateinit var message: String

    @SerializedName("contact_images")
    lateinit var contact_images: String

    @SerializedName("full_name")
    lateinit var full_name: String

    @SerializedName("geo_fence")
    lateinit var geoFence: String

    @SerializedName("geofence_distance")
    lateinit var geoFenceDistance: String

    @SerializedName("student_id")
    lateinit var studentId: String

    @SerializedName("start_date")
    lateinit var startDate: String

    @SerializedName("end_date")
    lateinit var endDate: String

    @SerializedName("speed_limit")
    lateinit var speedLimit: String

    @SerializedName("speed_milestone")
    lateinit var speedMileStone: String

    @SerializedName("trip_date")
    lateinit var tripDate: String

    @SerializedName("route_id")
    lateinit var routeId: String

    @SerializedName("new_pickup_location")
    lateinit var newPickUpLocation: String

    @SerializedName("new_pickup_latitude")
    lateinit var newPickUpLat: String

    @SerializedName("new_pickup_longitude")
    lateinit var newPickUpLng: String

    @SerializedName("notification_id")
    lateinit var notification_id: String

    @SerializedName("new_route_id")
    lateinit var new_route_id: String

    @SerializedName("trip_id")
    lateinit var tripId: String

    @SerializedName("amount")
    lateinit var amount: String

    @SerializedName("reason_id")
    lateinit var reasonId: String

    @SerializedName("merchant_reference")
    lateinit var merchant_reference: String

    @SerializedName("transaction_id")
    lateinit var transaction_id: String

    @SerializedName("authorization_code")
    lateinit var authorization_code: String
    @SerializedName("card_token")
    lateinit var card_token: String

    @SerializedName("payment_mode")
    lateinit var paymentMode: String

    @SerializedName("rate")
    lateinit var rate: String

    @SerializedName("ratetouser_id")
    lateinit var ratetouser_id: String

    @SerializedName("ratetouser_type")
    lateinit var ratetouser_type: String

    @SerializedName("comment")
    lateinit var comment: String
}