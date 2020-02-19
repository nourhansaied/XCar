package com.victoria.customer.ui.home.ride.fragments

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.CancelReason
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.fragments.HomeStartFragment
import com.victoria.customer.ui.home.ride.adapter.CancelRideAdapter
import com.victoria.customer.ui.home.ride.viewmodel.CancelRideViewModel
import com.victoria.customer.ui.interfaces.ItemClickListener
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
            parameter.tripId = VictoriaCustomer.getTripData().tripId.toString()
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
                        showSnackBar(getString(R.string.validation_empty_cancellation_reason))
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

        val adapter = CancelRideAdapter(context, list, ItemClickListener { t, pos ->
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
                navigator.load(HomeStartFragment::class.java).clearHistory("").replace(false)
            }
        }, onError = {

            true
        })
    }
}
