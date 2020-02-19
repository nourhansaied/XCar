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
import com.victoria.customer.ui.home.ride_history.adapter.PastRideAdapter
import com.victoria.customer.ui.interfaces.ItemClickListener
import com.victoria.customer.ui.manager.Passable
import com.victoria.customer.util.advance_adapter.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_past_ride_layout.*


class PastRideFragment : BaseFragment() {

    lateinit var list: ArrayList<PastRide>
    lateinit var adapter: PastRideAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    var currentPage = 0
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

                endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        val parameter = Parameter()
                        currentPage = page
                        parameter.page = page
                        parameter.user_type = Common.CUSTOMER
                        homeActivityViewModel.pastRideApiCall(parameter)
                    }
                }
                /*recyclerViewRequestJob.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {

                }
            });*/
                recycleViewRideHistory.addOnScrollListener(endlessRecyclerViewScrollListener)
            }, 300)
        } else {
            handler.removeCallbacksAndMessages(null)
        }
    }

    override fun bindData() {
        list = ArrayList()
        linearLayoutManager = LinearLayoutManager(context)
        setAdapter()
        observePastRideApiCalling()

    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {


        }
    }

    private fun setAdapter() {
        val adapter = PastRideAdapter(this!!.context!!, list, ItemClickListener { t, pos ->
            run {
                navigator.load(PastRideDetailFragment::class.java).hasData(object : Passable<PastRideDetailFragment> {
                    override fun passData(t: PastRideDetailFragment) {
                        t.passRideDetail(list[pos])
                    }

                }).replace(true)
            }
        })
        this.adapter = adapter
        recycleViewRideHistory.layoutManager = linearLayoutManager
        recycleViewRideHistory.adapter = adapter

    }

    private fun observePastRideApiCalling() {
        homeActivityViewModel.pastRideList.observe(this, onChange = {
            if (it.responseCode == 1) {
                list.addAll(it.data!!)
                adapter.items = list
            } else if (it.responseCode == 2) {
                adapter.errorMessage = it.message
            }
        }, onError = {
            true
        })
    }
}
