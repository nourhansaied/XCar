package com.victoria.customer.ui.home.ride_history.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.PastRide
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.ride_history.adapter.UpcomingRideAdapter
import com.victoria.customer.ui.interfaces.ItemClickListener
import com.victoria.customer.ui.manager.Passable
import com.victoria.customer.util.advance_adapter.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_past_ride_layout.*


class UpcomingRideFragment : BaseFragment() {

    lateinit var list: ArrayList<PastRide>
    lateinit var adapter: UpcomingRideAdapter

    lateinit var linearLayoutManager: LinearLayoutManager

    private var currentPage: Int = 0
    internal lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override fun createLayout(): Int {
        return R.layout.fragment_past_ride_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        list = ArrayList()
        val handler = Handler()

        if (isVisibleToUser) {
            handler.postDelayed({
                setAdapter()
                if (recycleViewRideHistory != null) {
                    endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                        override fun onLoadMore(page: Int, totalItemsCount: Int) {
                            val parameter = Parameter()
                            currentPage = page
                            parameter.page = page
                            parameter.user_type = Common.CUSTOMER
                            homeActivityViewModel.upcomingRide(parameter)
                        }
                    }
                    /*recyclerViewRequestJob.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

            }
        });*/
                    recycleViewRideHistory.addOnScrollListener(endlessRecyclerViewScrollListener)
                }
            }, 300)
        } else {
            handler.removeCallbacksAndMessages(null)
        }

    }

    override fun bindData() {
        list = ArrayList()
        linearLayoutManager = LinearLayoutManager(context)

        setAdapter()
        observeUpcomingRideApiCalling()


        /*recycleViewRideHistory.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                val parameter = Parameter()
                currentPage = page
                parameter.page = page
                parameter.user_type = Common.CUSTOMER
                homeActivityViewModel.upcomingRide(parameter)
            }
        })*/
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

        }
    }

    private fun setAdapter() {
        if (recycleViewRideHistory != null) {
            val adapter = UpcomingRideAdapter(context, list, ItemClickListener { t, pos ->
                run {
                    navigator.load(UpcomingRideDetailFragment::class.java).hasData(object : Passable<UpcomingRideDetailFragment> {
                        override fun passData(t: UpcomingRideDetailFragment) {
                            t.passRideDetail(list[pos])
                        }

                    }).replace(true)
                }
            })
            this.adapter = adapter
            recycleViewRideHistory.layoutManager = linearLayoutManager
            recycleViewRideHistory.adapter = adapter
        }
    }


    private fun observeUpcomingRideApiCalling() {
        homeActivityViewModel.pastRideList.observe(this, onChange = {
            if (it.responseCode == 1) {
                list.addAll(it.data!!)
                //adapter.notifyDataSetChanged()
                adapter.items = list
            } else if (it.responseCode == 2) {
                adapter.errorMessage = it.message
            }
        }, onError = {
            true
        })
    }
}
