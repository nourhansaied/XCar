package com.victoria.customer.ui.interfaces

/**
 * Created on 15/11/18.
 */
interface CallbackLocationSelection {

    fun onFavoritePlaceSelected()
    fun onSetOnMapSelected()
    fun onHomeSelected(homeLocation:String,latitude:Double,longitude:Double)
}
