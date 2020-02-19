package com.victoria.driver.ui.home.fragment


import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import com.twilio.accessmanager.AccessManager
import com.twilio.chat.*
import com.victoria.customer.data.URLFactory
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.AccessData
import com.victoria.driver.di.App
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.activity.HomeActivity
import com.victoria.driver.ui.home.adapter.ChatMessageListAdapter
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.ui.viewmodel.HomeViewModel
import com.victoria.driver.util.CircleTransform
import kotlinx.android.synthetic.main.fragment_chat_inside.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Member
import java.util.*
import kotlin.collections.ArrayList


open class ChatMessageListingFragment : BaseFragment(), ChannelListener, SwipeRefreshLayout.OnRefreshListener {

    private var linearLayoutManager: LinearLayoutManager? = null
    var chatList = ArrayList<Message>()
    var newChannel: Channel? = null

    private var chatListingAdapter: ChatMessageListAdapter? = null

    internal var bundle: Bundle? = null

    private val viewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun createLayout(): Int {
        return R.layout.fragment_chat_inside
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        hideKeyBoard()

        linearLayoutManager = LinearLayoutManager(context)
        setAdapter()

        setDataFromBundle()

        getMessageFromGlobalChatClient()
        //recyclerViewMessageListing.smoothScrollToPosition(FakeDataList.getChatList(context).size - 1)
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        imageViewBack.setOnClickListener { onViewClick(it) }
        imageViewSend.setOnClickListener { onViewClick(it) }
        imageViewProfile.setOnClickListener { onViewClick(it) }
        swipeRefresh.setOnRefreshListener(this)
        //loadImagePicasso(getString(R.string.user_image_one), true, imageViewProfile, false)

    }

    private fun setDataFromBundle() {
        Picasso.get()
                .load(App.getTripData().customerData.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewProfile)
        textViewUserName.text = App.getTripData().customerData.firstName + " " + App.getTripData().customerData.lastName
    }

