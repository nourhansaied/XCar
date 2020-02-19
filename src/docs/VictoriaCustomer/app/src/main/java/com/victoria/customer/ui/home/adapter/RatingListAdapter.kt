package com.victoria.customer.ui.home.adapter


import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.victoria.customer.R
import com.victoria.customer.data.pojo.NotificationData
import com.victoria.customer.data.pojo.RateReviewData
import com.victoria.customer.util.advance_adapter.adapter.AdvanceRecycleViewAdapter
import com.victoria.customer.util.advance_adapter.base.BaseHolder


/**
 * Created  on 3/10/16.
 */

class RatingListAdapter(private val context: Context, private var eList: List<RateReviewData>)
    : AdvanceRecycleViewAdapter<RatingListAdapter.ViewHolder, RateReviewData>() {


    override fun createDataHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        view = View.inflate(context, R.layout.row_my_rating_list, null)
        return ViewHolder(view)

    }

    override fun onBindDataHolder(holder: ViewHolder?, position: Int, notificationData: RateReviewData?) {

        holder?.textViewUserName?.text = notificationData?.userName
        holder?.textViewGivenRatingComment?.text = notificationData?.comment
        holder?.textViewGivenRating?.text=notificationData?.rate.toString()

        Glide.with(context!!)
                .load(notificationData?.profileImageThumb)
                .apply(RequestOptions().circleCrop())
                .into(holder?.imageViewUserProfile!!)
    }

    lateinit var view: View

    public inner class ViewHolder(viewMain: View) : BaseHolder<NotificationData>(view) {
        var imageViewUserProfile = viewMain.findViewById(R.id.imageViewUserProfile) as AppCompatImageView
        var textViewUserName = viewMain.findViewById(R.id.textViewUserName) as AppCompatTextView
        var textViewGivenRatingComment = viewMain.findViewById(R.id.textViewGivenRatingComment) as AppCompatTextView
        var textViewGivenRating = viewMain.findViewById(R.id.textViewGivenRating) as AppCompatTextView


    }
}
