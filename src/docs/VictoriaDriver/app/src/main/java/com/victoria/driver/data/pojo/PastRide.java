package com.victoria.driver.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PastRide {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("trip_no")
    @Expose
    public String tripNo;
    @SerializedName("channel_sid")
    @Expose
    public String channelSid;
    @SerializedName("customer_id")
    @Expose
    public Integer customerId;
    @SerializedName("driver_id")
    @Expose
    public Integer driverId;
    @SerializedName("vehicle_id")
    @Expose
    public Integer vehicleId;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
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
    @SerializedName("splitting_ratio")
    @Expose
    public String splittingRatio;
    @SerializedName("trip_type")
    @Expose
    public String tripType;
    @SerializedName("distance")
    @Expose
    public Float distance;
    @SerializedName("ride_time")
    @Expose
    public Float rideTime;
    @SerializedName("waiting_time")
    @Expose
    public Integer waitingTime;
    @SerializedName("total_time")
    @Expose
    public Float totalTime;
    @SerializedName("driver_earning")
    @Expose
    public Float driverEarning;
    @SerializedName("owner_earning")
    @Expose
    public Float ownerEarning;
    @SerializedName("total_amount")
    @Expose
    public Float totalAmount;
    @SerializedName("payment_mode")
    @Expose
    public String paymentMode;
    @SerializedName("payment_status")
    @Expose
    public String paymentStatus;
    @SerializedName("driver_confirmpayment_status")
    @Expose
    public String driverConfirmpaymentStatus;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("cancel_by")
    @Expose
    public String cancelBy;
    @SerializedName("reason")
    @Expose
    public Integer reason;
    @SerializedName("comment")
    @Expose
    public Comment comment;
    @SerializedName("cancel_charge")
    @Expose
    public Float cancelCharge;
    @SerializedName("cancel_driverearning")
    @Expose
    public Float cancelDriverearning;
    @SerializedName("cancel_ownerearning")
    @Expose
    public Float cancelOwnerearning;
    @SerializedName("local_tripdatetime")
    @Expose
    public String localTripdatetime;
    @SerializedName("tripdatetime")
    @Expose
    public String tripdatetime;
    @SerializedName("trip_mapurl")
    @Expose
    public String tripMapurl;
    @SerializedName("sent_reminder_type")
    @Expose
    public String sentReminderType;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;

}