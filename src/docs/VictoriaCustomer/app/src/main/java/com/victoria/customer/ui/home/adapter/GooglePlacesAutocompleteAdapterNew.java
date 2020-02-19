package com.victoria.customer.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.victoria.customer.R;
import com.victoria.customer.model.PlaceModel;

import java.util.List;


public class GooglePlacesAutocompleteAdapterNew extends BaseAdapter {


    private final Context applicationContext;
    private final List<PlaceModel> placeModels;

    public GooglePlacesAutocompleteAdapterNew(Context applicationContext, List<PlaceModel> placeModels) {
        this.applicationContext = applicationContext;
        this.placeModels = placeModels;
    }

    @Override
    public int getCount() {
        return placeModels.size();
    }

    @Override
    public PlaceModel getItem(int i) {
        return placeModels.get(i);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PlaceViewHolder viewholder;


        if (view==null){
            view = LayoutInflater.from(applicationContext).inflate(R.layout.row_place_auto_search_layout, parent, false);
            viewholder=new PlaceViewHolder(view);
            view.setTag(viewholder);
        }
        viewholder= (PlaceViewHolder) view.getTag();
        viewholder.txtArea.setText(placeModels.get(position).subTitle);
        viewholder.txtTitle.setText(placeModels.get(position).heading);
//        viewholder.txtArea.setText(placeModels.get(position).getSecondaryTitle());
        return view;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    private class PlaceViewHolder {
        TextView txtTitle;
        TextView txtArea;
        public PlaceViewHolder(View view) {
            txtArea = (TextView) view.findViewById(R.id.textViewHeading);
            txtTitle = (TextView) view.findViewById(R.id.textViewSubTitle);
        }
    }
}
