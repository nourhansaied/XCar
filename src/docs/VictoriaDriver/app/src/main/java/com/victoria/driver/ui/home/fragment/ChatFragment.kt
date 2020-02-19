package com.victoria.driver.ui.home.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.victoria.driver.R
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.adapter.ChatAdapter
import com.victoria.driver.ui.model.ChatModel
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseFragment() {

    lateinit var chatAdapter: ChatAdapter
    lateinit var chatItems: ArrayList<ChatModel>

    override fun createLayout(): Int = R.layout.fragment_chat

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        setAdapterForChatting()
        recyclerViewChatting.scrollToPosition(chatItems.size - 1)
        imageViewBack.setOnClickListener { onViewClick(it) }
        textViewSend.setOnClickListener { onViewClick(it) }
    }


    private fun setAdapterForChatting() {
        chatItems = ArrayList()

        chatItems.add(ChatModel(getString(R.string.dummy_strring), "3.33 PM", true))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "3.34 PM", false))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "3.36 PM", true))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "3.38 PM", false))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "3.43 PM", true))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "3.53 PM", false))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "4.00 PM", true))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "5.33 PM", false))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "6.33 PM", true))
        chatItems.add(ChatModel(getString(R.string.dummy_strring), "7.33 PM", false))

        recyclerViewChatting.layoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(this.context!!, chatItems)
        recyclerViewChatting.adapter = chatAdapter
    }


    override fun onViewClick(view: View) {

        when (view.id) {
            R.id.textViewSend -> {
                if (!editTextChatText.text.toString().trim()?.isEmpty()) {
                    chatItems.add(ChatModel(editTextChatText.text.toString().trim(), "8.00 PM", false))
                    recyclerViewChatting.scrollToPosition(chatItems.size - 1)
                    editTextChatText.setText("")
                }

            }
            R.id.imageViewBack -> {
                navigator.goBack()
            }
        }
    }
}