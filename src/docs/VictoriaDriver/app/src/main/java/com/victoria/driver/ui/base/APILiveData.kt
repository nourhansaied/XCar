package com.victoria.driver.ui.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.victoria.driver.data.pojo.DataWrapper
import com.victoria.driver.data.pojo.ResponseBody

class APILiveData<T> : MutableLiveData<DataWrapper<T>>() {


    /**
     *  @param owner : Life Cycle Owner
     *  @param onChange : live data
     *  @param onError : Server and RideWithMe error -> return true to handle error by base else return false to handle error by your self
     *
     */
    /*public fun observe(
            owner: BaseFragment,
            onChange: (ResponseBody<T>) -> Unit,
            onError: (Throwable) -> Boolean = { true }
    ) {
        super.observe(owner, Observer<DataWrapper<T>> {
            if (it?.throwable != null) {
                if (onError(it.throwable)) owner.onError(it.throwable)
            } else if (it?.responseBody != null) {
                onChange(it.responseBody)
            }
        })
    }

*/
    fun observe(owner: BaseActivity,
                onChange: (ResponseBody<T>) -> Unit,
                onError: (Throwable, ResponseBody<T>?) -> Unit = { _, _ -> }) {
        super.observe(owner, Observer<DataWrapper<T>> {
            if (it?.throwable != null) {
                owner.toggleLoader(false)
                onError(it.throwable, it.responseBody)
            } else if (it?.responseBody != null) {
                onChange(it?.responseBody)
            }
        })
    }

    public fun observe(
            owner: BaseFragment,
            onChange: (ResponseBody<T>) -> Unit,
            onError: (Throwable) -> Boolean = { true }, liveData: Boolean = true
    ) {
        super.observe(owner, Observer<DataWrapper<T>> {
            if (it?.throwable != null) {
                if (!it.apiCall) {
                    if (onError(it.throwable)) owner.onError(it.throwable)
                    it.apiCall = liveData
                }
            } else if (it?.responseBody != null) {
                if (!it.apiCall) {
                    onChange(it.responseBody)
                    it.apiCall = liveData
                }
            }
        })
    }

    public fun observeOwner(owner: BaseFragment, onChange: (ResponseBody<T>) -> Unit, onError: (Throwable, ResponseBody<T>?) -> Boolean = { _, _ -> true }) {
        super.observe(owner.viewLifecycleOwner, Observer<DataWrapper<T>> {
            if (it?.throwable != null) {
                if (onError(it.throwable, it.responseBody)) owner.onError(it.throwable)
            } else if (it?.responseBody != null) {
                onChange(it?.responseBody)
            }
        })
    }


}