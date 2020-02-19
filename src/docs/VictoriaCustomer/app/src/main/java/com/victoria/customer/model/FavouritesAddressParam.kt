package com.victoria.customer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavouritesAddressParam(

        var address: String? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var type: String? = null,
        var address_id:String?=null


) : Parcelable