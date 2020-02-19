package com.victoria.customer.ui.home.ride.dialog


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.*
import com.victoria.customer.R
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.ui.home.ride.fragments.ChatMessageListingFragment
import com.victoria.customer.ui.interfaces.ISelectCountry
import com.victoria.customer.ui.interfaces.ItemClickEventListener
import kotlinx.android.synthetic.main.dialog_driver_arrived_layout.view.*


class DriverArrivedDialog : DialogFragment() {


    lateinit var imageViewClose: AppCompatImageView
    private var callback: ISelectCountry? = null
    private var callback2: ItemClickEventListener? = null

    fun setCallback(callback: ItemClickEventListener) {
        this.callback2 = callback
    }

    fun setCallback(callback: ISelectCountry) {
        this.callback = callback
    }

    override fun onStart() {
        super.onStart()
        val params = dialog.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // dialog.window!!.setBackgroundDrawable(resources.getDrawable(R.drawable.drawable_background_with_corner))
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_driver_arrived_layout, container, false)
        val wmlp = dialog.window?.attributes
        wmlp?.gravity = Gravity.CENTER
        if (VictoriaCustomer.getTripData().customerData.name != null) {
            view.textViewUserName.text = "Hello, " + VictoriaCustomer.getTripData().customerData.name
            view.textViewDriverCarType.text = VictoriaCustomer.getTripData().driverData.vehicleModel +
                    " - " + VictoriaCustomer.getTripData().vehicleData.vehicle +
                    " - " + VictoriaCustomer.getTripData().driverData.vehicleNumber
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        imageViewClose = view.findViewById(R.id.imageViewClose)
        (view.findViewById(R.id.textViewCall) as AppCompatTextView).setOnClickListener {
            if (isPermissionGranted()) {
                callAction()
            }
        }
        (view.findViewById(R.id.textViewMessage) as AppCompatTextView).setOnClickListener {
            if (callback2 != null)
                callback2?.onItemEventFired()
        }
        isCancelable = false
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* Handler().postDelayed({

             callback?.selectCountry("","",CountryData());
             dismiss()

         },2000)*/


        imageViewClose.setOnClickListener {

            //callback?.selectCountry("","",CountryData());
            //dismiss()
        }
    }

    private fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this!!.activity!!, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                return true
            } else {

                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdLog.v("TAG", "Permission is granted")
            return true
        }
    }

    private fun callAction() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + VictoriaCustomer.getTripData().driverData.countryCode
                + VictoriaCustomer.getTripData().driverData.phone)
        startActivity(callIntent)
    }

}
