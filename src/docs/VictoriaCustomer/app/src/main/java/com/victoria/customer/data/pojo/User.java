package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("customer_id")
    @Expose
    public Integer customerId;
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
    @SerializedName("profile_image")
    @Expose
    public String profileImage;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("referral_code")
    @Expose
    public String referralCode;
    @SerializedName("wallet")
    @Expose
    public Double wallet;
    @SerializedName("otp_code")
    @Expose
    public String otpCode;
    @SerializedName("otp_verify")
    @Expose
    public String otpVerify;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("login_status")
    @Expose
    public String loginStatus;
    @SerializedName("last_login")
    @Expose
    public String lastLogin;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;
    @SerializedName("profile_image_thumb")
    @Expose
    public String profileImageThumb;
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
    @SerializedName("rating")
    @Expose
    public Double rating;

    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("vehicle_number")
    @Expose
    public String vehicle_number;
    @SerializedName("vehicle_model")
    @Expose
    public String vehicle_model;
    @SerializedName("vehicle_brand")
    @Expose
    public String vehicle_brand;


}
