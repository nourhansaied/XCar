package com.victoria.customer.data.pojo

import com.google.gson.annotations.SerializedName

data class RateReviewData(
        @SerializedName("comment")
        var comment: String,
        @SerializedName("customer_id")
        var customerId: Int,
        @SerializedName("driver_id")
        var driverId: Int,
        @SerializedName("id")
        var id: Int,
        @SerializedName("insertdate")
        var insertdate: String,
        @SerializedName("profile_image")
        var profileImage: String,
        @SerializedName("profile_image_thumb")
        var profileImageThumb: String,
        @SerializedName("rate")
        var rate: Int,
        @SerializedName("rate_to")
        var rateTo: String,
        @SerializedName("trip_id")
        var tripId: String,
        @SerializedName("user_name")
        var userName: String
)