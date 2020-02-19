package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationData {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("primary_id")
    @Expose
    public Integer primaryId;
    @SerializedName("tablename")
    @Expose
    public String tablename;
    @SerializedName("driver_id")
    @Expose
    public Integer driverId;
    @SerializedName("customer_id")
    @Expose
    public Integer customerId;
    @SerializedName("receiver_type")
    @Expose
    public String receiverType;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("is_read")
    @Expose
    public String isRead;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("content_flag")
    @Expose
    public String contentFlag;
    @SerializedName("insertd_date")
    @Expose
    public String insertdDate;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("profile_image")
    @Expose
    public String profileImage;
    @SerializedName("profile_image_thumb")
    @Expose
    public String profileImageThumb;
}
