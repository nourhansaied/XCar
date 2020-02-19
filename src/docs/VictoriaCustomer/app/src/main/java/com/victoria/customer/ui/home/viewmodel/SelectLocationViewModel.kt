package com.victoria.customer.ui.home.viewmodel

import com.victoria.customer.data.pojo.FavouriteAddress
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.model.FavouritesAddressParam
import com.victoria.customer.model.NearByDriver
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import javax.inject.Inject

class SelectLocationViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    var addFavLiveData = APILiveData<String>()
    var favouriteAddressLiveData=APILiveData<List<FavouriteAddress>>()
    var removeAddressLiveData = APILiveData<String>()
    var nearByDriver = APILiveData<List<NearByDriver>>()

    fun nearbyDriverListing(parameter: Parameter) {
        userRepository.nearByDrivers(parameter).subscribe(withLiveData(nearByDriver))
    }

    fun addFavouriteAddress(parameter: FavouritesAddressParam) {
        userRepository.addFavouriteAddress(parameter).subscribe(withLiveData(addFavLiveData))
    }

    fun getFavouriteList(){
        userRepository.favouritesAddressList().subscribe(withLiveData(favouriteAddressLiveData))
    }

    fun removeFavouriteAddress(parameter: FavouritesAddressParam){

        userRepository.removeFavouriteAddress(parameter).subscribe(withLiveData(removeAddressLiveData))
    }
}