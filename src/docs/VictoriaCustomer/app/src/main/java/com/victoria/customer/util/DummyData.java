package com.victoria.customer.util;

import com.google.android.gms.maps.model.LatLng;
import com.victoria.customer.R;
import com.victoria.customer.model.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 10/10/18.
 */
public class DummyData {

    public static ArrayList<Service> getCarTypes(int position){

        ArrayList<Service>  carList=new ArrayList<>();

        if(position==1) {
            carList.clear();
            carList.add(new Service("Sedan", "EGP 10-15", R.drawable.type_1_car_1));
            carList.add(new Service("Van", "EGP 8-10", R.drawable.type_1_car_2));

        }else if(position==2){
            carList.clear();
            carList.add(new Service("Sedan", "EGP 30-50", R.drawable.v_royal_1));
            carList.add(new Service("4x4", "EGP 30-40", R.drawable.v_royal_2));

        }if(position==4){
            carList.clear();
            carList.add(new Service("Sedan", "EGP 40-50", R.drawable.ic_airport_1));
            carList.add(new Service("Van", "EGP 25-45", R.drawable.ic_airport_2));

        }if(position==3){
            carList.clear();
            carList.add(new Service("Van", "EGP 25-45", R.drawable.van_group_1));

        }

        return carList;

    }


    public static List<LatLng> getMultipleLocation(){

        List<LatLng> latLngs=new ArrayList<>();
        latLngs.add(new LatLng(23.074497, 72.526678));
        latLngs.add(new LatLng(23.074783, 72.523470));
        latLngs.add(new LatLng(23.073480, 72.525482));
        latLngs.add(new LatLng(23.078840, 72.524870));
        latLngs.add(new LatLng(23.081258, 72.526222));

        return latLngs;
    }
}
