package com.victoria.customer.model;

/**
 * Created on 1/12/18.
 */
public class PlaceModel {
    public String heading;
    public String subTitle;
    public String placeId;


    public PlaceModel() {

    }

    public PlaceModel(String heading, String subTitle, String placeId) {
        this.heading = heading;
        this.subTitle = subTitle;
        this.placeId = placeId;
    }
}
