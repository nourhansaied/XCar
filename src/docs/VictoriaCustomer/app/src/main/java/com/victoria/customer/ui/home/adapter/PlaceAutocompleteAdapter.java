package com.victoria.customer.ui.home.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.victoria.customer.R;
import com.victoria.customer.core.Common;
import com.victoria.customer.model.PlaceModel;
import com.victoria.customer.ui.interfaces.ItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created on 1/12/18.
 */
public class PlaceAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "";
    public List<PlaceModel> resultList;
    ItemClickListener itemClickListener;
    private Context context = null;
    SharedPreferences sharedPreferences;

    PlaceModel placeModel;
    AppCompatTextView name;
    AppCompatTextView release;
    ConstraintLayout constraintLayout;

    public PlaceAutocompleteAdapter(Context context, int textViewResourceId, List<PlaceModel> model, ItemClickListener itemClickListener) {
        super(context, textViewResourceId);
        this.context = context;
        this.resultList = model;
        this.itemClickListener = itemClickListener;

    }


    @Override
    public int getCount() {
        if (resultList != null)
            return resultList.size();
        else
            return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.row_place_auto_search_layout, parent, false);

        name = (AppCompatTextView) listItem.findViewById(R.id.textViewHeading);
        release = (AppCompatTextView) listItem.findViewById(R.id.textViewSubTitle);
        constraintLayout = (ConstraintLayout) listItem.findViewById(R.id.constraintLayout);
        if (resultList != null && resultList.size() > 0) {
            try {
                name.setText(resultList.get(position).heading);
                release.setText(resultList.get(position).subTitle);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultList != null && resultList.size() > 0) {
                    itemClickListener.onItemEventFired(resultList.get(position).heading, position);
                }
            }
        });


        return listItem;
    }

    @Override
    public PlaceModel getItem(int index) {
        return resultList.get(index);
    }


    public ArrayList<PlaceModel> autocomplete(String input) {
        ArrayList<PlaceModel> resultList = null;
        ArrayList<PlaceModel> descriptionList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + context.getString(R.string.browser_key));
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            sb.append("&location=" + String.valueOf(Common.latitudeCurrent) + "," + String.valueOf(Common.longitudeCurrent));
            sb.append("&radius=" + String.valueOf(50));
            sharedPreferences = context.
                    getSharedPreferences(Common.LANGUAGE_SELECTION
                            , Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean(Common.WHICH_LANGUAGE, false)) {
                sb.append("&language=ar");
            } else {
                sb.append("&language=en");
            }


            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            Log.d("yo", jsonResults.toString());
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            descriptionList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(new PlaceModel(predsJsonArray.getJSONObject(i).toString(), predsJsonArray.getJSONObject(i).getString("description"), predsJsonArray.getJSONObject(i).getString("place_id")));
                descriptionList.add(new PlaceModel(predsJsonArray.getJSONObject(i).getString("description"), predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").getString("secondary_text"), predsJsonArray.getJSONObject(i).getString("place_id")));
            }

//            .toArray(new String[resultList.size()])
            return (descriptionList);
        } catch (JSONException e) {
            Log.e("", "Cannot process JSON results", e);
        }

        return descriptionList;
    }


    public List<PlaceModel> getResultList() {
        return resultList;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    //setImageVisibility();
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}