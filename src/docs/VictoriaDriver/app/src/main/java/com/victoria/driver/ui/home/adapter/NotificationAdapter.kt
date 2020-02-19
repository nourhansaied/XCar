package com.victoria.driver.ui.home.adapter

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.NotificationData
import com.victoria.driver.util.DateTimeFormatter
import com.victoria.driver.util.advance_adapter.adapter.AdvanceRecycleViewAdapter
import com.victoria.driver.util.advance_adapter.base.BaseHolder


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

    inner class ViewHolder(viewMain: View) : BaseHolder<NotificationData>(view) {
        var textViewNotificationHeading = viewMain.findViewById(R.id.textViewNotificationHeading) as AppCompatTextView
        var textViewNotificationDescription = viewMain.findViewById(R.id.textViewNotificationDescription) as AppCompatTextView
        var textViewNotificationTime = viewMain.findViewById(R.id.textViewNotificationTime) as AppCompatTextView

    }
}
