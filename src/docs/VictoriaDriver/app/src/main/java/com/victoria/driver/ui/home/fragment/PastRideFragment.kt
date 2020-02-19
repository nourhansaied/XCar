package com.victoria.driver.ui.home.fragment


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.PastRide
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.adapter.PastRideAdapter
import com.victoria.driver.ui.interfaces.ItemClickListener
import com.victoria.driver.ui.manager.Passable
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.advance_adapter.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_past_ride_layout.*


class PastRideFragment : BaseFragment() {


    lateinit var list: ArrayList<PastRide>
    lateinit var adapter: PastRideAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun createLayout(): Int {
        return R.layout.fragment_past_ride_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun bindData() {
        list = ArrayList()
        linearLayoutManager = LinearLayoutManager(context)

        setAdapter()

        recycleViewRideHistory.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                observePastRideApiCalling(page)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_past_ride_layout, container, false)
    }


    override fun onViewClick(view: View) {
        when (view.id) {

        }
    }

    private fun setAdapter() {
        val adapter = PastRideAdapter(context!!, list,
                ItemClickListener { t, pos ->
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

    private fun observePastRideApiCalling(page: Int) {
        homeActivityViewModel.pastRideList.observe(this, onChange = {
            homeActivityViewModel.pastRideList.removeObservers(this)
            if (it.responseCode == 1) {
                list.addAll(it.data!!)
                adapter.items = list
                adapter.notifyDataSetChanged()
            } else if (it.responseCode == 2) {
                adapter.errorMessage = ""
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.page = page
        parameter.user_type = Common.DRIVER
        homeActivityViewModel.pastRideApiCall(parameter)
    }
}
