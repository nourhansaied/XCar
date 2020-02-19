package com.victoria.driver.ui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarDocuments {

    @SerializedName("driving_license")
    @Expose
    public String drivingLicense = null;
    @SerializedName("registration_image")
    @Expose
    public String registrationImage = null;
    @SerializedName("car_frontimage")
    @Expose
    public String carFrontimage = null;
    @SerializedName("car_backimage")
    @Expose
    public String carBackimage = null;

}