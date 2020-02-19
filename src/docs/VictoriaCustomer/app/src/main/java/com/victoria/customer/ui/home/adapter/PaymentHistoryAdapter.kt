package com.victoria.customer.ui.home.adapter


import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.libraries.places.internal.it
import com.victoria.customer.R
import com.victoria.customer.core.Common.DATEFORMATONE
import com.victoria.customer.core.Common.DATEFORMATTWO
import com.victoria.customer.core.Common.DATEFORMATUTC
import com.victoria.customer.data.pojo.PaymentHistory
import com.victoria.customer.util.advance_adapter.adapter.AdvanceRecycleViewAdapter
import com.victoria.customer.util.advance_adapter.base.BaseHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_transaction_history_item.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created  on 3/10/16.
 */

class PaymentHistoryAdapter(private val context: Context, private val paymentHistoryList: List<PaymentHistory>) :
        AdvanceRecycleViewAdapter<PaymentHistoryAdapter.PaymentHistoryDataHolder, PaymentHistory>() {

    private var dateAndTimeBoth: String = ""
    var preDateTime = ""

    override fun createDataHolder(parent: ViewGroup?, viewType: Int): PaymentHistoryDataHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.row_transaction_history_item, parent, false)
        return PaymentHistoryDataHolder(view)
    }


    override fun onBindDataHolder(holder: PaymentHistoryDataHolder?, position: Int, item: PaymentHistory?) {
        if (isShowDate(position)) {
            holder?.textViewTransactionDate?.visibility = View.VISIBLE
        } else {
            holder?.textViewTransactionDate?.visibility = View.GONE
        }
        holder?.textViewTransactionId?.text = context.getString(R.string.dummy_transaction_id_1234567890) + item?.id.toString()
        if (item?.status.equals("credited")) {
            holder?.textViewTrasactionAmount?.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder?.textViewTrasactionAmount?.text = "+ " + context.getString(R.string.label_curruncy_sar) + " " + item?.amount.toString()
        } else {
            holder?.textViewTrasactionAmount?.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            holder?.textViewTrasactionAmount?.text = "- " + context.getString(R.string.label_curruncy_sar) + " " + item?.amount.toString()
        }
        holder?.textViewTransactionStatus?.text = item?.description.toString()
        dateAndTimeBoth = item?.wallethistoryDate?.let { utcToLocal(it, DATEFORMATUTC, DATEFORMATONE) }.toString()
        dateAndTimeBoth = item?.wallethistoryDate?.let { utcToLocal(it, DATEFORMATUTC, DATEFORMATONE) }.toString()
        holder?.textViewTransactionDate?.text = dateAndTimeBoth.split("-")[0]
        holder?.textViewTransactionTime?.text = item?.wallethistoryDate?.let { utcToLocal(it, DATEFORMATUTC, DATEFORMATTWO) }.toString()
        /* holder?.textViewRideWith?.text = "Ride with " + item?.firstName + " " + item?.lastName
         holder?.textViewTransactionId?.text = item?.transactionId
         holder?.textViewTotalPositive?.text = "+ $" + item?.driverEarning.toString()

         if (isShowLine(position)) {
             holder?.view?.visibility = View.GONE

         } else {
             holder?.view?.visibility = View.VISIBLE
         }

         holder?.textViewTime?.text = dateAndTimeBoth.split("-")[1]*/
        /*if (paymentHistoryList[position].status.equals("completed")) {
            holder.textViewTotalPositive.visibility = View.VISIBLE
            holder.textViewTotalNegative.visibility = View.GONE
        } else {
            holder.textViewTotalPositive.visibility = View.GONE
            holder.textViewTotalNegative.visibility = View.VISIBLE
        }*/
    }

    class PaymentHistoryDataHolder(override val containerView: View) : BaseHolder<PaymentHistory>(containerView),
            LayoutContainer {
        var textViewTransactionTime = containerView.findViewById<AppCompatTextView>(R.id.textViewTransactionTime)
        var textViewTransactionId = containerView.findViewById<AppCompatTextView>(R.id.textViewTransactionId)
        var textViewTrasactionAmount = containerView.findViewById<AppCompatTextView>(R.id.textViewTrasactionAmount)
        var textViewTransactionStatus = containerView.findViewById<AppCompatTextView>(R.id.textViewTransactionStatus)
        var textViewTransactionDate = containerView.findViewById<AppCompatTextView>(R.id.textViewTransactionDate)

        init {

        }
    }

    private fun isShowDate(position: Int): Boolean {
        val isShow: Boolean
        isShow = if (position == 0) {
            true
        } else {
            val nextText = paymentHistoryList[position - 1].wallethistoryDate.split(" ")[0]
            val curranText = paymentHistoryList[position].wallethistoryDate.split(" ")[0]
            !curranText.equals(nextText, ignoreCase = true)
        }
        return isShow
    }

    private fun isShowLine(position: Int): Boolean {
        val isShow: Boolean
        isShow = if (position == paymentHistoryList.size - 1) {
            true
        } else {
            val nextText = paymentHistoryList[position + 1].wallethistoryDate
            val curranText = paymentHistoryList[position].wallethistoryDate
            !curranText.equals(nextText, ignoreCase = true)
        }
        return isShow
    }

    fun utcToLocal(dateString: String, inputFormat: String, outputFormat: String): String {
        var simpleDateFormat = SimpleDateFormat(inputFormat)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC") // TimeZone.getDefault()
        var date: Date? = null
        var dateAsString = ""
        var dateS: String? = null
        try {
            date = simpleDateFormat.parse(dateString);
            val simple = SimpleDateFormat(outputFormat, Locale.ENGLISH)
            val utcZone = TimeZone.getDefault() // TimeZone.getTimeZone("UTC");
            simple.timeZone = utcZone
            dateS = simple.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateS.toString()
    }
}
