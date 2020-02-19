package com.victoria.driver.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatewiseEarning {

    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("driver_earning")
    @Expose
    public Double driverEarning;
    @SerializedName("total_trips")
    @Expose
    public Double totalTrips;

}