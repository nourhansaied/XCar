package com.victoria.driver.ui.manager

import com.victoria.driver.ui.base.BaseActivity
import com.victoria.driver.ui.base.BaseFragment


/**
 * Created by hlink21 on 29/5/17.
 */

interface Navigator {

    fun <T : BaseFragment> load(tClass: Class<T>): FragmentActionPerformer<T>

    fun loadActivity(aClass: Class<out BaseActivity>): ActivityBuilder

    fun <T : BaseFragment> loadActivity(aClass: Class<out BaseActivity>, pageTClass: Class<T>): ActivityBuilder

    fun goBack()

    fun finish()

    fun closeDrawer()

    fun openDrawer()

    fun toggleLoader(b: Boolean)

//    fun showErrorMessage(message: String)

}
