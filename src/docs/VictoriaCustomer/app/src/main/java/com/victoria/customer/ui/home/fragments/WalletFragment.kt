package com.victoria.customer.ui.home.fragments

import android.arch.lifecycle.ViewModelProviders
import android.view.View

import com.victoria.customer.R
import com.victoria.customer.data.URLFactory.ResponseCode.SUCCESS
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.SignInViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.ride.viewmodel.RideViewModel
import com.victoria.customer.ui.home.viewmodel.FareEstimationViewModel
import kotlinx.android.synthetic.main.fragment_wallet_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*


class WalletFragment : BaseFragment() {

    override fun createLayout(): Int {
        return R.layout.fragment_wallet_layout_new
    }

    private val viewModel: FareEstimationViewModel  by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[FareEstimationViewModel::class.java]
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        toolBarText.text = getString(R.string.toolbar_title_wallet)
        observeResponse(Parameter())
        imageViewMenu.setImageResource(R.drawable.arrow_back)
        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewAddAmount.setOnClickListener(this::onViewClick)
        imageViewTransaction.setOnClickListener(this::onViewClick)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewMenu -> {
                navigator.load(HomeStartFragment::class.java).replace(false)
                //openDrawer()
            }

            R.id.imageViewAddAmount -> {
                navigator.load(AddAmountFragment::class.java).replace(true)
            }

            R.id.imageViewTransaction -> {
                navigator.load(TransactionHistoryFragment::class.java).replace(true)
            }
        }
    }

    private fun observeResponse(parameter: Parameter) {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.getwalletamount.value = null
        viewModel.getwalletamount(parameter)
        viewModel.getwalletamount.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            if (responseBody.responseCode == SUCCESS) {
                textViewWalletBalance.text = String.format("%.2f", responseBody.data?.wallet?.toDouble())
            }
        })
    }
}
