package com.victoria.customer.data.service;

import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.pojo.*
import com.victoria.customer.model.*
import com.victoria.customer.model.VehicleData
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TripService {

    @POST(URLFactory.Method.FARE_ESTIMATION)
    fun getFareEstimation(@Body parameter: RideData): Single<ResponseBody<FareEstimationData>>

    @POST(URLFactory.Method.VERIFY_PROMOCODE)
    fun verifyPromocode(@Body parameter: RideData): Single<ResponseBody<PromoCode>>

    @POST(URLFactory.Method.PLACE_ORDER)
    fun placeOrder(@Body parameter: RideData): Single<ResponseBody<PlaceOrder>>

    @POST(URLFactory.Method.TRIP_DETAILS)
    fun tripDetails(@Body parameter: RideData): Single<ResponseBody<String>>

    @POST(URLFactory.Method.TRACK_TRIP)
    fun trackTrip(@Body parameter: Parameter): Single<ResponseBody<PlaceOrder>>

    @GET(URLFactory.Method.VEHICLE_LIST)
    fun vehicleList(): Single<ResponseBody<VehicleData>>

    @POST(URLFactory.Method.GENERATE_ACCESSTOKEN)
    fun generateAccesstoken(@Body parameter: Parameter): Single<ResponseBody<AccessData>>

    @POST(URLFactory.Method.GETSDKTOKEN)
    fun getsdktoken(@Body parameter: Parameter): Single<ResponseBody<PayfortAccess>>

    @POST(URLFactory.Method.NEAR_BY_DRIVERS)
    fun nearByDrivers(@Body parameter: Parameter): Single<ResponseBody<List<NearByDriver>>>

    @POST(URLFactory.Method.CANCEL_RIDE_REASON)
    fun cancelRideReasons(@Body parameter: Parameter): Single<ResponseBody<List<CancelReason>>>

    @POST(URLFactory.Method.CANCEL_TRIP)
    fun cancelRide(@Body parameter: Parameter): Single<ResponseBody<Unit>>

    @POST(URLFactory.Method.RECURRING_API)
    fun recurringApi(@Body parameter: Parameter): Single<ResponseBody<Unit>>

    @GET(URLFactory.Method.GETWALLETAMOUNT)
    fun getwalletamount(): Single<ResponseBody<WalletAmount>>

    @POST(URLFactory.Method.ADDWALLETAMOUNT)
    fun addwalletamount(@Body parameter: Parameter): Single<ResponseBody<Unit>>

    @GET(URLFactory.Method.CARDLIST)
    fun cardlist(): Single<ResponseBody<List<CardListing>>>

    @POST(URLFactory.Method.WALLETHISTORY)
    fun wallethistory(@Body parameter: Parameter): Single<ResponseBody<List<PaymentHistory>>>

    @POST(URLFactory.Method.REMOVECARD)
    fun removecard(@Body parameter: Parameter): Single<ResponseBody<Unit>>


}