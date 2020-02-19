package com.victoria.customer.data.datasource;

import com.victoria.customer.data.pojo.*
import com.victoria.customer.data.repository.TripRepository
import com.victoria.customer.data.service.TripService
import com.victoria.customer.model.*
import com.victoria.customer.model.VehicleData
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TripLiveDataSource @Inject constructor(private val tripService: TripService) : BaseDataSource(), TripRepository {
    override fun wallethistory(parameter: Parameter): Single<DataWrapper<List<PaymentHistory>>> {
        return execute(tripService.wallethistory(parameter))
    }

    override fun getwalletamount(parameter: Parameter): Single<DataWrapper<WalletAmount>> {
        return execute(tripService.getwalletamount())
    }

    override fun addwalletamount(parameter: Parameter): Single<DataWrapper<Unit>> {
        return execute(tripService.addwalletamount(parameter))
    }

    override fun cardlist(): Single<DataWrapper<List<CardListing>>> {
        return execute(tripService.cardlist())
    }

    override fun removecard(parameter: Parameter): Single<DataWrapper<Unit>> {
        return execute(tripService.removecard(parameter))
    }

    override fun getsdktoken(parameter: Parameter): Single<DataWrapper<PayfortAccess>> {
        return execute(tripService.getsdktoken(parameter))
    }

    override fun recurringRideApi(parameter: Parameter): Single<DataWrapper<Unit>> {
        return execute(tripService.recurringApi(parameter))
    }

    override fun cancelRide(parameter: Parameter): Single<DataWrapper<Unit>> {
        return execute(tripService.cancelRide(parameter))
    }

    override fun cancelRideReasons(parameter: Parameter): Single<DataWrapper<List<CancelReason>>> {
        return execute(tripService.cancelRideReasons(parameter))
    }

    override fun getFareEstimation(parameter: RideData): Single<DataWrapper<FareEstimationData>> {

        return execute(tripService.getFareEstimation(parameter))
    }

    override fun verifyPromocode(parameter: RideData): Single<DataWrapper<PromoCode>> {
        return execute(tripService.verifyPromocode(parameter))
    }

    override fun placeOrder(parameter: RideData): Single<DataWrapper<PlaceOrder>> {
        return execute(tripService.placeOrder(parameter))
    }

    override fun tripDetails(parameter: RideData): Single<DataWrapper<String>> {
        return execute(tripService.tripDetails(parameter))
    }

    override fun trackTrip(parameter: Parameter): Single<DataWrapper<PlaceOrder>> {
        return execute(tripService.trackTrip(parameter))
    }

    override fun vehicleList(): Single<DataWrapper<VehicleData>> {
        return execute(tripService.vehicleList())
    }

    override fun generateAccesstoken(parameter: Parameter): Single<DataWrapper<AccessData>> {
        return execute(tripService.generateAccesstoken(parameter))
    }

    override fun nearByDrivers(parameter: Parameter): Single<DataWrapper<List<NearByDriver>>> {
        return execute(tripService.nearByDrivers(parameter))
    }

}
