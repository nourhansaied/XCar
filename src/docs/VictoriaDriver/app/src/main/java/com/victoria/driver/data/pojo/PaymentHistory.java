package com.victoria.driver.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentHistory {

    @SerializedName("trip_id")
    @Expose
    public Integer tripId;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
    @SerializedName("driver_amount")
    @Expose
    public Float driverAmount;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("tripdatetime")
    @Expose
    public String tripdatetime;
    @SerializedName("pickup_address")
    @Expose
    public String pickupAddress;
    @SerializedName("pickup_latitude")
    @Expose
    public String pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    public String pickupLongitude;
    @SerializedName("dropoff_address")
    @Expose
    public String dropoffAddress;
    @SerializedName("dropoff_latitude")
    @Expose
    public String dropoffLatitude;
    @SerializedName("dropoff_longitude")
    @Expose
    public String dropoffLongitude;
    @SerializedName("total_amount")
    @Expose
    public Float totalAmount;
    @SerializedName("payment_mode")
    @Expose
    public String paymentMode;
    @SerializedName("payment_status")
    @Expose
    public String paymentStatus;
    @SerializedName("status")
    @Expose
    public String status;


}