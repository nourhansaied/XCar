package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentHistory {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("trip_id")
    @Expose
    public String tripId;
    @SerializedName("amount")
    @Expose
    public Double amount;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;
    @SerializedName("wallet_status")
    @Expose
    public String walletStatus;
    @SerializedName("wallethistory_date")
    @Expose
    public String wallethistoryDate;
    @SerializedName("transaction_id")
    @Expose
    public String transactionId;
    @SerializedName("trip_no")
    @Expose
    public String tripNo;
    @SerializedName("total_amount")
    @Expose
    public Double totalAmount;
    @SerializedName("payment_mode")
    @Expose
    public String paymentMode;
    @SerializedName("pickup_address")
    @Expose
    public String pickupAddress;
    @SerializedName("dropoff_address")
    @Expose
    public String dropoffAddress;

}