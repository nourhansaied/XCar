package com.victoria.customer.ui.home.fragments

import android.support.v4.view.ViewPager
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.ride.adapter.ViewPagerAdapter
import com.victoria.customer.ui.home.ride_history.fragments.PastRideFragment
import com.victoria.customer.ui.home.ride_history.fragments.UpcomingRideFragment
import kotlinx.android.synthetic.main.fragment_my_ride_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*

class MyRidesFragment : BaseFragment() {

    override fun createLayout(): Int {
        return R.layout.fragment_my_ride_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        toolBarText.text = getString(R.string.toolbar_title_my_rides)
        imageViewMenu.setImageResource(R.drawable.arrow_back)

        imageViewMenu.setOnClickListener(this::onViewClick)
        setupViewPager(viewPagerRideHistory)

        viewPagerRideHistory.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    viewPagerRideHistory.currentItem = 0
                    radioUpcomingRide.isChecked = true

                } else if (position == 1) {
                    viewPagerRideHistory.currentItem = 1
                    radioPastRide.isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        radioGroupMyRide.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.radioUpcomingRide) {
                viewPagerRideHistory.currentItem = 0

            } else if (checkedId == R.id.radioPastRide) {

                viewPagerRideHistory.currentItem = 1

            }
        }
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.imageViewMenu -> {
                navigator.load(HomeStartFragment::class.java).replace(false)
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFrag(UpcomingRideFragment())
        adapter.addFrag(PastRideFragment())
        viewPager.adapter = adapter
        viewPager.currentItem = 0
    }
}
