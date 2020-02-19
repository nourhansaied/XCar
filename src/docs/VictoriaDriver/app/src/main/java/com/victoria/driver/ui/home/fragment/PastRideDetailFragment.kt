package com.victoria.driver.ui.home.fragment

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import com.squareup.picasso.Picasso
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.PastRide
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.CircleTransform
import com.victoria.driver.util.DateTimeFormatter
import com.victoria.driver.util.Spanny
import kotlinx.android.synthetic.main.fragment_completed_ride_details_linear_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*

class PastRideDetailFragment : BaseFragment() {

    lateinit var pastRide: PastRide

    override fun createLayout(): Int {
        return R.layout.fragment_completed_ride_details_linear_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun bindData() {
        observeUserDetailResponse()
        toolBarText.text = getString(R.string.toolbar_title_past_ride_detail)
        imageViewBack.setOnClickListener(this::onViewClick)
    }

    private fun setUserData(data: User?) {
        textViewRideDateTime.text = DateTimeFormatter.utcToLocal(pastRide.tripdatetime, Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_OUT_THREE)
        textViewTotalCost.text = getString(R.string.label_curruncy_sar) + " " + pastRide.totalAmount
        textViewPickup.text = pastRide.pickupAddress
        textViewDropoff.text = pastRide.dropoffAddress
        textViewEstimatedKm.text = pastRide.distance.toString() + " km"
        textViewEstimatedDuration.text = pastRide.totalTime.toString() + " min"

        textViewDriverName.text = data?.name
        Picasso.get()
                .load(data?.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewDriver)

        textViewLabelPhone.movementMethod = LinkMovementMethod.getInstance()
        val spannableText = Spanny(getString(R.string.phone))
                .append(" " + data?.phone, ForegroundColorSpan(resources.getColor(R.color.text_blue)), StyleSpan(Typeface.BOLD), RelativeSizeSpan(1.01f), 0, 5, 0)

        textViewLabelPhone.text = spannableText

        //textViewLabelPhone.text =
        textViewRating.text = (String.format("%.2f", data?.rating))
    }

    override fun onViewClick(view: View) {
        when (view.id) {
            R.id.imageViewBack -> {
                activity!!.onBackPressed()
            }
        }
    }

    fun passRideDetail(pastRide: PastRide) {
        this.pastRide = pastRide
    }

    private fun observeUserDetailResponse() {
        homeActivityViewModel.userData.observe(this, onChange = {
            navigator.toggleLoader(false)
            homeActivityViewModel.userData.removeObservers(this)
            if (it.responseCode == 1) {
                setUserData(it.data)
            } else {
                showMessage(it.message)
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.user_type = Common.CUSTOMER
        parameter.userId = pastRide.customerId.toString()
        navigator.toggleLoader(true)
        homeActivityViewModel.userDetailApiCall(parameter)
    }
}
