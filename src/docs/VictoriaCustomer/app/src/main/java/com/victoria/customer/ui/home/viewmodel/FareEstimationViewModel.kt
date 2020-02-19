package com.victoria.customer.ui.home.viewmodel

import com.victoria.customer.data.pojo.*
import com.victoria.customer.data.repository.TripRepository
import com.victoria.customer.model.FareEstimationData
import com.victoria.customer.model.Parameter
import com.victoria.customer.model.RideData
import com.victoria.customer.ui.base.APILiveData
import com.victoria.customer.ui.base.BaseViewModel
import io.reactivex.Single
import javax.inject.Inject

class FareEstimationViewModel @Inject constructor(private val tripRepository: TripRepository) : BaseViewModel() {

    var fareEstimationLiveData = APILiveData<FareEstimationData>()
    var verifyPromocodeLiveData = APILiveData<PromoCode>()
    var placeOrderLiveData = APILiveData<PlaceOrder>()
    var recurringLiveData = APILiveData<Unit>()
    var getwalletamount = APILiveData<WalletAmount>()
    var addwalletamount = APILiveData<Unit>()
    var cardlist = APILiveData<List<CardListing>>()
    var removecard = APILiveData<Unit>()
    val paymentHistoryList = APILiveData<List<PaymentHistory>>()

    fun paymentHistoryListApi(parameter: Parameter) {
        tripRepository.wallethistory(parameter).subscribe(withLiveData(paymentHistoryList))
    }

    fun getwalletamount(param: Parameter) {
        tripRepository.getwalletamount(param).subscribe(withLiveData(getwalletamount))
    }

    fun addwalletamount(param: Parameter) {
        tripRepository.addwalletamount(param).subscribe(withLiveData(addwalletamount))
    }

    fun cardlist(param: Parameter) {
        tripRepository.cardlist().subscribe(withLiveData(cardlist))
    }

    fun removecard(param: Parameter) {
        tripRepository.removecard(param).subscribe(withLiveData(removecard))
    }

    fun getFareEstimation(param: RideData) {
        tripRepository.getFareEstimation(param).subscribe(withLiveData(fareEstimationLiveData))
    }

    fun verifyPromocode(param: RideData) {
        tripRepository.verifyPromocode(param).subscribe(withLiveData(verifyPromocodeLiveData))
    }

    fun recurringLiveData(parameter: Parameter) {
        tripRepository.recurringRideApi(parameter).subscribe(withLiveData(recurringLiveData))
    }

    fun placeOrder(param: RideData) {
        tripRepository.placeOrder(param).subscribe(withLiveData(placeOrderLiveData))
    }

}