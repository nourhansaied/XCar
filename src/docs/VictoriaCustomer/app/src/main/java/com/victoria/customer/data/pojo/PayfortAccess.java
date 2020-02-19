package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayfortAccess {

    @SerializedName("response_code")
    @Expose
    public String responseCode;
    @SerializedName("device_id")
    @Expose
    public String deviceId;
    @SerializedName("response_message")
    @Expose
    public String responseMessage;
    @SerializedName("service_command")
    @Expose
    public String serviceCommand;
    @SerializedName("sdk_token")
    @Expose
    public String sdkToken;
    @SerializedName("signature")
    @Expose
    public String signature;
    @SerializedName("merchant_identifier")
    @Expose
    public String merchantIdentifier;
    @SerializedName("access_code")
    @Expose
    public String accessCode;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("status")
    @Expose
    public String status;

}