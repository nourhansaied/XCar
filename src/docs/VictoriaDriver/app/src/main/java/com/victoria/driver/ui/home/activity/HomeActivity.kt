package com.victoria.driver.ui.home.activity


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import com.twilio.accessmanager.AccessManager
import com.twilio.chat.CallbackListener
import com.twilio.chat.ChatClient
import com.twilio.chat.ErrorInfo
import com.twilio.chat.StatusListener
import com.victoria.customer.data.URLFactory
import com.victoria.driver.R
import com.victoria.driver.R.id.placeHolder
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.core.Common
import com.victoria.driver.core.Session
import com.victoria.driver.data.AccessData
import com.victoria.driver.data.pojo.Notification
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.App
import com.victoria.driver.di.component.ActivityComponent
import com.victoria.driver.fcm.MyFirebaseMessagingService
import com.victoria.driver.fcm.NotificationHandler
import com.victoria.driver.ui.authentication.activities.AuthenticationActivity
import com.victoria.driver.ui.base.BaseActivity
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.dialog_fragment.RideRequestPopup
import com.victoria.driver.ui.home.fragment.*
import com.victoria.driver.ui.location_service.LocationService
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.ui.viewmodel.HomeViewModel
import com.victoria.driver.util.CircleTransform
import com.victoria.driver.util.PARAMETERS
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject

class HomeActivity : BaseActivity(), RideRequestPopup.CallBackForRideNavigation {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var session: Session
    internal lateinit var sharedPreferences: SharedPreferences

    public companion object TwillioChat {
        private lateinit var notificationHandler: NotificationHandler

        @JvmStatic
        fun setNotificationHandler(notificationHandlerHandler: NotificationHandler) {
            notificationHandler = notificationHandlerHandler
        }

        @JvmField
        var activeornot: Boolean = false
        @JvmField
        var openNotification: Boolean = false
        var newChatClient: ChatClient? = null
    }

    var isMenuOpen: Boolean = false
    private var dialogFragment: RideRequestPopup? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var baseFragment: BaseFragment? = null

    override fun findFragmentPlaceHolder(): Int {
        return placeHolder

    }

    override fun findContentView(): Int {
        return R.layout.activity_home_layout
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = this.getSharedPreferences(Common.LANGUAGE_SELECTION, Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean(Common.WHICH_LANGUAGE, false)) {
            changeLang(Common.Language.ARABIC, this)
            Common.WHICH_LANGUAGE_SELECTED = Common.ARABIC_LANGUAGE
        } else {
            changeLang("", this)
            Common.WHICH_LANGUAGE_SELECTED = ""
        }
        setContentView(R.layout.activity_home_layout)

        load(HomeFragment::class.java).replace(false)

        navigateFromNotificationClick()
        textViewNotificationCount.visibility = View.GONE

        setDataFromSession()

        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
        imageViewProfile.setOnClickListener(this::onViewClick)
        textViewHome.setOnClickListener(this::onViewClick)
        textViewEarnings.setOnClickListener(this::onViewClick)
        textViewRideHistory.setOnClickListener(this::onViewClick)
        textViewNotification.setOnClickListener(this::onViewClick)
        textViewTransactionHistory.setOnClickListener(this::onViewClick)
        textViewSettings.setOnClickListener(this::onViewClick)
        textViewLogout.setOnClickListener(this::onViewClick)
        constraintLayoutRoot.setOnClickListener(this::onViewClick)


    }

