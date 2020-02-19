package com.victoria.customer.data.pojo

import com.google.gson.annotations.SerializedName

data class FavouriteAddress(
        @SerializedName("address")
        var address: String,
        @SerializedName("address_id")
        var addressId: Int,
        @SerializedName("id")
        var id: Int,
        @SerializedName("insertdate")
        var insertdate: String,
        @SerializedName("latitude")
        var latitude: String,
        @SerializedName("longitude")
        var longitude: String,
        @SerializedName("type")
        var type: String,
        @SerializedName("user_id")
        var userId: Int
)