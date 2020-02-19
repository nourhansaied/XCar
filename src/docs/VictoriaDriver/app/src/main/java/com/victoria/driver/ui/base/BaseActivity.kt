package com.victoria.driver.ui.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.victoria.driver.R
import com.victoria.driver.di.HasComponent
import com.victoria.driver.di.Injector
import com.victoria.driver.di.component.ActivityComponent
import com.victoria.driver.di.component.DaggerActivityComponent
import com.victoria.driver.ui.authentication.fragment.StartFragment
import com.victoria.driver.ui.home.fragment.*
import com.victoria.driver.ui.manager.*
import com.victoria.driver.util.LocationManager
import com.victoria.driver.util.MyCustomProgressDialog
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasComponent<ActivityComponent>, HasToolbar, Navigator {
    override val component: ActivityComponent
        get() = activityComponent
    @Inject
    lateinit var navigationFactory: FragmentNavigationFactory

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var activityStarter: ActivityStarter

    //protected var toolbar: Toolbar? = null
    //protected var toolbarTitle: AppCompatTextView? = null
    //internal var progressDialog: ProgressDialog? = null
    internal var progressDialog: MyCustomProgressDialog? = null

    internal var alertDialog: AlertDialog? = null

    private lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        activityComponent = DaggerActivityComponent.builder()
                .bindApplicationComponent(Injector.INSTANCE.applicationComponent)
                .bindActivity(this)
                .build()

        inject(activityComponent)

        super.onCreate(savedInstanceState)

        setContentView(findContentView())

        locationManager.setActivity(this)

        /*if (toolbar != null)
            setSupportActionBar(toolbar)*/

        setUpAlertDialog()

        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng?) {
                locationManager.stop()
            }

            override fun onFail(status: LocationManager.LocationListener.Status?) {

            }
        })
        /*progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)*/

        //toggleLoader(true)

    }


    private fun setUpAlertDialog() {
        alertDialog = AlertDialog.Builder(this)
                .setPositiveButton("ok", null)
                .setTitle(R.string.app_name)
                .create()
    }

    fun <F : BaseFragment> getCurrentFragment(): F? {
        return if (findFragmentPlaceHolder() == 0) null else supportFragmentManager.findFragmentById(findFragmentPlaceHolder()) as F
    }

    abstract fun findFragmentPlaceHolder(): Int

    @LayoutRes
    abstract fun findContentView(): Int


    abstract fun inject(activityComponent: ActivityComponent)


    fun showErrorMessage(message: String?) {
        val f = getCurrentFragment<BaseFragment>()
//        val f = getCurrentFragment<BaseFragment<*, *>>()
        if (f != null)
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (getCurrentFragment<BaseFragment>()!!.view != null) {

                val snackbar = Snackbar.make(getCurrentFragment<BaseFragment>()?.view!!, message!!, Snackbar.LENGTH_SHORT)
                snackbar.setActionTextColor(this.resources.getColor(R.color.white))
                snackbar.setAction("Ok") { snackbar.dismiss() }
                val snackView = snackbar.view
                val textView = snackView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
                textView.maxLines = 4
                textView.setTextColor(this.resources.getColor(R.color.white))

                snackView.setBackgroundResource(R.drawable.snackbar_gradirent)
                snackbar.show()
            }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun toggleLoader(show: Boolean) {

        /*if (show) {
            if (!progressDialog!!.isShowing)
                progressDialog!!.show()
        } else {
            if (progressDialog!!.isShowing)
                progressDialog!!.dismiss()
        }*/

        if (show) {
            try {
                if (progressDialog == null/*&& this!= null*/) {
                    try {

                        progressDialog = MyCustomProgressDialog()

                    } catch (e: Exception) {
                        //e.printStackTrace();
                    }

                }
                if (progressDialog != null && !progressDialog?.isAdded!!/* && getActivity() != null*/) {
                    progressDialog!!.show(supportFragmentManager, "")
                }

            } catch (e: Exception) {

            }

        } else {
            try {

                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

            } catch (e: Exception) {

            }

        }
    }

    protected fun shouldGoBack(): Boolean {
        return true
    }

    fun showDialogWithTwoActions(title: String?, message: String?,
                                 positiveName: String?, negativeName: String?,
                                 positiveFunction: (DialogInterface, Int) -> Unit,
                                 negativeFunction: (DialogInterface, Int) -> Unit) {
        var alertDialogMain = AlertDialog.Builder(this)
                .setCancelable(false)
                .create()
        alertDialogMain.setMessage(message)
        alertDialogMain.setTitle(title)
        alertDialogMain.setButton(DialogInterface.BUTTON_POSITIVE, positiveName, positiveFunction)
        alertDialogMain.setButton(DialogInterface.BUTTON_NEGATIVE, negativeName, negativeFunction)
        alertDialogMain.show()
        val textView = alertDialogMain.findViewById<TextView>(android.R.id.message)
        if (textView != null) {
            textView.typeface = ResourcesCompat.getFont(this, R.font.montserrat_regular)
        }
    }


    override fun onBackPressed() {
        hideKeyboard()


        val currentFragment = getCurrentFragment<BaseFragment>()
        if (currentFragment == null)
            super.onBackPressed()
        else if (currentFragment is DriverGoingFragment || currentFragment is RideStartFragment || currentFragment is RatingFragment || currentFragment is ReceiptFragment)
        else if (currentFragment is StartFragment)
            finish()
        else if (currentFragment is HomeFragment) {
            finish()
        } else if (currentFragment.onBackActionPerform() && shouldGoBack())
            super.onBackPressed()

        // pending animation
        // overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);

    }

    fun hideKeyboard() {
        // Check if no view has focus:

        val view = this.currentFocus
        if (view != null) {
            val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun showToolbar(b: Boolean) {
        val supportActionBar = supportActionBar
        if (supportActionBar != null) {

            if (b)
                supportActionBar.show()
            else
                supportActionBar.hide()
        }
    }

    override fun setToolbarTitle(title: CharSequence) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }

    override fun setToolbarTitle(@StringRes title: Int) {

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(title)
            //appToolbarTitle.setText(name);
        }
    }

    override fun showBackButton(b: Boolean) {

        val supportActionBar = supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(b)
    }

    override fun setToolbarColor(@ColorRes color: Int) {

        TODO("Remove Comment")
        /*if (toolbar != null) {
            toolbar.setBackgroundResource(color)
        }*/

    }


    override fun setToolbarElevation(isVisible: Boolean) {

        if (supportActionBar != null) {
            supportActionBar!!.elevation = if (isVisible) 8f else 0f
        }
    }

    fun showKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    override fun <T : BaseFragment> load(tClass: Class<T>): FragmentActionPerformer<T> {
        return navigationFactory.make(tClass)
    }

    override fun loadActivity(aClass: Class<out BaseActivity>): ActivityBuilder {
        return activityStarter.make(aClass)
    }

    override fun <T : BaseFragment> loadActivity(aClass: Class<out BaseActivity>, pageTClass: Class<T>): ActivityBuilder {
        return activityStarter.make(aClass).setPage(pageTClass)
    }

    override fun openDrawer() {

    }

    override fun closeDrawer() {

    }

    override fun goBack() {
        onBackPressed()
    }


}
