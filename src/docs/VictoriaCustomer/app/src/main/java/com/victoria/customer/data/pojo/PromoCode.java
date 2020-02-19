package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoCode {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("promocode")
    @Expose
    public String promocode;
    @SerializedName("promocodetype")
    @Expose
    public String promocodetype;
    @SerializedName("amount")
    @Expose
    public Integer amount;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("per_user_count")
    @Expose
    public Integer perUserCount;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("is_deleted")
    @Expose
    public String isDeleted;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;
    @SerializedName("promocode_id")
    @Expose
    public Integer promocodeId;

}