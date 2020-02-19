package com.victoria.driver.ui.base

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar

/**
 * Created by hlink21 on 20/12/16.
 */

interface HasToolbar {

    fun setToolbar(toolbar: Toolbar)

    fun showToolbar(b: Boolean)

    fun setToolbarTitle(title: CharSequence)

    fun setToolbarTitle(@StringRes title: Int)

    fun showBackButton(b: Boolean)

    fun setToolbarColor(@ColorRes color: Int)

    fun setToolbarElevation(isVisible: Boolean)
}
