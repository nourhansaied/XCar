package com.victoria.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FareEstimationData {

    @SerializedName("base_fare")
    @Expose
    public Double baseFare;
    @SerializedName("rate_per_kilometer")
    @Expose
    public Double ratePerKilometer;
    @SerializedName("rate_per_min")
    @Expose
    public Double ratePerMin;
    @SerializedName("waiting_rate_per_min")
    @Expose
    public Double waitingRatePerMin;
    @SerializedName("distance")
    @Expose
    public Double distance;
    @SerializedName("distance_text")
    @Expose
    public String distanceText;
    @SerializedName("time")
    @Expose
    public Double time;
    @SerializedName("time_text")
    @Expose
    public String timeText;
    @SerializedName("distance_fare")
    @Expose
    public Double distanceFare;
    @SerializedName("duration_fare")
    @Expose
    public Double durationFare;
    @SerializedName("total_amount")
    @Expose
    public Double totalAmount;
    @SerializedName("promocode_amount")
    @Expose
    public Double promocodeAmount;
    @SerializedName("min_total_amount")
    @Expose
    public Double minTotalAmount;
    @SerializedName("max_total_amount")
    @Expose
    public Double maxTotalAmount;
    @SerializedName("driver_earning")
    @Expose
    public Double driverEarning;
    @SerializedName("owner_earning")
    @Expose
    public Double ownerEarning;

}