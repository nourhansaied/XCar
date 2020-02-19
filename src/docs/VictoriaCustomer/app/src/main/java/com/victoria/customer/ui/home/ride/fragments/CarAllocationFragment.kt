package com.victoria.customer.ui.home.ride.fragments

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.victoria.customer.R
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_car_allocation.*


class CarAllocationFragment : BaseFragment() {

    private lateinit var zoomin: Animation
    private lateinit var zoomOut: Animation

    override fun createLayout(): Int {
        return R.layout.fragment_car_allocation
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        startAnimation(frameLayoutCenter)
        textViewCancel.setOnClickListener(this::onViewClick)

    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.textViewCancel -> {

                //navigator.load(DriverComingFragment::class.java).replace(false)

                navigator.load(CancelRideFragment::class.java).replace(true)
            }


        }
    }


    private fun startAnimation(marker: FrameLayout) {

        zoomin = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
        zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)

        marker.animation = zoomin
        marker.animation = zoomOut

        this.zoomin.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(arg0: Animation) {}

            override fun onAnimationRepeat(arg0: Animation) {}

            override fun onAnimationEnd(arg0: Animation) {
                marker.startAnimation(zoomOut)
            }
        })
        this.zoomOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(arg0: Animation) {}

            override fun onAnimationRepeat(arg0: Animation) {}

            override fun onAnimationEnd(arg0: Animation) {
                marker.startAnimation(zoomin)
            }
        })


    }
}
