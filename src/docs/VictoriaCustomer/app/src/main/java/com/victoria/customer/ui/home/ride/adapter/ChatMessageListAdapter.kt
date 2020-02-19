package com.victoria.customer.ui.home.ride.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.twilio.chat.Message
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Session
import com.victoria.customer.util.CircleTransform
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_message_receiver_listing.*
import kotlinx.android.synthetic.main.row_message_sender_listing.*
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChatMessageListAdapter(private val context: Context
                             , private val chatList: ArrayList<Message>, var session: Session) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_SENDER = 0
    private val TYPE_RECEIVER = 1
    var jsonObject: JSONObject? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        return if (viewType == TYPE_SENDER) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.row_message_sender_listing, parent, false)
            ChatMessageListAdapter.ChatViewHolderSender(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.row_message_receiver_listing, parent, false)
            ChatMessageListAdapter.ChatViewHolderReceiver(view)
        }
    }


    override fun getItemViewType(position: Int): Int {
        var sender_id = ""
        var type = ""

        try {
            val jsonObject = JSONObject(chatList.get(position).getAttributes().toString())
            sender_id = jsonObject.getString("sender_id")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        if (sender_id.equals(session.user?.userType + session.user?.customerId.toString(), ignoreCase = true)) {
            if (!chatList[position].hasMedia()) {
                jsonObject = null
                try {
                    jsonObject = JSONObject(chatList[position].attributes.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                type = TYPE_SENDER.toString()
            }
        } else {
            if (!chatList[position].hasMedia()) {
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(chatList[position].attributes.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                type = TYPE_RECEIVER.toString()
            }
        }
        return type.toInt()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_SENDER) {
            val senderViewHolder = holder as ChatViewHolderSender
            senderViewHolder.textViewSenderMessage.text = chatList[position].messageBody
            senderViewHolder.textViewDate.text = getDate(chatList.get(position).timeStampAsDate.time)

        } else {
            val receiverViewHolder = holder as ChatViewHolderReceiver
            receiverViewHolder.textViewReceiverMessage.text = chatList[position].messageBody
            receiverViewHolder.textViewDateRec.text = getDate(chatList.get(position).timeStampAsDate.time)

        }
    }

    class ChatViewHolderReceiver(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            var textViewReceiver = containerView.findViewById<AppCompatTextView>(R.id.textViewReceiverMessage)
            var imageViewProfile = containerView.findViewById<AppCompatImageView>(R.id.imageViewProfile)
        }
    }

    class ChatViewHolderSender(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            var textViewSenderMessage = containerView.findViewById<AppCompatTextView>(R.id.textViewSenderMessage)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun getDate(time: Long): String {
        var cal = Calendar.getInstance()
        var tz = cal.getTimeZone()
        var sdf = SimpleDateFormat(Common.DATE_TIME_FORMAT_TIMER)
        sdf.setTimeZone(tz);//set time zone.
        var localTime = sdf.format(Date(time))
        var date = Date()
        try {
            date = sdf.parse(localTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return localTime
    }
}
