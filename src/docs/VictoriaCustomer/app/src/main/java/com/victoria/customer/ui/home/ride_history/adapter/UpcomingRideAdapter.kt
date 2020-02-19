package com.victoria.customer.ui.home.ride_history.adapter


import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.PastRide
import com.victoria.customer.ui.interfaces.ItemClickListener
import com.victoria.customer.util.DateTimeFormatter
import com.victoria.customer.util.advance_adapter.adapter.AdvanceRecycleViewAdapter
import com.victoria.customer.util.advance_adapter.base.BaseHolder


/**
 * Created  on 3/10/16.
 */

class UpcomingRideAdapter(private val context: Context?,
                          private var eList: List<PastRide>, itemClickListener: ItemClickListener) :
        AdvanceRecycleViewAdapter<UpcomingRideAdapter.ViewHolder, PastRide>() {

    override fun createDataHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        view = View.inflate(context, R.layout.row_my_ride_item_layout, null)
        return ViewHolder(view)
    }

    lateinit var view: View
    var itemClickListener: ItemClickListener = itemClickListener


    override fun onBindDataHolder(holder: ViewHolder?, position: Int, upcomingRide: PastRide?) {
        if (upcomingRide != null) {
            holder?.imageViewStatusCompleted?.visibility = View.INVISIBLE
            holder?.textViewCancel?.visibility = View.VISIBLE
            holder?.textViewTotalCost?.visibility = View.GONE

            holder?.textViewRideDateTime?.text = DateTimeFormatter.utcToLocal(upcomingRide.tripdatetime,
                    Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_OUT_THREE)
            holder?.textViewPickup?.text = upcomingRide.pickupAddress
            holder?.textViewDropoff?.text = upcomingRide.dropoffAddress
            if (context != null) {
                holder?.textViewTotalCost?.text = context.getString(R.string.label_curruncy_sar) + " " + upcomingRide.totalAmount
            }

            holder?.constraintLayoutRoot?.setOnClickListener {
                itemClickListener.onItemEventFired("", position)

            }
        }
    }

    inner class ViewHolder(viewH: View) : BaseHolder<PastRide>(view) {
        var imageViewStatusCompleted = viewH.findViewById<AppCompatImageView>(R.id.imageViewStatusCompleted)
        var textViewCancel = viewH.findViewById<AppCompatTextView>(R.id.textViewCancel)
        var textViewTotalCost = viewH.findViewById<AppCompatTextView>(R.id.textViewTotalCost)
        var textViewRideDateTime = viewH.findViewById<AppCompatTextView>(R.id.textViewRideDateTime)
        var textViewPickup = viewH.findViewById<AppCompatTextView>(R.id.textViewPickup)
        var textViewDropoff = viewH.findViewById<AppCompatTextView>(R.id.textViewDropoff)
        var constraintLayoutRoot = viewH.findViewById<ConstraintLayout>(R.id.constraintLayoutRoot)

    }
}
