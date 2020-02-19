package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelReason {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("reason")
    @Expose
    public String reason;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("sortorder")
    @Expose
    public String sortorder;
    @SerializedName("is_deleted")
    @Expose
    public String isDeleted;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;
    @SerializedName("reason_id")
    @Expose
    public Integer reasonId;

}