    private fun setAdapter() {
        chatListingAdapter = ChatMessageListAdapter(this.context!!, chatList, session)
        recyclerViewMessageListing.apply {
            adapter = chatListingAdapter
            layoutManager = linearLayoutManager
        }

        recyclerViewMessageListing.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if (bottom < oldBottom) {
                    recyclerViewMessageListing.postDelayed({
                        recyclerViewMessageListing.scrollToPosition(chatList.size - 1)
                    }, 100)
                }
            }
        })
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.imageViewSend -> {
                if (editTextMessage.text!!.toString().trim { it <= ' ' } != "") {
                    if (newChannel != null && newChannel!!.messages != null) {
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put(Common.SENDER_ID, session.user?.userType + session.user?.driverId)
                            jsonObject.put(Common.PROFILE, session.user?.profileImage)
                            jsonObject.put(Common.NAME, session.user?.firstName + " " + session.user?.lastName)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        newChannel!!.messages.sendMessage(
                                Message.options()
                                        .withAttributes(jsonObject)
                                        .withBody(editTextMessage.text!!.toString().trim { it <= ' ' }),
                                object : CallbackListener<Message>() {
                                    override fun onSuccess(message: Message) {
                                        if (getView() != null) {
                                            editTextMessage.setText("")
                                        }
                                    }
                                })
                    }
                }
            }

            R.id.imageViewBack -> {
                navigator.goBack()
            }
        }
    }

    private fun createChatRoom() {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        val properties = ChatClient.Properties.Builder().createProperties()
        startChatting()
    }

    private fun startChatting() {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        HomeActivity.newChatClient?.channels?.getChannel(App.getTripData().channelSid, object : CallbackListener<Channel>() {
            override fun onSuccess(channel: Channel?) {
                if (channel != null) {
                    newChannel = channel
                    newChannel?.join(object : StatusListener() {
                        override fun onSuccess() {
                            navigator.toggleLoader(false)
                            newChannel!!.addListener(this@ChatMessageListingFragment)
                            fetchMessage()
                        }
                    })
                }
            }

            override fun onError(errorInfo: ErrorInfo?) {
                navigator.toggleLoader(false)
            }
        })
    }

    fun onMemberDeleted(p0: Member?) {

    }

    fun onTypingEnded(p0: Member?) {

    }

    override fun onMessageAdded(message: Message?) {
        if (view != null && chatListingAdapter != null) {
            message?.let { chatList.add(it) }
            chatListingAdapter!!.notifyDataSetChanged()

            //for unread message count

            newChannel?.getMessages()?.lastConsumedMessageIndex = chatList.get(chatList.size - 1).messageIndex
            recyclerViewMessageListing.layoutManager = linearLayoutManager
            recyclerViewMessageListing.scrollToPosition(chatList.size - 1)
        }
    }


    private fun getMessageFromGlobalChatClient() {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        if (HomeActivity.newChatClient != null) {
            HomeActivity.newChatClient?.channels?.getChannel(App.getTripData().channelSid, object : CallbackListener<Channel>() {
                override fun onSuccess(channel: Channel?) {
                    if (channel != null) {
                        newChannel = channel
                        newChannel!!.join(object : StatusListener() {
                            override fun onSuccess() {
                                navigator.toggleLoader(false)
                                newChannel?.addListener(this@ChatMessageListingFragment)
                                fetchMessage()
                            }
                        })
                    }
                }

                override fun onError(errorInfo: ErrorInfo?) {}
            })
        } else {
            observeResponseForAccessToken()
        }
    }

    public fun observeResponseForAccessToken() {
        //identity,user_type[Customer,Driver],device_type[A,I],device_token
        val parameter = Parameter()
        parameter.identity = session.user?.driverId.toString()
        parameter.user_type = session.user?.userType.toString()
        parameter.devieType = "A"
        parameter.deviceToken = session.deviceId
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.generateAccesstoken.value = null
        viewModel.generateAccesstoken(parameter)
        viewModel.generateAccesstoken.observe(this, { responseBody ->
            viewModel.generateAccesstoken.removeObservers(this)
            navigator.toggleLoader(false)
            if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
                responseBody.data?.let { createChatClient(it) }
            }
        })
    }

    public fun createChatClient(accessTokenData: AccessData) {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        val accessManager = AccessManager(accessTokenData.token, object : AccessManager.Listener {
            override fun onTokenWillExpire(accessManager: AccessManager) {

            }

            override fun onTokenExpired(accessManager: AccessManager) {
                observeResponseForAccessToken()
            }

            override fun onError(accessManager: AccessManager, s: String) {

            }
        })

        val properties = ChatClient.Properties.Builder().createProperties()
        context?.let {
            ChatClient.create(it, accessTokenData.token, properties, object : CallbackListener<ChatClient>() {
                override fun onSuccess(chatClient: ChatClient) {
                    HomeActivity.newChatClient = chatClient
                    HomeActivity?.newChatClient!!.registerFCMToken(session.deviceId, object : StatusListener() {
                        override fun onSuccess() {
                            navigator.toggleLoader(false)
                            getMessageFromGlobalChatClient()
                        }
                    })
                }

                override fun onError(errorInfo: ErrorInfo?) {

                }
            })
        }
    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = false
        if (newChannel != null) {
            if (chatList.size > 0) {
                if (chatList[0].messageIndex > 0) {
                    newChannel!!.messages.getMessagesBefore(chatList[0].messageIndex - 1,
                            20, object : CallbackListener<List<Message>>() {
                        override fun onSuccess(messages: List<Message>) {
                            if (view != null) {
                                Collections.reverse(messages)
                                if (swipeRefresh.isRefreshing) {
                                    swipeRefresh.isRefreshing = false
                                }
                                for (message in messages) {
                                    chatList.add(0, message)
                                }
                                chatListingAdapter?.notifyDataSetChanged()
                            } else {
                                if (swipeRefresh.isRefreshing) {
                                    swipeRefresh.isRefreshing = false
                                }
                            }
                        }
                    })
                } else {
                    if (swipeRefresh.isRefreshing) {
                        swipeRefresh.isRefreshing = false
                    }
                }
            }
        }
    }

    fun fetchMessage() {
        if (newChannel != null) {
            if (swipeRefresh != null) {
                if (swipeRefresh.isRefreshing) {
                    swipeRefresh.isRefreshing = false
                }
            }

            newChannel?.messages?.getLastMessages(20, object : CallbackListener<List<Message>>() {
                override fun onSuccess(messages: List<Message>) {
                    if (view != null) {
                        navigator.toggleLoader(false)
                        chatList.addAll(messages)
                        //for unread message count
                        if (!chatList.isEmpty()) {
                            newChannel!!.messages.lastConsumedMessageIndex = chatList.get(chatList.size - 1).messageIndex
                        }
                        chatListingAdapter?.notifyDataSetChanged()
                        recyclerViewMessageListing.scrollToPosition(chatList.size - 1)
                    }
                }
            })
        }
    }

    override fun onMemberDeleted(p0: com.twilio.chat.Member?) {

    }

    override fun onTypingEnded(p0: com.twilio.chat.Member?) {

    }

    override fun onMessageDeleted(p0: Message?) {

    }

    override fun onMemberAdded(p0: com.twilio.chat.Member?) {

    }

    override fun onTypingStarted(p0: com.twilio.chat.Member?) {

    }

    override fun onSynchronizationChanged(p0: Channel?) {
    }

    override fun onMessageUpdated(p0: Message?, p1: Message.UpdateReason?) {
    }

    override fun onMemberUpdated(p0: com.twilio.chat.Member?, p1: com.twilio.chat.Member.UpdateReason?) {
    }
}