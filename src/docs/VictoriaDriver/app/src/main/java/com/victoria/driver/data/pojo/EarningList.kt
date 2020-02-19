package com.victoria.driver.data.pojo

import com.google.gson.annotations.SerializedName

class EarningList {
    constructor(date: String?, totalSpend: String?) {
        this.date = date
        this.totalSpend = totalSpend

    }

    @SerializedName("date")
    var date: String? = null

    @SerializedName("total_spend")
    var totalSpend: String? = null

}