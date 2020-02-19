package com.victoria.driver.ui.base

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
import com.twilio.chat.StatusListener
import com.victoria.driver.R
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.core.Session
import com.victoria.driver.di.HasComponent
import com.victoria.driver.di.component.ActivityComponent
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.di.module.FragmentModule
import com.victoria.driver.exception.AuthenticationException
import com.victoria.driver.ui.authentication.activities.AuthenticationActivity
import com.victoria.driver.ui.home.activity.HomeActivity
import com.victoria.driver.ui.manager.Navigator
import com.victoria.driver.util.PARAMETERS
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

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var toolbar: HasToolbar

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

    fun openDrawer() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).openDrawer()
        }
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

    fun showDialogWithTwoActions(title: String?, message: String?,
                                 positiveName: String?, negativeName: String?,
                                 positiveFunction: (DialogInterface, Int) -> Unit,
                                 negativeFunction: (DialogInterface, Int) -> Unit) {
        var alertDialogMain = AlertDialog.Builder(this.activity!!)
                .setCancelable(false)
                .create()
        alertDialogMain.setMessage(message)
        alertDialogMain.setTitle(title)
        alertDialogMain.setButton(DialogInterface.BUTTON_POSITIVE, positiveName, positiveFunction)
        alertDialogMain.setButton(DialogInterface.BUTTON_NEGATIVE, negativeName, negativeFunction)
        alertDialogMain.show()
        val textView = alertDialogMain.findViewById<TextView>(android.R.id.message)
        if (textView != null) {
            textView.typeface = ResourcesCompat.getFont(this.activity!!, R.font.montserrat_regular)
        }
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
                (activity as BaseActivity)?.toggleLoader(false)

                (activity as BaseActivity).showErrorMessage(throwable.toString())
            }
        }
//        Log.e(javaClass.simpleName, "Error From Base framework ", throwable)
    }

    fun showMessage(message: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showErrorMessage(message)
        }
    }

    protected abstract fun createLayout(): Int
    protected abstract fun inject(fragmentComponent: FragmentComponent)
    protected abstract fun bindData()

}
