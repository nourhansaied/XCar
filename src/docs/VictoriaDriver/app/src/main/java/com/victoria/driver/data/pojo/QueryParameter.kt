package com.victoria.driver.data.pojo

import com.google.gson.annotations.SerializedName

data class QueryParameter(@SerializedName("email") val email: String,
                          @SerializedName("password") val password: String,
                          @SerializedName("device_type") val deviceType: String,
                          @SerializedName("device_token") val deivciceToken: String) {
}