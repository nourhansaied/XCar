package com.victoria.customer.ui.base;


import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.iid.FirebaseInstanceId
import com.twilio.chat.StatusListener
import com.victoria.customer.R
import com.victoria.customer.core.AppPreferences
import com.victoria.customer.core.Session
import com.victoria.customer.di.HasComponent
import com.victoria.customer.di.component.ActivityComponent
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.di.module.FragmentModule
import com.victoria.customer.exception.AuthenticationException
import com.victoria.customer.ui.authentication.activities.AuthenticationActivity
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.ui.manager.Navigator
import com.victoria.customer.util.LocationManager
import com.victoria.customer.util.PARAMETERS
import java.net.ConnectException
import javax.inject.Inject

/**
 * Created by hlink21 on 25/4/16.
 */
abstract class BaseFragment : Fragment(), HasComponent<FragmentComponent> {

    @Inject
    lateinit var appPreferences: AppPreferences
    @Inject
    lateinit var navigator: Navigator
    protected lateinit var toolbar: HasToolbar
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var session: Session
    @Inject
    lateinit var locationManager: LocationManager


    override val component: FragmentComponent
        get() {
            return getComponent(ActivityComponent::class.java).plus(FragmentModule(this))
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(createLayout(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindData()
    }

    protected fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<C>).component)
    }

    override fun onAttach(context: Context?) {
        inject(component)
        super.onAttach(context)

        if (activity is HasToolbar)
            toolbar = activity as HasToolbar


    }

    override fun onResume() {
        super.onResume()

        /*locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng) {
                latitude = latLng.latitude
                longitude = latLng.longitude
                locationManager.stop()
            }

            override fun onFail(status: LocationManager.LocationListener.Status) {

            }
        })*/

    }


    fun hideKeyBoard() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideKeyboard()
        }
    }

    fun showKeyBoard() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showKeyboard()
        }
    }

    fun openDrawer() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).openDrawer()
        }
    }

    fun closeDrawer() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).closeDrawer()
        }
    }

    fun <T : BaseFragment> getParentFragment(targetFragment: Class<T>): T? {
        if (parentFragment == null) return null
        try {
            return targetFragment.cast(parentFragment)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }

    open fun onShow() {

    }

    open fun onBackActionPerform(): Boolean {
        return true
    }

    open fun onViewClick(view: View) {

    }

    fun onError(throwable: Throwable) {
        if (activity is BaseActivity) {
            if (throwable is AuthenticationException) {
                if (HomeActivity.newChatClient != null) {
                    HomeActivity.newChatClient!!.unregisterFCMToken(session.deviceId, object : StatusListener() {
                        override fun onSuccess() {
                            if (HomeActivity.newChatClient != null) {
                                appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                                appPreferences.clearAll()
                                (activity as BaseActivity).loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
                                HomeActivity.newChatClient!!.shutdown()
                                HomeActivity.newChatClient = null
                            } else {
                                appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                                appPreferences.clearAll()
                                (activity as BaseActivity).loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
                            }
                        }
                    })
                } else {
                    appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, false)
                    appPreferences.clearAll()
                    (activity as BaseActivity).loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
                }
            } else if (throwable is ConnectException) {
                showMessage("OOPS! NO INTERNET. Please check your network connection. Try Again")
            } else {
                (activity as BaseActivity).showErrorMessage(throwable.toString())
            }
        }
//        Log.e(javaClass.simpleName, "Error From Base framework ", throwable)
    }

    private lateinit var alertDialog: AlertDialog

    fun showDialogWithTwoActions(title: String?, message: String?,
                                 positiveName: String?, negativeName: String?,
                                 positiveFunction: (DialogInterface, Int) -> Unit,
                                 negativeFunction: (DialogInterface, Int) -> Unit) {
        alertDialog = context?.let {
            AlertDialog.Builder(it)
                    .setCancelable(false)
                    .create()
        }!!
        alertDialog.setMessage(message)
        alertDialog.setTitle(title)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveName, positiveFunction)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeName, negativeFunction)
        alertDialog.show()
        val textView = alertDialog.findViewById<TextView>(android.R.id.message)
        if (textView != null) {
            textView.typeface = context?.let { ResourcesCompat.getFont(it, R.font.montserrat_regular) }
        }
    }

    fun showMessage(message: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showErrorMessage(message)
        }
    }

    fun showSnackBar(message: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showSnackBar(message)
        }
    }

    fun showSnackBar(message: String, showOk: Boolean) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showSnackBar(message, showOk)
        }
    }

    fun showSnackBar(view: View, message: String, showOk: Boolean) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showSnackBar(view, message, showOk)
        }
    }

    protected abstract fun createLayout(): Int
    protected abstract fun inject(fragmentComponent: FragmentComponent)
    protected abstract fun bindData()

}
