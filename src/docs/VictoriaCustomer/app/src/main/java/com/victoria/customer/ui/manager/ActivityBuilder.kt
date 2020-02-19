package com.victoria.customer.ui.manager;


import android.os.Bundle
import android.support.annotation.UiThread
import android.view.View
import com.victoria.customer.ui.base.BaseFragment

@UiThread
interface ActivityBuilder {

    fun start()

    fun addBundle(bundle: Bundle): ActivityBuilder

    fun addSharedElements(pairs: List<Pair<View, String>>): ActivityBuilder

    fun byFinishingCurrent(): ActivityBuilder

    fun byFinishingAll(): ActivityBuilder

    fun <T : BaseFragment> setPage(page: Class<T>): ActivityBuilder

    fun forResult(requestCode: Int): ActivityBuilder

    fun shouldAnimate(isAnimate: Boolean): ActivityBuilder


}
