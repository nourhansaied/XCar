package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripDetails {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("trip_id")
    @Expose
    public Integer tripId;
    @SerializedName("promocode_id")
    @Expose
    public Integer promocodeId;
    @SerializedName("promocode")
    @Expose
    public String promocode;
    @SerializedName("promocode_amount_type")
    @Expose
    public String promocodeAmountType;
    @SerializedName("promocode_amount")
    @Expose
    public Double promocodeAmount;
    @SerializedName("promocode_discount")
    @Expose
    public Double promocodeDiscount;
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
    @SerializedName("duration_fare")
    @Expose
    public Double durationFare;
    @SerializedName("distance_fare")
    @Expose
    public Double distanceFare;
    @SerializedName("night_charge")
    @Expose
    public Double nightCharge;
    @SerializedName("rideby")
    @Expose
    public String rideby;
    @SerializedName("guest_name")
    @Expose
    public String guestName;
    @SerializedName("guest_country_code")
    @Expose
    public String guestCountryCode;
    @SerializedName("guest_phone")
    @Expose
    public String guestPhone;
    @SerializedName("accept_datetime")
    @Expose
    public String acceptDatetime;
    @SerializedName("cancel_datetime")
    @Expose
    public String cancelDatetime;
    @SerializedName("arrived_datetime")
    @Expose
    public String arrivedDatetime;
    @SerializedName("start_datetime")
    @Expose
    public String startDatetime;
    @SerializedName("end_datetime")
    @Expose
    public String endDatetime;
    @SerializedName("payment_datetime")
    @Expose
    public String paymentDatetime;
    @SerializedName("insert_datetime")
    @Expose
    public String insertDatetime;

}