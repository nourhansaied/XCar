package com.victoria.customer.ui.manager;


import android.os.Bundle
import android.support.annotation.UiThread
import android.view.View

@UiThread
interface FragmentActionPerformer<T> {

    fun add(toBackStack: Boolean)

    fun add(toBackStack: Boolean, tag: String)

    fun replace(toBackStack: Boolean)

    fun replace(toBackStack: Boolean, tag: String)

    fun setBundle(bundle: Bundle): FragmentActionPerformer<*>

    fun addSharedElements(sharedElements: List<Pair<View, String>>): FragmentActionPerformer<*>

    fun clearHistory(tag: String): FragmentActionPerformer<*>

    fun clearHistory():FragmentActionPerformer<*>

    fun hasData(passable: Passable<T>): FragmentActionPerformer<*>

}