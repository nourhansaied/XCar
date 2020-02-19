package com.victoria.customer.data.repository;

import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.pojo.*
import com.victoria.customer.model.*
import com.victoria.customer.model.VehicleData
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface TripRepository {

    fun getFareEstimation(parameter: RideData): Single<DataWrapper<FareEstimationData>>

    fun verifyPromocode(parameter: RideData): Single<DataWrapper<PromoCode>>

    fun placeOrder(parameter: RideData): Single<DataWrapper<PlaceOrder>>

    fun tripDetails(parameter: RideData): Single<DataWrapper<String>>

    fun trackTrip(parameter: Parameter): Single<DataWrapper<PlaceOrder>>

    fun nearByDrivers(parameter: Parameter): Single<DataWrapper<List<NearByDriver>>>

    fun generateAccesstoken(parameter: Parameter): Single<DataWrapper<AccessData>>

    fun getsdktoken(parameter: Parameter): Single<DataWrapper<PayfortAccess>>

    fun vehicleList(): Single<DataWrapper<VehicleData>>

    fun cancelRideReasons(parameter: Parameter): Single<DataWrapper<List<CancelReason>>>

    fun cancelRide(parameter: Parameter): Single<DataWrapper<Unit>>

    fun recurringRideApi(parameter: Parameter): Single<DataWrapper<Unit>>

    fun getwalletamount(parameter: Parameter): Single<DataWrapper<WalletAmount>>

    fun addwalletamount(parameter: Parameter): Single<DataWrapper<Unit>>

    fun cardlist(): Single<DataWrapper<List<CardListing>>>

    fun removecard(parameter: Parameter): Single<DataWrapper<Unit>>

    fun wallethistory(parameter: Parameter): Single<DataWrapper<List<PaymentHistory>>>

}
