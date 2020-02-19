package com.victoria.customer.ui.base;


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager
import com.payfort.fort.android.sdk.base.callbacks.FortCallback
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.di.HasComponent
import com.victoria.customer.di.Injector
import com.victoria.customer.di.component.ActivityComponent
import com.victoria.customer.di.component.DaggerActivityComponent
import com.victoria.customer.error.ExceptionHandler
import com.victoria.customer.ui.authentication.fragments.StartFragment
import com.victoria.customer.ui.home.fragments.HomeFragment
import com.victoria.customer.ui.home.fragments.HomeStartFragment
import com.victoria.customer.ui.home.ride.fragments.*
import com.victoria.customer.ui.manager.*
import com.victoria.customer.util.LocationManager
import com.victoria.customer.util.MyCustomProgressDialog
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

    private var progressDialog: MyCustomProgressDialog? = null

    private var fortCallback: FortCallBackManager? = null
    private lateinit var alertDialog: AlertDialog
    private lateinit var settingsDialog: AlertDialog
    private lateinit var confirmationDialog: AlertDialog

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
        ExceptionHandler.register(this, Common.ERRORLINK)

        /*if (toolbar != null)
            setSupportActionBar(toolbar)*/

        setUpAlertDialog()

        /*progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Please wait...")
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)*/

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
/*
        val f = getCurrentFragment<BaseFragment>()
//        val f = getCurrentFragment<BaseFragment<*, *>>()
        if (f != null)
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Snackbar.make(f.view!!, message!!, BaseTransientBottomBar.LENGTH_LONG).show()
*/
        if (getCurrentFragment<BaseFragment>() != null) {
            hideKeyboard()
            showSnackBar(message!!)
            // Snackbar.make(getCurrentFragment().getView(), message, BaseTransientBottomBar.LENGTH_SHORT).show();
            // getCurrentFragment().getView().setBackgroundColor(ContextCompat.getColor(this, R.color.themeColor))

        }

    }


    fun showSnackBar(message: String) {
        showSnackBar1(message)
    }

    fun showSnackBar(message: String, showOk: Boolean) {
        showSnackBar(findViewById(R.id.container), message, showOk)
    }

    fun showSnackBar(view: View, message: String, showOk: Boolean) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        val sbView = snackBar.view
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        textView.typeface = ResourcesCompat.getFont(this, R.font.montserrat_medium)
        sbView.setBackgroundResource(R.color.colorAccent)

        if (showOk)
            snackBar.setAction("Ok") { snackBar.dismiss() }

        snackBar.show()
    }

    private fun showSnackBar1(message: String) {
        //  hideKeyBoard();
        if (getCurrentFragment<BaseFragment>()!!.view != null) {

            val snackbar = Snackbar.make(getCurrentFragment<BaseFragment>()?.view!!, message, Snackbar.LENGTH_SHORT)
            snackbar.setActionTextColor(this.resources.getColor(R.color.white))
            snackbar.setAction("Ok") { snackbar.dismiss() }
            val snackView = snackbar.view
            val textView = snackView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            textView.maxLines = 4
            textView.setTextColor(this.resources.getColor(R.color.white))

            snackView.setBackgroundResource(R.color.colorAccent)
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
                if (progressDialog == null /*&& this!= null*/) {
                    try {

                        progressDialog = MyCustomProgressDialog()


                    } catch (e: Exception) {
                        //e.printStackTrace();
                    }

                }
                if (progressDialog != null && !progressDialog!!.isShown/* && getActivity() != null*/) {
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

    override fun onBackPressed() {
        hideKeyboard()


        val currentFragment = getCurrentFragment<BaseFragment>()
        if (currentFragment == null)
            super.onBackPressed()
        else if (currentFragment is ReceiptFragment || currentFragment is DriverComingFragment || currentFragment is RideFragment || currentFragment is RatingFragment || currentFragment is CarAllocationFragment)
        else if (currentFragment is StartFragment)
            finish()
        else if (currentFragment is HomeStartFragment) {
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

    fun setFortCallBackFectory(): FortCallBackManager {
        fortCallback = FortCallBackManager.Factory.create()
        return fortCallback!!
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


    fun showDialogWithOneAction(title: String?, message: String?,
                                positiveButton: String?,
                                positiveFunction: (DialogInterface, Int) -> Unit) {
        showDialogWithTwoActions(title, message, positiveButton, null, positiveFunction) { _, _ -> }
    }

    fun showDialogWithTwoActions(title: String?, message: String?,
                                 positiveName: String?, negativeName: String?,
                                 positiveFunction: (DialogInterface, Int) -> Unit,
                                 negativeFunction: (DialogInterface, Int) -> Unit) {
        alertDialog = AlertDialog.Builder(this)
                .setCancelable(false)
                .create()
        alertDialog.setMessage(message)
        alertDialog.setTitle(title)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveName, positiveFunction)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeName, negativeFunction)
        alertDialog.show()
        val textView = alertDialog.findViewById<TextView>(android.R.id.message)
        if (textView != null) {
            textView.typeface = ResourcesCompat.getFont(this, R.font.montserrat_regular)
        }
    }

    fun hideDialog() {
        if (alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (fortCallback != null) {
            fortCallback!!.onActivityResult(requestCode, resultCode, data)
        }

        if (getCurrentFragment<BaseFragment>() != null) {
            getCurrentFragment<BaseFragment>()!!.onActivityResult(requestCode, resultCode, data)
        }

    }

    /* override fun showSettingsDialog(title: String?, message: String?, isCancelable: Boolean,
                                     positiveButtonName: String?, negativeButtonName: String?,
                                     positiveFunction: (DialogInterface, Int) -> Unit,
                                     negativeFunction: (DialogInterface, Int) -> Unit) {
         val builder = AlertDialog.Builder(this)
         builder.setTitle(title)
         builder.setMessage(message)
         builder.setCancelable(isCancelable)
         builder.setPositiveButton(positiveButtonName, positiveFunction)
         builder.setNegativeButton(negativeButtonName, negativeFunction)
         settingsDialog = builder.create()
         val textView = settingsDialog.findViewById<TextView>(android.R.id.message)
         if (textView != null)
             textView.typeface = ResourcesCompat.getFont(this, R.font.montserrat_regular)
         settingsDialog.show()
     }

     override fun hideSettingsDialog() {
         if (settingsDialog.isShowing) {
             settingsDialog.dismiss()
         }
     }

     override fun openSettingsScreen(message: String) {
         val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
         val uri = Uri.fromParts("package", packageName, null)
         intent.data = uri
         Toast.makeText(this, message, Toast.LENGTH_LONG).show()
         startActivityForResult(intent, REQUEST_CODE_PERMISSION)
     }*/


    override fun <T : BaseFragment> load(tClass: Class<T>): FragmentActionPerformer<T> {
        return navigationFactory.make(tClass)
    }

    override fun loadActivity(aClass: Class<out BaseActivity>): ActivityBuilder {
        return activityStarter.make(aClass)
    }

    override fun <T : BaseFragment> loadActivity(aClass: Class<out BaseActivity>, pageTClass: Class<T>): ActivityBuilder {
        return activityStarter.make(aClass).setPage(pageTClass)
    }


    override fun goBack() {
        onBackPressed()
    }


    override fun openDrawer() {

    }

    override fun closeDrawer() {

    }
}
