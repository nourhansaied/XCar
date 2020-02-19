package com.victoria.driver.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationHandler {


    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("post_id")
    @Expose
    public String postId;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @SerializedName("serviceprovider_id")
    @Expose
    private String serviceproviderId;
    @SerializedName("message_id")
    @Expose
    private String messageId;

    @SerializedName("receiver_id")
    @Expose
    private String receiverId;

    @SerializedName("s_name")
    @Expose
    private String sName;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("r_name")
    @Expose
    private String rName;
    @SerializedName("chat_id")
    @Expose
    private String chatId;

    @SerializedName("sender_name")
    @Expose
    private String sender_name;

    @SerializedName("middle_name")
    @Expose
    private String middle_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getServiceproviderId() {
        return serviceproviderId;
    }

    public void setServiceproviderId(String serviceproviderId) {
        this.serviceproviderId = serviceproviderId;
    }


    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}