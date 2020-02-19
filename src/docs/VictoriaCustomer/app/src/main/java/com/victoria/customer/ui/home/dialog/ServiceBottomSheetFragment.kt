package com.victoria.customer.ui.home.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.DialogFragment
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.model.VictoriaVehicleData
import com.victoria.customer.ui.home.adapter.BottomSheetAdapter
import com.victoria.customer.ui.interfaces.ItemEventListener


class ServiceBottomSheetFragment : DialogFragment {

    internal lateinit var recycleViewServiceType: RecyclerView
    internal lateinit var textViewService: AppCompatTextView
    internal lateinit var constraintLayoutRoot: ConstraintLayout
    lateinit var services: List<VictoriaVehicleData>
    lateinit var title: String

    lateinit var callbackSuccess: CallbackVehicleSelection
    //Bottom Sheet Callback

    constructor()

    @SuppressLint("ValidFragment")
    constructor(callbackSuccess: CallbackVehicleSelection) {
        this.callbackSuccess = callbackSuccess
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        //super.setupDialog(dialog, style);
        //Get the content View
        val contentView = View.inflate(context, R.layout.bottom_sheet_service_list, null)
        recycleViewServiceType = contentView.findViewById(R.id.recycleViewServiceType)
        textViewService = contentView.findViewById(R.id.textViewService)
        constraintLayoutRoot = contentView.findViewById(R.id.constraintLayoutRoot)

        dialog.setContentView(contentView)

        if (dialog != null) {

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window.setLayout(width, height)
            dialog.window.setGravity(Gravity.CENTER)
            dialog.setCancelable(true)

            dialog.setCanceledOnTouchOutside(true)
        }

        recycleViewServiceType.setHasFixedSize(true)
        recycleViewServiceType.layoutManager = LinearLayoutManager(activity?.applicationContext, HORIZONTAL, false)
        constraintLayoutRoot.setOnTouchListener { view, motionEvent ->
            dismiss()
            true
        }
        textViewService.text = title
        setAdapter(services)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.full_screen_dialog)

    }


    fun setAdapter(list: List<VictoriaVehicleData>) {
        val bottomSheetAdapter = BottomSheetAdapter(activity!!, list, ItemEventListener { t, pos ->
            callbackSuccess.vehicleSelected(t)
            dismiss()

        })

        recycleViewServiceType.adapter = bottomSheetAdapter

    }

    fun setData(s: String, carTypes: List<VictoriaVehicleData>) {
        title = s
        services = carTypes

    }

    interface CallbackVehicleSelection{

        fun vehicleSelected(vehicleData:VictoriaVehicleData)
    }

}