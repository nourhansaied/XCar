package com.victoria.customer.ui.home.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.VictoriaVehicleData
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.adapter.BottomSheetAdapter
import com.victoria.customer.ui.interfaces.ItemEventListener
import kotlinx.android.synthetic.main.dialog_service_type_layout.*


class ServiceFragment : BaseFragment() {

    lateinit var services: List<VictoriaVehicleData>
    //lateinit var services: List<Service>

    lateinit var title: String

    lateinit var bundle: Bundle
    //var location

    override fun createLayout(): Int {
        return R.layout.dialog_service_type_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        recycleViewServiceType.setHasFixedSize(true)
        recycleViewServiceType.layoutManager = LinearLayoutManager(activity?.applicationContext)

        textViewService.text = title;
        imageViewClose.setOnClickListener(this::onViewClick)
        //if(services!=null) {
        setAdapter(services)
        //}

    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewClose -> {

                navigator.goBack()
            }

        }
    }


    fun setAdapter(list: List<VictoriaVehicleData>/*list:List<Service>*/) {
        val bottomSheetAdapter = BottomSheetAdapter(activity, list, ItemEventListener { t, pos ->

            navigator.load(FareEstimationFragment::class.java).replace(true)

        })

        imageViewClose.setOnClickListener { navigator.goBack() }
        recycleViewServiceType.layoutManager = LinearLayoutManager(activity?.applicationContext)
        recycleViewServiceType.adapter = bottomSheetAdapter

    }

}
