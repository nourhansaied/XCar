package com.victoria.driver.ui.authentication.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.ui.model.VehicleList
import kotlinx.android.synthetic.main.row_car_types.view.*

class CarTypesAdapter(var carTypeList: ArrayList<VehicleList>, var context: Context,
                      var callBackForCarType: CallBackForCarTypeSelect?) : RecyclerView.Adapter<CarTypesAdapter.ViewHolderCarType>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCarType {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.row_car_types, parent, false)
        return ViewHolderCarType(view)
    }

    override fun getItemCount(): Int {
        return carTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolderCarType, position: Int) {
        holder.containerView.textViewCarType.text = carTypeList[position].vehicle
        holder.containerView.textViewCarType.setOnClickListener { callBackForCarType?.carSelectCallBack(position) }
    }

    class ViewHolderCarType(val containerView: View) : RecyclerView.ViewHolder(containerView) {

        init {

        }

    }

    public interface CallBackForCarTypeSelect {
        public fun carSelectCallBack(position: Int) {}
    }
}
