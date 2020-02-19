package com.victoria.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VictoriaVehicleData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("vehicle")
    @Expose
    public String vehicle;
    @SerializedName("types")
    @Expose
    public String types;
    @SerializedName("selected_image")
    @Expose
    public String selectedImage;
    @SerializedName("unselected_image")
    @Expose
    public String unselectedImage;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("is_deleted")
    @Expose
    public String isDeleted;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;
    @SerializedName("vehicle_id")
    @Expose
    public Integer vehicleId;
    @SerializedName("vehicle_selectedimage")
    @Expose
    public String vehicleSelectedimage;
    @SerializedName("vehicle_thumb_selectedimage")
    @Expose
    public String vehicleThumbSelectedimage;
    @SerializedName("vehicle_unselectedimage")
    @Expose
    public String vehicleUnselectedimage;
    @SerializedName("vehicle_thumb_unselectedimage")
    @Expose
    public String vehicleThumbUnselectedimage;
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

}