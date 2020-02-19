package com.victoria.driver.ui.home.fragment

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.di.App
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.adapter.CancelRideAdapter
import com.victoria.driver.ui.interfaces.ItemClickListener
import com.victoria.driver.ui.model.CancelReason
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.CancelRideViewModel
import kotlinx.android.synthetic.main.fragment_cancel_ride_layout.*


class CancelRideFragment : BaseFragment() {

    lateinit var list: ArrayList<CancelReason>
    lateinit var adapter: CancelRideAdapter

    private var selectedReason: String = ""

    override fun createLayout(): Int {
        return R.layout.fragment_cancel_ride_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val cancelRideViewModel: CancelRideViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CancelRideViewModel::class.java]
    }

    override fun bindData() {
        list = ArrayList()
        imageViewClose.setOnClickListener(this::onViewClick)
        buttonCancel.setOnClickListener(this::onViewClick)

        observeCancelRideList()
        navigator.toggleLoader(true)
        cancelRideViewModel.cancelRideReasons()

        observeCancelRide()
    }

    private fun getParamForCancelRide(): Parameter {
        val parameter = Parameter()
        if (arguments != null) {
            parameter.tripId = arguments!!.getString(Common.TRIP_ID)
        } else {
            parameter.tripId = App.getTripData().tripId.toString()
        }

        parameter.reasonId = list[adapter.selectedPosition].reasonId.toString()
        return parameter
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewClose -> {
                navigator.goBack()
            }

            R.id.buttonCancel -> {
                if (adapter.selectedPosition != -1 && selectedReason == getString(R.string.cancel_reason_5)) {

                    if (editTextCommentBox.text!!.isNotEmpty()) {
                        navigator.toggleLoader(true)
                        cancelRideViewModel.cancelRide(getParamForCancelRide())
                        /*showMessage(getString(R.string.success_message_cancel))
                        navigator.load(HomeStartFragment::class.java).clearHistory("").replace(false)*/

                    } else {
                        showMessage(getString(R.string.validation_empty_cancellation_reason))
                    }

                } else if (adapter.selectedPosition == -1) {
                    showMessage(getString(R.string.validation_empty_reason))

                    /*showSnackBar(getString(R.string.success_message_cancel))
                    navigator.load(HomeStartFragment::class.java).clearHistory("").replace(false)*/
                } else {
                    navigator.toggleLoader(true)
                    cancelRideViewModel.cancelRide(getParamForCancelRide())
                }
            }
        }
    }

    private fun setAdapter() {

        val adapter = CancelRideAdapter(this.context!!, list, ItemClickListener { t, _ ->
            selectedReason = t
            if (t == getString(R.string.cancel_reason_5)) {
                editTextCommentBox.visibility = View.VISIBLE
            } else {
                editTextCommentBox.visibility = View.GONE
            }
        })

        this.adapter = adapter
        recycleViewCancelReason.layoutManager = LinearLayoutManager(context)
        recycleViewCancelReason.adapter = adapter

    }

    private fun observeCancelRideList() {
        cancelRideViewModel.cancelRideReason.observe(this, onChange = {
            navigator.toggleLoader(false)
            if (it.responseCode == 1) {
                list.addAll(it.data!!)
                setAdapter()
            } else {
                showMessage(it.message)
            }

        }, onError = {

            true
        })
    }

    private fun observeCancelRide() {
        cancelRideViewModel.cancelRide.observe(this, onChange = {
            navigator.toggleLoader(false)
            showMessage(it.message)
            if (it.responseCode == 1) {
                showMessage(it.message)
                navigator.load(HomeFragment::class.java).clearHistory("").replace(false)
            }
        }, onError = {

            true
        })
    }
}
