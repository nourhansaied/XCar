package com.victoria.driver.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportDatum {

    @SerializedName("total_trip_earning")
    @Expose
    public Double totalTripEarning;
    @SerializedName("driver_earning")
    @Expose
    public Double driverEarning;

}