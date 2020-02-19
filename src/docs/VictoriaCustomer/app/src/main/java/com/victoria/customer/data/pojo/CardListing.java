package com.victoria.customer.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardListing {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("customer_id")
    @Expose
    public Integer customerId;
    @SerializedName("card_token")
    @Expose
    public String cardToken;
    @SerializedName("card_number")
    @Expose
    public String cardNumber;
    @SerializedName("expiry_date")
    @Expose
    public String expiryDate;
    @SerializedName("card_holder_name")
    @Expose
    public String cardHolderName;
    @SerializedName("card_type")
    @Expose
    public String cardType;
    @SerializedName("insertdate")
    @Expose
    public String insertdate;
    @SerializedName("card_id")
    @Expose
    public Integer cardId;
    @SerializedName("card_image")
    @Expose
    public String cardImage;

}