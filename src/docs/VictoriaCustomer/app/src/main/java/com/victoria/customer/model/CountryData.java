package com.victoria.customer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created  on 2/8/17.
 */

public class CountryData {

    @SerializedName("e164_cc")
    @Expose
    public String e164Cc;
    @SerializedName("iso2_cc")
    @Expose
    public String iso2Cc;
    @SerializedName("e164_sc")
    @Expose
    public Integer e164Sc;
    @SerializedName("geographic")
    @Expose
    public Boolean geographic;
    @SerializedName("level")
    @Expose
    public Integer level;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("example")
    @Expose
    public String example;
    @SerializedName("display_name")
    @Expose
    public String displayName;
    @SerializedName("full_example_with_plus_sign")
    @Expose
    public String fullExampleWithPlusSign;
    @SerializedName("display_name_no_e164_cc")
    @Expose
    public String displayNameNoE164Cc;
    @SerializedName("e164_key")
    @Expose
    public String e164Key;




}