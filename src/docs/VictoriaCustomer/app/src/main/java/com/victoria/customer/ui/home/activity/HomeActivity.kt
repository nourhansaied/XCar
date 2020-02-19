package com.victoria.customer.ui.home.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.NotificationCompat
import android.view.View
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import com.twilio.accessmanager.AccessManager
import com.twilio.chat.CallbackListener
import com.twilio.chat.ChatClient
import com.twilio.chat.ErrorInfo
import com.twilio.chat.StatusListener
import com.victoria.customer.R
import com.victoria.customer.core.AppPreferences
import com.victoria.customer.core.Common
import com.victoria.customer.core.Session
import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.URLFactory.ResponseCode.SUCCESS
import com.victoria.customer.data.pojo.*
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.ActivityComponent
import com.victoria.customer.fcm.NotificationHandler
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.activities.AuthenticationActivity
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseActivity
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.fragments.*
import com.victoria.customer.ui.home.ride.dialog.DriverArrivedDialog
import com.victoria.customer.ui.home.ride.fragments.*
import com.victoria.customer.ui.home.settings.fragments.PaymentDetailsFragment
import com.victoria.customer.ui.home.viewmodel.FareEstimationViewModel
import com.victoria.customer.ui.home.viewmodel.HomeViewModel
import com.victoria.customer.util.CircleTransform
import com.victoria.customer.util.NetworkChangeReceiver
import com.victoria.customer.util.PARAMETERS
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject


