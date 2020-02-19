package com.victoria.customer.ui.home.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.victoria.customer.R
import com.victoria.customer.model.VictoriaVehicleData
import com.victoria.customer.ui.interfaces.ItemEventListener
import kotlinx.android.synthetic.main.row_service_item_layout.view.*


/**
 * Created  on 3/10/16.
 */

class BottomSheetAdapter(private val context: Context?, private val eList: List<VictoriaVehicleData>, private val stringItemEventListener: ItemEventListener?) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(context).inflate(R.layout.row_service_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomSheetAdapter.ViewHolder, position: Int) {

        holder.bind(eList[position])

    }


    override fun getItemCount(): Int {
        return eList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(service: VictoriaVehicleData) {

            itemView.textViewCarType.text = service.vehicle
            itemView.textViewPrice.text = "EGP" + " " + (String.format("%.2f", service.baseFare.toFloat()))

            Picasso.get()
                    .load(service.vehicleThumbUnselectedimage)
                    .into(itemView.imageViewCar)

            //itemView.imageViewCar.setImageResource(service.vehicleThumbUnselectedimage)

            itemView.constraintLayoutRoot!!.setOnClickListener {
                stringItemEventListener?.onItemEventFired(eList[adapterPosition], adapterPosition)
                // mBottomSheetDialog.dismiss();
            }
        }
    }
}
