package com.victoria.driver.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comment {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("data")
    @Expose
    public List<Object> data = null;

}