    private fun navigateFromNotificationClick() {
        if (intent.extras != null) {
            if (intent.extras.getString(Common.ACTIVITY_FIRST_PAGE) != null) {
                if (intent.extras.getString(Common.ACTIVITY_FIRST_PAGE) == (Common.PUSH_TAG_PAYMENT)) {
                    run {
                        /*var bundle = Bundle()
                        bundle.putString(Common.TRIP_ID, intent.extras.getString(Common.TRIP_ID))
                        load(ReceiptFragment::class.java).setBundle(bundle).replace(true)*/
                    }
                } else if (intent.extras.getString(Common.ACTIVITY_FIRST_PAGE) == (Common.PUSH_TAG_PLACE_ORDER)) {
                    run {
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        var unmaskedRequestCode = requestCode and 0x0000ffff
        if (unmaskedRequestCode == 1) {
            getCurrentFragment<BaseFragment>()?.onRequestPermissionsResult(unmaskedRequestCode, permissions, grantResults)
        }
    }

    override fun onResume() {
        super.onResume()
        startService(Intent(this@HomeActivity, LocationService::class.java))
        EventBus.getDefault().register(this)
        baseFragment = supportFragmentManager.findFragmentById(placeHolder) as BaseFragment?
        if (newChatClient == null) {
            observeResponseForAccessToken()
        }
        Handler().postDelayed({
            observeTrackTrip()
        }, 500)

        //observeLogoutClickResponse()
        //observeNotificationCount()
        //observeAcceptRejectApi()
    }


    fun observeResponseForAccessToken() {
        //identity,user_type[Customer,Driver],device_type[A,I],device_token
        val parameter = Parameter()
        parameter.identity = session.user?.driverId.toString()
        parameter.user_type = session.user?.userType.toString()
        parameter.devieType = "A"
        parameter.deviceToken = FirebaseInstanceId.getInstance().token.toString()
        toggleLoader(false)
        toggleLoader(true)
        viewModel.generateAccesstoken.value = null
        viewModel.generateAccesstoken(parameter)
        viewModel.generateAccesstoken.observe(this, { responseBody ->
            viewModel.generateAccesstoken.removeObservers(this)
            toggleLoader(false)
            if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
                responseBody.data?.let { createChatClient(it) }
            }
        })
    }

    public fun createChatClient(accessTokenData: AccessData) {
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
        ChatClient.create(this, accessTokenData.token, properties, object : CallbackListener<ChatClient>() {
            override fun onSuccess(chatClient: ChatClient) {
                newChatClient = chatClient
                HomeActivity?.newChatClient!!.registerFCMToken(FirebaseInstanceId.getInstance().token, object : StatusListener() {
                    override fun onSuccess() {

                    }
                })
            }

            override fun onError(errorInfo: ErrorInfo?) {

            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(myFirebaseNotification: Notification) {
        when (myFirebaseNotification.tag) {
            Common.PUSH_TAG_TWILLIO -> {
                if (getCurrentFragment<BaseFragment>() !is ChatMessageListingFragment) {
                    sendNotification(myFirebaseNotification, Common.PUSH_TAG_TWILLIO)
                }
            }
            Common.PUSH_TAG_PLACE_ORDER -> {
                observeTrackTrip()
                //homeActivityViewModel.trackTripApiCall()
            }
            Common.PUSH_TAG_PAYMENT -> {
                Toast.makeText(this, myFirebaseNotification.message, Toast.LENGTH_SHORT).show()
            }

            Common.PUSH_TAG_CANCEL_TRIP -> {
                showErrorMessage(myFirebaseNotification.message)
                load(HomeFragment::class.java).replace(false)
            }

            Common.PUSH_TAG_CANCEL_LATE_TRIP -> {
                showErrorMessage(myFirebaseNotification.message)
                load(HomeFragment::class.java).replace(false)
            }
            else -> {
            }
        }
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this@HomeActivity, LocationService::class.java))
    }

    private fun setDataFromSession() {
        var user = session.user
        Picasso.get()
                .load(user?.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewProfile)

        textViewUserName.text = user?.firstName + " " + user?.lastName
    }

    override fun openDrawer() {

        observeNotificationCount()

        isMenuOpen = true
        constraintLayoutRoot.animate().translationY(0.toFloat()).setDuration(500).start()

    }


    override fun closeDrawer() {

        isMenuOpen = false
        var animValue: Float = constraintLayoutRoot.height.toFloat()
        constraintLayoutRoot.animate().translationY(-animValue).setDuration(500).start()
    }


    fun onViewClick(view: View) {
        isMenuOpen = false

        when (view.id) {

            R.id.imageViewMenu -> {
                isMenuOpen = true
                openDrawer()
            }

            R.id.imageViewProfile -> {
                closeDrawer()
                load(DriverProfileFragmet::class.java).replace(false)
            }

            R.id.textViewHome -> {
                closeDrawer()
                load(HomeFragment::class.java).replace(false)
            }

            R.id.textViewEarnings -> {
                closeDrawer()
                load(EarningsFragment::class.java).replace(false)
            }
            R.id.textViewRideHistory -> {
                closeDrawer()
                load(RideHistoryFragment::class.java).replace(false)
            }
            R.id.textViewNotification -> {
                closeDrawer()
                load(NotificationFragment::class.java).replace(false)

            }
            R.id.textViewTransactionHistory -> {
                closeDrawer()
                load(TransactionHistoryFragment::class.java).replace(false)
            }
            R.id.textViewSettings -> {
                closeDrawer()
                load(SettingsFragment::class.java).replace(false)
            }

            R.id.textViewLogout -> {
                closeDrawer()
                showDialogWithTwoActions(null, getString(R.string.logout_message), getString(R.string.label_yes), getString(R.string.label_no), { yesclick, id ->
                    observeLogoutClickResponse()
                }, { noClick, id ->
                })
            }

            R.id.imageViewClose -> {
                closeDrawer()
            }


            R.id.constraintLayoutRoot -> {
                closeDrawer()
            }


        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()

        val baseFragment = supportFragmentManager.findFragmentById(placeHolder) as BaseFragment?

        if (baseFragment is RideHistoryFragment
                || baseFragment is DriverProfileFragmet
                || baseFragment is SettingsFragment
                || baseFragment is EarningsFragment
                || baseFragment is NotificationFragment
                || baseFragment is TransactionHistoryFragment) {

            load(HomeFragment::class.java).replace(false)

        } else if (baseFragment is DriverGoingFragment || baseFragment is RideStartFragment) {
        } else if (baseFragment is HomeFragment) {
            finish()
        } else if (isMenuOpen) {

            closeDrawer()

        } else {
            super.onBackPressed()
        }
    }

    var requestAcceptOrReject: Boolean = false


    override fun rideNavigationCallBack(isAccept: Boolean) {
        observeAcceptRejectApi(isAccept)
    }

    /**
     * Logout API calling stuff
     * */

    private fun getLogOutData(): Parameter {
        val parameter = Parameter()

        return parameter
    }

    private fun observeLogoutClickResponse() {
        baseFragment = supportFragmentManager.findFragmentById(placeHolder) as BaseFragment?
        homeActivityViewModel.logOut.observe(baseFragment!!, { responseBody ->
            homeActivityViewModel.logOut.removeObservers(baseFragment!!)
            handleOtpResponse(responseBody)
        })
        homeActivityViewModel.logOutApiCall()
    }

    private fun handleOtpResponse(responseBody: ResponseBody<User>) {
        //toggleLoader(false)
        if (responseBody.responseCode == 1) {
            if (HomeActivity.newChatClient != null) {
                HomeActivity.newChatClient!!.unregisterFCMToken(session.deviceId, object : StatusListener() {
                    override fun onSuccess() {
                        if (HomeActivity.newChatClient != null) {
                            appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                            appPreferences.clearAll()
                            HomeActivity.newChatClient!!.shutdown()
                            HomeActivity.newChatClient = null
                            loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
                        } else {
                            session.clearSession()
                            appPreferences.clearAll()
                            appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                            loadActivity(AuthenticationActivity::class.java).byFinishingCurrent().start()
                        }
                    }
                })
            } else {
                session.clearSession()
                appPreferences.clearAll()
                appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                (this as BaseActivity).loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
            }
        } else {
            showErrorMessage(responseBody.message)
        }
    }

    fun changeLang(language: String, context: Context) {

        var lang = Common.Language.ENGLISH

        if (!language.equals("", ignoreCase = true) && language.equals(Common.Language.ARABIC, ignoreCase = true)) {
            lang = Common.Language.ARABIC
        }

        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        if (!language.isEmpty()) {
            Common.WHICH_LANGUAGE_SELECTED = Common.ARABIC_LANGUAGE
            sharedPreferences.edit().putBoolean(Common.WHICH_LANGUAGE, true).apply()
            session.setLanguage(Common.ARABIC_LANGUAGE)
        } else {
            Common.WHICH_LANGUAGE_SELECTED = ""
            sharedPreferences.edit().putBoolean(Common.WHICH_LANGUAGE, false).apply()
            session.setLanguage("")
        }
    }


    /**
     * Logout API calling stuff
     * */

    fun updateDetail() {
        setDataFromSession()
    }

    /**
     * Track Trip API calling stuff
     * */


    private fun observeTrackTrip() {
        baseFragment = supportFragmentManager.findFragmentById(placeHolder) as BaseFragment?
        homeActivityViewModel.trackTrip.removeObservers(baseFragment!!)
        homeActivityViewModel.trackTrip.observe(baseFragment!!, { responseBody ->
            homeActivityViewModel.trackTrip.removeObservers(baseFragment!!)
            //toggleLoader(false)
            if (responseBody.responseCode == 1) {
                App.setTripData(responseBody.data)
                if ((responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) ||
                                responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_LATER))) {

                    if (responseBody.data?.status.equals(Common.TRIP_TAG_WAITING)) {
                        if (dialogFragment != null) {
                            dialogFragment!!.dismissAllowingStateLoss()
                        }
                        dialogFragment = RideRequestPopup(this, responseBody.data?.appWaitingTime!!, appPreferences)
                        dialogFragment!!.show(this.supportFragmentManager, "")

                    } else if (responseBody.data?.status.equals(Common.TRIP_TAG_ASSIGNED)) {
                        if (intent != null && intent.extras != null &&
                                intent.extras.getString(Common.ACTIVITY_FIRST_PAGE) == (Common.PUSH_TAG_TWILLIO)) {
                            run {
                                intent.extras.clear()
                                intent = null
                                load(DriverGoingFragment::class.java).setBundle(Bundle().apply {
                                    putBoolean(Common.IS_OPEN_CHAT, true)
                                }).replace(true)
                            }
                        } else {
                            load(DriverGoingFragment::class.java).setBundle(Bundle().apply {
                                putBoolean(Common.IS_OPEN_CHAT, false)
                            }).replace(true)
                        }
                    } else if (responseBody.data?.status.equals(Common.TRIP_TAG_ARRIVED)) {

                        load(RideStartFragment::class.java).replace(true)

                    } else if (responseBody.data?.status.equals(Common.TRIP_TAG_PROCESSING)) {

                        load(RideStartFragment::class.java).replace(true)
                    } else if (responseBody.data?.status.equals(Common.TRIP_TAG_COMPLETE) && responseBody.data?.driverConfirmpaymentStatus.equals(Common.TRIP_TAG_DRIVER_CONFIRMATION_STATUS_UNPAID)) {

                        load(ReceiptFragment::class.java).replace(false)
                    }
                }
            } else if (responseBody.responseCode == 2) {

            }

        })

        homeActivityViewModel.trackTripApiCall()
    }


    /**
     * Track Trip API calling stuff
     * */

    /**
     * Accept Or Reject Trip API calling stuff
     * */

    private fun observeAcceptRejectApi(isAccept: Boolean) {
        baseFragment = supportFragmentManager.findFragmentById(placeHolder) as BaseFragment?
        homeActivityViewModel.acceptOrReject.observe(baseFragment!!, { responseBody ->
            homeActivityViewModel.acceptOrReject.removeObservers(baseFragment!!)
            toggleLoader(false)
            if (responseBody.responseCode == 1) {
                if (requestAcceptOrReject) {
                    //Toast.makeText(this, responseBody.message, Toast.LENGTH_SHORT).show()
                    App.setTripData(responseBody.data)
                    load(DriverGoingFragment::class.java).replace(true)
                } else {
                    if (dialogFragment != null)
                        dialogFragment?.dismiss()
                }
            } else if (responseBody.responseCode == 2) {
                showErrorMessage(responseBody.message)
            }
        })

        val parameter = Parameter()
        parameter.tripId = App.getTripData().tripId.toString()
        requestAcceptOrReject = isAccept
        toggleLoader(true)
        homeActivityViewModel.acceptOrRejectApiCall(parameter, isAccept)

    }

    /**
     * Accept Or Reject Trip API calling stuff
     * */

    private fun observeNotificationCount() {
        baseFragment = supportFragmentManager.findFragmentById(placeHolder) as BaseFragment?
        homeActivityViewModel.notificationCount.observe(baseFragment!!, { responseBody ->
            homeActivityViewModel.notificationCount.removeObservers(baseFragment!!)
            if (responseBody.data?.notificationcount != 0) {
                textViewNotificationCount.visibility = View.VISIBLE
                textViewNotificationCount.text = responseBody.data?.notificationcount.toString()
            } else {
                textViewNotificationCount.visibility = View.GONE
            }

        })
        val parameter = Parameter()
        parameter.user_type = Common.DRIVER
        homeActivityViewModel.notificationCountApi(parameter)
    }

    override fun dissmissAfterTimesUp() {
        if (dialogFragment != null) {
            observeAcceptRejectApi(false)
        }
    }

    fun sendNotification(notification: Notification, flag: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if (flag != Common.PUSH_TAG_TWILLIO) {
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, notification.tag)
        }

        if (flag == Common.PUSH_TAG_PAYMENT) {
            intent.putExtra(Common.TRIP_ID, notification.trip_id)
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, flag)
        } else if (flag == Common.PUSH_TAG_PLACE_ORDER) {
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, flag)
        } else if (flag == Common.PUSH_TAG_TWILLIO) {
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, flag)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        /*Uri defaultSoundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() +
                "/" + R.raw.notification_sound);*/

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setContentTitle(notification.title)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
                .setContentText(notification.message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(defaultSoundUri, attributes)
            notificationManager?.createNotificationChannel(channel)
        }

        notificationManager?.notify(0, notificationBuilder.build())
    }

}
