package com.victoria.driver.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EarningsYear {


    @SerializedName("total_earning")
    @Expose
    public Double totalEarning;
    @SerializedName("total_trips")
    @Expose
    public Double totalTrips;
    @SerializedName("yearly_earnings")
    @Expose
    public List<DatewiseEarning> datewiseEarnings = null;

}