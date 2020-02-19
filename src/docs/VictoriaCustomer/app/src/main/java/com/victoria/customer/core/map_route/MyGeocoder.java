package com.victoria.customer.core.map_route;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.victoria.customer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyGeocoder {


    private Context context;

    @Inject
    public MyGeocoder(Context context) {
        this.context = context;
    }

    //Method will contact the Google Geocoder API and return a JSONObject containing
    //a list of Address data
    //https://developers.google.com/maps/documentation/geocoding/
    //Method may return null


    public String getLocationInfo(double latitude, double longitude) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        String coords = (float) latitude + "," + (float) longitude;
        String getUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + coords + "&key=" + context.getResources().getString(R.string.browser_key);
        URL url = null;
        Log.e("URL ", getUrl + "");

        HttpURLConnection urlConnection = null;
        BufferedReader br = null;

        StringBuilder stringBuilder = null;
        int statusCode = 0;


        try {
            url = new URL(getUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            // set the connection timeout to 10 seconds and the read timeout to 120 seconds
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(120 * 1000);
            urlConnection.connect();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

            statusCode = urlConnection.getResponseCode();

            InputStreamReader streamReader = new InputStreamReader(stream);
            br = new BufferedReader(streamReader);

            String line;
            stringBuilder = new StringBuilder();

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //close buffered reader and under lying stream
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

       Log.d("MyGeocoder" , "statusCode = " + statusCode  +" HTTPStatus.SC_OK = " + HttpURLConnection.HTTP_OK);

        //create json object from result string and then parse it
        if (statusCode == HttpURLConnection.HTTP_OK) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.d("MyGeocoder" ,"JSONException e = " + e.getMessage());
                e.printStackTrace();
            }

            return parseJSONAddressList(jsonObject);
        } else {
            return "";
        }
    }


    //Method will parse JSONObject and return a List<Address> containing one Address.
//The Address will only contain data for county, state and country.
    private String parseJSONAddressList(JSONObject jsonObject) {


        boolean flag = false;
        String formattedAddress;

        if (jsonObject != null)

            try {

                String Status = jsonObject.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObject.getJSONArray("results");

                    JSONObject zero1 = Results.getJSONObject(0);
                    JSONArray address_components1 = zero1.getJSONArray("address_components");
                    formattedAddress = zero1.getString("formatted_address");


                   /* for (int k = 0; k < Results.length(); k++) {
                        JSONObject zero = Results.getJSONObject(k);
                        JSONArray address_components = zero.getJSONArray("address_components");
                        //formattedAddress = zero.getString("formatted_address");
                        placeId = zero1.getString("place_id");

                        Log.e("PlaceId ", placeId + "");

                        for (int i = 0; i < address_components.length(); i++) {
                            JSONObject zero2 = address_components.getJSONObject(i);
                            String long_name = zero2.getString("long_name");
                            JSONArray mtypes = zero2.getJSONArray("types");
                            //String Type = mtypes.getString(0);


                            for (int j = 0; j < mtypes.length(); j++) {
                                if (!TextUtils.isEmpty(long_name) || long_name != null || long_name.length() > 0 || long_name != "") {


                         *//**//*   if (mtypes.get(j).toString().equalsIgnoreCase("sublocality")) {
                                locality = long_name;
                                Log.e("Locality ",locality+"");
                                flag=true;
                                break;
                            }else if(locality.equalsIgnoreCase("")) {*//**//*
                                    if (mtypes.get(j).toString().equalsIgnoreCase("locality")) {
                                        locality = long_name;
                                        Log.e("Locality ", locality + "");
                                        flag = true;
                                        break;
                                    }
                                    //}
                                }


                            }


                            if (flag) {

                                break;
                            }

                        }



                    }

*/
                    return formattedAddress ;


                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        return "";
    }
}