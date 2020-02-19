package com.victoria.driver.util;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CityAsyncTask extends AsyncTask<String, String, String> {
    Activity act;
    double latitude;
    double longitude;

    public CityAsyncTask(Activity act, double latitude, double longitude) {
        // TODO Auto-generated constructor stub
        this.act = act;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            //Log.e("Addresses", "-->" + addresses);
            if (!addresses.isEmpty())
                result = addresses.get(0).getAddressLine(0)/* + "" + addresses.get(0).getAddressLine(1) + addresses.get(0).getAddressLine(2)*/;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

    }
}