class HomeActivity : BaseActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var session: Session

    var isMenuOpen: Boolean = false
    internal lateinit var sharedPreferences: SharedPreferences

    private var networkChangeReceiver: NetworkChangeReceiver? = null
    lateinit var driverArrivedDialog: DriverArrivedDialog

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


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun findFragmentPlaceHolder(): Int {
        return R.id.placeHolder
    }

    override fun findContentView(): Int {
        return R.layout.activity_home_layout
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
    }

    private val viewModelF: FareEstimationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FareEstimationViewModel::class.java]
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

        setDataFromSession()

        observeNotificationCount()

        load(HomeStartFragment::class.java).replace(false)

        observeLogoutClickResponse()

        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
        constraintLayoutRoot.setOnClickListener(this::onViewClick)
        imageViewProfile.setOnClickListener(this::onViewClick)
        textViewHome.setOnClickListener(this::onViewClick)
        textViewWallet.setOnClickListener(this::onViewClick)
        textViewMyRides.setOnClickListener(this::onViewClick)
        textViewNotification.setOnClickListener(this::onViewClick)
        textViewSettings.setOnClickListener(this::onViewClick)
        textViewLogout.setOnClickListener(this::onViewClick)
        if (intent?.extras != null && intent.extras.containsKey(Common.ACTIVITY_FIRST_PAGE)) {
            if (intent.extras.get(Common.ACTIVITY_FIRST_PAGE).equals(Common.PUSH_TAG_REFER_USER)) {
                loadActivity(IsolatedActivity::class.java).setPage(WalletFragment::class.java).start()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val unmaskedRequestCode = requestCode and 0x0000ffff
        if (unmaskedRequestCode == 1) {
            getCurrentFragment<BaseFragment>()?.onRequestPermissionsResult(unmaskedRequestCode, permissions, grantResults)
        }
    }

    private fun setDataFromSession() {
        val user = session.user
        Picasso.get()
                .load(user?.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewProfile)

        textViewUserName.text = user?.firstName + " " + user?.lastName
        textViewWalletAmount.text = "EGP " + user?.wallet.toString()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(myFirebaseNotification: Notification) {
        when (myFirebaseNotification.tag) {
            Common.PUSH_TAG_TWILLIO -> {
                if (getCurrentFragment<BaseFragment>() !is ChatMessageListingFragment) {
                    sendNotification(myFirebaseNotification, Common.PUSH_TAG_TWILLIO)
                }
            }
            Common.PUSH_TAG_DECLINE_ORDER -> {
                val bundle = Bundle()
                bundle.putBoolean(Common.SHOW_MESSGE_FOR_NO_DRIVER_AVAILABLE, true)
                bundle.putString(Common.MESSAGE, myFirebaseNotification.message)
                load(HomeStartFragment::class.java).setBundle(bundle).replace(false)
            }

            Common.PUSH_TAG_ACCEPT_ORDER -> {
                homeActivityViewModel.trackTripApiCall()
            }

            Common.PUSH_TAG_ARRIVED_ORDER -> {
                homeActivityViewModel.trackTripApiCall()
            }

            Common.PUSH_TAG_START_ORDER -> {
                homeActivityViewModel.trackTripApiCall()
            }

            Common.PUSH_TAG_CHANGE_DROP_OFF_ORDER -> {
                homeActivityViewModel.trackTripApiCall()
            }

            Common.PUSH_TAG_COMPLETE_ORDER -> {
                homeActivityViewModel.trackTripApiCall()
            }

            Common.PUSH_TAG_CANCEL_TRIP -> {
                if (getCurrentFragment<BaseFragment>() is DriverComingFragment) {
                    val driverComingFragment = supportFragmentManager.findFragmentById(R.id.placeHolder) as DriverComingFragment?
                    driverComingFragment?.closeDialogAndReturn()

                } else {
                    load(HomeStartFragment::class.java).replace(false)
                }
                showErrorMessage(myFirebaseNotification.message)

            }

            Common.PUSH_TAG_CANCEL_LATE_TRIP -> {
                showErrorMessage(myFirebaseNotification.message)
                if (getCurrentFragment<BaseFragment>() is DriverComingFragment) {
                    val driverComingFragment = DriverComingFragment()
                    driverComingFragment.closeDialogAndReturn()

                } else {
                    load(HomeStartFragment::class.java).replace(false)
                }
            }
            else -> {
            }
        }
    }


    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun openDrawer() {
        val parameter: Parameter = Parameter()
        parameter.user_type = Common.CUSTOMER
        homeActivityViewModel.notificationCountApi(parameter)
        isMenuOpen = true
        constraintLayoutRoot.animate().translationY(0.toFloat()).setDuration(500).start()
        observeResponseForWalletAmt()
    }

    private fun observeResponseForWalletAmt() {
        toggleLoader(false)
        toggleLoader(true)
        viewModelF.getwalletamount.value = null
        viewModelF.getwalletamount(Parameter())
        viewModelF.getwalletamount.observe(this, { responseBody ->
            toggleLoader(false)
            if (responseBody.responseCode == SUCCESS) {
                textViewWalletAmount.text = String.format("%.2f", responseBody.data?.wallet?.toDouble())
            }
        })
    }

    override fun closeDrawer() {
        isMenuOpen = false
        var animValue: Float = constraintLayoutRoot.height.toFloat()
        constraintLayoutRoot.animate().translationY(-animValue).setDuration(500).start()
    }


    fun onViewClick(view: View) {
        isMenuOpen = false
        closeDrawer()
        when (view.id) {

            R.id.imageViewMenu -> {
                isMenuOpen = true
                openDrawer()
            }

            R.id.imageViewProfile -> {
                load(ProfileFragment::class.java).replace(false)
            }
            R.id.textViewHome -> {
                load(HomeStartFragment::class.java).replace(false)
            }
            R.id.textViewWallet -> {
                load(WalletFragment::class.java).replace(false)
            }
            R.id.textViewMyRides -> {
                load(MyRidesFragment::class.java).replace(false)

            }
            R.id.textViewNotification -> {
                load(NotificationFragment::class.java).replace(false)
            }
            R.id.textViewSettings -> {
                load(SettingsFragment::class.java).replace(false)
            }

            R.id.textViewLogout -> {
                showDialogWithTwoActions(null, getString(R.string.logout_message), getString(R.string.label_yes), getString(R.string.label_no), { yesclick, id ->
                    toggleLoader(true)
                    homeActivityViewModel.logOutApiCall()
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

        val baseFragment = supportFragmentManager.findFragmentById(R.id.placeHolder) as BaseFragment?

        if (baseFragment is SettingsFragment
                || baseFragment is MyRidesFragment
                || baseFragment is TransactionHistoryFragment
                || baseFragment is NotificationFragment
                || baseFragment is WalletFragment
                || baseFragment is ProfileFragment) {

            load(HomeStartFragment::class.java).replace(false)
            //finish()
        } else if (baseFragment is ReceiptFragment ||
                baseFragment is DriverComingFragment ||
                baseFragment is RideFragment || baseFragment is RatingFragment || baseFragment is CarAllocationFragment) {

        } else if (baseFragment is HomeStartFragment) {
            finish()
        } else if (baseFragment is SelectLocationFragment) {

            VictoriaCustomer.setRideData()
            super.onBackPressed()
        } else if (isMenuOpen) {

            closeDrawer()

        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")

        if (newChatClient == null) {
            observeResponseForAccessToken()
        }

        networkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(networkChangeReceiver, filter)
        EventBus.getDefault().register(this)
        Handler().postDelayed({
            observeTrackTrip()
        }, 200)
        if (sharedPreferences.getBoolean(Common.WHICH_LANGUAGE, false)) {
            changeLang(Common.Language.ARABIC, this)
            Common.WHICH_LANGUAGE_SELECTED = Common.ARABIC_LANGUAGE
        } else {
            changeLang("", this)
            Common.WHICH_LANGUAGE_SELECTED = ""
        }
    }

    public fun observeResponseForAccessToken() {
        //identity,user_type[Customer,Driver],device_type[A,I],device_token
        val parameter = Parameter()
        parameter.identity = session.user?.customerId.toString()
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
                HomeActivity?.newChatClient!!.registerFCMToken(session.deviceId, object : StatusListener() {
                    override fun onSuccess() {

                    }
                })
            }

            override fun onError(errorInfo: ErrorInfo?) {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver)
        }
    }

    /**
     * Track trip API calling stuff
     * */

    private fun observeTrackTrip() {
        if (getCurrentFragment<BaseFragment>() !is PaymentDetailsFragment) {
            homeActivityViewModel.trackTrip.value = null
            homeActivityViewModel.trackTripApiCall()
            homeActivityViewModel.trackTrip.observe(this, onChange = { it ->
                handleTrackTripResponse(it)
            })
        }
    }


    private fun handleTrackTripResponse(responseBody: ResponseBody<PlaceOrder>) {
        toggleLoader(false)
        if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
            VictoriaCustomer.setTripData(responseBody.data)
            if ((responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) ||
                            responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_LATER)) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_WAITING)) {
                load(CarAllocationFragment::class.java).replace(true)
            } else if ((responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) ||
                            responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_LATER)) && responseBody.data?.status.equals(Common.TRIP_TAG_ASSIGNED)) {
                if (intent != null && intent.extras != null && intent.extras.getString(Common.ACTIVITY_FIRST_PAGE)
                        == (Common.PUSH_TAG_TWILLIO)) {
                    run {
                        intent.extras.clear()
                        intent = null
                        load(DriverComingFragment::class.java).setBundle(Bundle().apply {
                            putBoolean(Common.IS_OPEN_CHAT, true)
                        }).replace(true)
                    }
                } else {
                    load(DriverComingFragment::class.java).setBundle(Bundle().apply {
                        putBoolean(Common.IS_OPEN_CHAT, false)
                    }).replace(true)
                }
            } else if ((responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) || responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_LATER)) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_ARRIVED)) {
                if (intent != null && intent.extras != null && intent.extras.getString(Common.ACTIVITY_FIRST_PAGE)
                        == (Common.PUSH_TAG_TWILLIO)) {
                    run {
                        intent.extras.clear()
                        intent = null
                        load(DriverComingFragment::class.java).setBundle(Bundle().apply {
                            putBoolean(Common.IS_OPEN_CHAT, true)
                        }).replace(true)
                    }
                } else {
                    load(DriverComingFragment::class.java).setBundle(Bundle().apply {
                        putBoolean(Common.IS_OPEN_CHAT, false)
                    }).replace(true)
                }
                //load(DriverComingFragment::class.java).replace(true)
                //load(RideStartFragment::class.java).replace(true)
            } else if ((responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) ||
                            responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_LATER)) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_PROCESSING)) {
                dismissAllDialog(supportFragmentManager)
                load(RideFragment::class.java).replace(true)
            } else if ((responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) ||
                            responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_LATER)) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_COMPLETED) &&
                    responseBody.data?.paymentStatus.equals(Common.TRIP_TAG_PAYMENT_STATUS_UNPAID)) {
                dismissAllDialog(supportFragmentManager)
                load(ReceiptFragment::class.java).replace(false)
            }
        } else if (responseBody.responseCode == 2) {
            val baseFragment = supportFragmentManager.findFragmentById(R.id.placeHolder) as BaseFragment?
            if (baseFragment is DriverComingFragment || baseFragment is RideFragment)
                load(HomeStartFragment::class.java).replace(false)
        }
    }

    fun dismissAllDialog(manager: FragmentManager) {
        val fragments = manager?.getFragments()

        if (fragments == null)
            return
        for (fragment in fragments) {
            if (fragment is DialogFragment) {
                var dialogFragment = fragment as DialogFragment
                dialogFragment.dismissAllowingStateLoss()
            }
        }
    }
    /**
     * Track trip API calling stuff
     * */

    /**
     * Logout and Track trip API calling stuff
     * */


    private fun observeLogoutClickResponse() {
        homeActivityViewModel.logOut.observe(this, { responseBody ->
            handleOtpResponse(responseBody)
        })
    }

    private fun handleOtpResponse(responseBody: ResponseBody<User>) {
        toggleLoader(false)
        if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
            if (newChatClient != null) {
                newChatClient!!.unregisterFCMToken(session.deviceId, object : StatusListener() {
                    override fun onSuccess() {
                        if (newChatClient != null) {
                            appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                            appPreferences.clearAll()
                            newChatClient!!.shutdown()
                            newChatClient = null
                            loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
                        } else {
                            appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                            appPreferences.clearAll()
                            loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
                        }
                    }
                })
            } else {
                appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                appPreferences.clearAll()
                (this as BaseActivity).loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
            }
        } else {
            showErrorMessage(responseBody.message)
        }
    }

    /**
     * Logout API calling stuff
     * */

    private fun observeNotificationCount() {
        homeActivityViewModel.notificationCount.observe(this, { responseBody ->
            if (responseBody.data?.notificationcount != 0) {
                textViewNotificationCount.text = responseBody.data?.notificationcount.toString()
            } else {
                textViewNotificationCount.visibility = View.GONE
            }

        })
    }

    fun updateDetail() {
        setDataFromSession()
    }

    fun sendNotification(notification: Notification, flag: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if (flag != Common.PUSH_TAG_TWILLIO) {
            intent.putExtra(Common.ACTIVITY_FIRST_PAGE, notification.tag)
        }
        if (flag == Common.PUSH_TAG_TWILLIO) {
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
