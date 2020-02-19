package com.victoria.driver.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("driver_id")
    @Expose
    public Integer driverId;
    @SerializedName("vehicle_id")
    @Expose
    public Integer vehicleId;
    @SerializedName("vehicle_brand")
    @Expose
    public String vehicleBrand;
    @SerializedName("vehicle_model")
    @Expose
    public String vehicleModel;
    @SerializedName("vehicle_number")
    @Expose
    public String vehicleNumber;
    @SerializedName("vehicle_color")
    @Expose
    public String vehicleColor;
    @SerializedName("national_id")
    @Expose
    public String nationalId;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("country_code")
    @Expose
    public String countryCode;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("pre_latitude")
    @Expose
    public String preLatitude;
    @SerializedName("pre_longitude")
    @Expose
    public String preLongitude;
    @SerializedName("is_addvehicledetails")
    @Expose
    public String isAddvehicledetails;
    @SerializedName("is_addbankdetails")
    @Expose
    public String isAddbankdetails;
    @SerializedName("bank_name")
    @Expose
    public String bankName;
    @SerializedName("account_holder_name")
    @Expose
    public String accountHolderName;
    @SerializedName("account_number")
    @Expose
    public String accountNumber;
    @SerializedName("routing_number")
    @Expose
    public String routingNumber;
    @SerializedName("otp_code")
    @Expose
    public String otpCode;
    @SerializedName("otp_verify")
    @Expose
    public String otpVerify;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("doc_status")
    @Expose
    public String docStatus;
    @SerializedName("service")
    @Expose
    public String service;
    @SerializedName("is_free")
    @Expose
    public String isFree;
    @SerializedName("login_status")
    @Expose
    public String loginStatus;
    @SerializedName("last_login")
    @Expose
    public String lastLogin;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("device_token")
    @Expose
    public String deviceToken;
    @SerializedName("device_type")
    @Expose
    public String deviceType;
    @SerializedName("profile_image")
    @Expose
    public String profileImage;
    @SerializedName("profile_image_thumb")
    @Expose
    public String profileImageThumb;
    @SerializedName("driving_license")
    @Expose
    public String drivingLicense;
    @SerializedName("registration_image")
    @Expose
    public String registrationImage;
    @SerializedName("car_frontimage")
    @Expose
    public String carFrontimage;
    @SerializedName("car_backimage")
    @Expose
    public String carBackimage;
    @SerializedName("rating")
    @Expose
    public Double rating;
    @SerializedName("driver_no")
    @Expose
    public String driverNo;

    @SerializedName("allow_gender")
    @Expose
    public String allowGender;




}
