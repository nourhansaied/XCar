package com.victoria.customer.model

import com.google.gson.annotations.SerializedName

data class ImageData(
        @SerializedName("profile_image")
        val profile_image: String,

        @SerializedName("image")
        val image: String
)

