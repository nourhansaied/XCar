package com.victoria.driver.ui.home.dialog_fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.victoria.driver.R
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.di.App
import kotlinx.android.synthetic.main.dialog_ride_request.*


@SuppressLint("ValidFragment")
/**
 * Created by hlink53 on 19/5/16.
 */
class RideRequestPopup @SuppressLint("ValidFragment") constructor
(var callbackForRide: CallBackForRideNavigation, var appWaitingTime: String, var appPreferences: AppPreferences) : DialogFragment() {
    private lateinit var myCountDownTimer: MyCountDownTimer
    var count = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_ride_request, container, false)
        ButterKnife.bind(this, view)
        val wmlp = dialog.window!!.attributes
        wmlp.gravity = Gravity.FILL_HORIZONTAL
        dialog.window!!.requestFeature(STYLE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun onStart() {
        super.onStart()
        val params = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageViewDone!!.setOnClickListener {
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_TIME_TO_CONTINUE, "")
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_PROGRESS, "")
            myCountDownTimer.cancel()
            callbackForRide.rideNavigationCallBack(true)
            dismissAllowingStateLoss()
        }
        imageViewClose!!.setOnClickListener {
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_TIME_TO_CONTINUE, "")
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_PROGRESS, "")
            myCountDownTimer.cancel()
            callbackForRide.rideNavigationCallBack(false)
            dismissAllowingStateLoss()
        }


        textViewRideDateTime.text = App.getTripData().customerData.name.toString()
        textViewRideType.text = App.getTripData().ride_type.toString() + " Ride"
        textViewPickup.text = App.getTripData().pickupAddress
        textViewDropoff.text = App.getTripData().dropoffAddress
        textViewEstimateCost.text = getString(R.string.label_curruncy_sar) + " " +
                App.getTripData().totalAmount /*+ "-" + (App.getTripData().totalAmount.toInt() + 10).toString()*/
        progressBar.progress = appWaitingTime.toInt() * 1000

        var totalTime: Long
        if (appPreferences.getString(com.victoria.driver.core.Common.TRIP_TIME_TO_CONTINUE) == "") {

            totalTime = appWaitingTime.toLong() * 1000
            count = appWaitingTime.toInt()
        } else {
            totalTime = appPreferences.getString(com.victoria.driver.core.Common.TRIP_TIME_TO_CONTINUE).toLong()
            count = appPreferences.getString(com.victoria.driver.core.Common.TRIP_PROGRESS).toInt()
        }
        myCountDownTimer = MyCountDownTimer(totalTime, 1000)
        myCountDownTimer.start()


    }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        private var totalCount = (millisInFuture / 1000).toInt()
        private var incrementValue = 100.0f / totalCount
        private var totalIncrement = (incrementValue * 100)

        var progress = totalIncrement

        override fun onTick(millisUntilFinished: Long) {

            count -= 1
            if (count >= 0) {
                progressBar.progress = count as Int * 100 / (30000 / 1000)
                textCounter.text = count.toString()
            }
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_PROGRESS, count.toString())
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_TIME_TO_CONTINUE, millisUntilFinished.toString())

        }

        override fun onFinish() {
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_TIME_TO_CONTINUE, "")
            appPreferences.putString(com.victoria.driver.core.Common.TRIP_PROGRESS, "")
            textCounter.text = getString(R.string._0)
            progressBar.progress = 0
            callbackForRide.dissmissAfterTimesUp()
            dismissAllowingStateLoss()


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        myCountDownTimer.cancel()
    }

    interface CallBackForRideNavigation {
        fun rideNavigationCallBack(isAccept: Boolean)
        fun dissmissAfterTimesUp()
    }
}
