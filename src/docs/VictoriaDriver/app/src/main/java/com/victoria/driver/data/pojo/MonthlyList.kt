package com.victoria.driver.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MonthlyList {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null

}