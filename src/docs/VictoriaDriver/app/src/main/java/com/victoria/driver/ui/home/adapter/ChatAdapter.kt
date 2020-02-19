package com.victoria.driver.ui.home.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.ui.model.ChatModel
import kotlinx.android.synthetic.main.row_chat_items.view.*
import kotlinx.android.synthetic.main.row_chat_user_side_items.view.*

class ChatAdapter(var context: Context, val chatList: List<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var USER = 1
    private var SENDER = 2

    override fun getItemViewType(position: Int): Int {
        return if (!chatList[position].isUser) {
            USER
        } else {
            SENDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        return if (viewType == USER) {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_user_side_items, parent, false)
            ChatUserViewHolder(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.row_chat_items, parent, false)
            ChatViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == USER) {
            val chatUserViewHolder = holder as ChatUserViewHolder
            chatUserViewHolder.itemView.textViewUserText.text = chatList[position].textMsg
            chatUserViewHolder.itemView.textViewTime.text = chatList[position].time
        } else {
            val chatViewHolder = holder as ChatViewHolder
            chatViewHolder.itemView.textViewSenderText.text = chatList[position].textMsg
            chatViewHolder.itemView.textViewSenderTime.text = chatList[position].time
        }
    }


}

class ChatUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {

    }
}

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {

    }
}
