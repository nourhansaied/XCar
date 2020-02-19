package com.victoria.customer.model;

/**
 * Created on 8/10/18.
 */
public class Service {

    public String carType;
    public String carPrice;
    public int carImage;

    public Service(String carType, String carPrice, int carImage) {
        this.carType = carType;
        this.carPrice = carPrice;
        this.carImage = carImage;
    }
}
