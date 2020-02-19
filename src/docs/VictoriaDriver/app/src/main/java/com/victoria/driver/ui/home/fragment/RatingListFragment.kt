package com.victoria.driver.ui.home.fragment


import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.RateReviewData
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.adapter.RatingListAdapter
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.advance_adapter.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_notification_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*


class RatingListFragment : BaseFragment() {

    lateinit var list: ArrayList<RateReviewData>
    lateinit var adapter: RatingListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun createLayout(): Int {
        return R.layout.fragment_notification_layout
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
        toolBarText.text = getString(R.string.toolbar_title_rating_list)
        //observeNotificationResponse(page)

        imageViewMenu.setImageResource(R.drawable.arrow_back)

        imageViewMenu.setOnClickListener(this::onViewClick)

        setAdapter()

        recycleViewNotification.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                observeRatingListResponse(page)
            }
        })
    }

    private fun observeRatingListResponse(page: Int) {
        homeActivityViewModel.rateReviewListLiveData.observe(this, onChange = {
            homeActivityViewModel.rateReviewListLiveData.removeObservers(this)
            if (it.responseCode == 1) {
                list.addAll(it.data!!)
                adapter.items = list
                adapter.notifyDataSetChanged()
            } else if (it.responseCode == 2) {
                adapter.errorMessage = it.message
            }
        }, onError = {
            true
        })
        val parameter = Parameter()
        parameter.page = page
        parameter.user_type = Common.DRIVER

        homeActivityViewModel.getRateReviewList(parameter)
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.imageViewMenu -> {
                //openDrawer()
               navigator.goBack()
            }

        }
    }

    private fun setAdapter() {

        val adapter = RatingListAdapter(context!!, list)
        this.adapter = adapter
        recycleViewNotification.layoutManager = linearLayoutManager
        recycleViewNotification.adapter = adapter
        //adapter.submitList(list)
    }
}
