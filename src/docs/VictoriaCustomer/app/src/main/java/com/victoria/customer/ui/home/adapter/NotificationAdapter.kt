package com.victoria.customer.ui.home.adapter


import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.NotificationData
import com.victoria.customer.util.DateTimeFormatter
import com.victoria.customer.util.advance_adapter.adapter.AdvanceRecycleViewAdapter
import com.victoria.customer.util.advance_adapter.base.BaseHolder


/**
 * Created  on 3/10/16.
 */

class NotificationAdapter(private val context: Context, private var eList: List<NotificationData>)
    : AdvanceRecycleViewAdapter<NotificationAdapter.ViewHolder, NotificationData>() {


    override fun createDataHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        view = View.inflate(context, R.layout.row_notification_item_layout, null)
        return ViewHolder(view)

    }

    override fun onBindDataHolder(holder: ViewHolder?, position: Int, notificationData: NotificationData?) {
        if (notificationData?.type.toString().contains("_")) {
            holder?.textViewNotificationHeading?.text = notificationData?.type.toString().replace("_", " ")
        } else {
            holder?.textViewNotificationHeading?.text = notificationData?.type.toString()
        }

        holder?.textViewNotificationDescription?.text = notificationData?.content
        holder?.textViewNotificationTime?.text = DateTimeFormatter.utcToLocal(notificationData?.insertdDate, Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_OUT_THREE)

    }

    lateinit var view: View

    public inner class ViewHolder(viewMain: View) : BaseHolder<NotificationData>(view) {
        var textViewNotificationHeading = viewMain.findViewById<AppCompatTextView>(R.id.textViewNotificationHeading)
        var textViewNotificationDescription = viewMain.findViewById<AppCompatTextView>(R.id.textViewNotificationDescription)
        var textViewNotificationTime = viewMain.findViewById<AppCompatTextView>(R.id.textViewNotificationTime)
    }
}
