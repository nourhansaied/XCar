package com.victoria.driver.ui.home.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.PastRide
import com.victoria.driver.ui.interfaces.CallbackHistory
import com.victoria.driver.util.DateTimeFormatter
import com.victoria.driver.util.advance_adapter.adapter.AdvanceRecycleViewAdapter
import com.victoria.driver.util.advance_adapter.base.BaseHolder


/**
 * Created  on 3/10/16.
 */

class UpcomingRideAdapter(private val context: Context, private var eList: List<PastRide>,
                          itemClickListener: CallbackHistory) :
        AdvanceRecycleViewAdapter<UpcomingRideAdapter.ViewHolder, PastRide>() {

    lateinit var view: View

    override fun createDataHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        view = View.inflate(context, R.layout.row_my_ride_item_layout, null)
        return ViewHolder(view)
    }

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
                itemClickListener.onOpenDetailsScreen(position)

            }
        }
    }

    public inner class ViewHolder(viewH: View) : BaseHolder<PastRide>(view) {
        var imageViewStatusCompleted = viewH.findViewById<AppCompatImageView>(R.id.imageViewStatusCompleted)
        var textViewCancel = viewH.findViewById<AppCompatTextView>(R.id.textViewCancel)
        var textViewTotalCost = viewH.findViewById<AppCompatTextView>(R.id.textViewTotalCost)
        var textViewRideDateTime = viewH.findViewById<AppCompatTextView>(R.id.textViewRideDateTime)
        var textViewPickup = viewH.findViewById<AppCompatTextView>(R.id.textViewPickup)
        var textViewDropoff = viewH.findViewById<AppCompatTextView>(R.id.textViewDropoff)
        var constraintLayoutRoot = viewH.findViewById<ConstraintLayout>(R.id.constraintLayoutRoot)
    }

    var itemClickListener: CallbackHistory = itemClickListener
}
