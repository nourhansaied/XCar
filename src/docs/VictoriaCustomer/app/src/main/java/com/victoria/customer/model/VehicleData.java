package com.victoria.customer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleData implements Parcelable {

    @SerializedName("Airport")
    @Expose
    public List<VictoriaVehicleData> airport = null;
    @SerializedName("Group")
    @Expose
    public List<VictoriaVehicleData> group = null;
    @SerializedName("VictoriaRoyal")
    @Expose
    public List<VictoriaVehicleData> victoriaRoyal = null;

    protected VehicleData(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VehicleData> CREATOR = new Creator<VehicleData>() {
        @Override
        public VehicleData createFromParcel(Parcel in) {
            return new VehicleData(in);
        }

        @Override
        public VehicleData[] newArray(int size) {
            return new VehicleData[size];
        }
    };
}