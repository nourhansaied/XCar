package com.victoria.driver.ui.home.fragment


import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.victoria.customer.data.URLFactory
import com.victoria.driver.R
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.PaymentHistory
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.exception.ApplicationException
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.adapter.PaymentHistoryAdapter
import com.victoria.driver.ui.home.adapter.TransactionHistoryAdapter
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.EditProfileViewModel
import com.victoria.driver.util.DateUtipPicker
import com.victoria.driver.util.advance_adapter.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_transaction_history_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import kotlinx.android.synthetic.main.toolbar_with_menu.toolBarText
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class TransactionHistoryFragment : BaseFragment() {

    lateinit var list: ArrayList<PaymentHistory>
    lateinit var paymentHistoryAdapter: PaymentHistoryAdapter
    var startDate: String = ""
    var endDate: String = ""
    private var fillDate = 1
    private var minimumDate = Calendar.getInstance()
    private var maxDate = Calendar.getInstance()

    @Inject
    lateinit var validator: Validator

    override fun createLayout(): Int {
        return R.layout.fragment_transaction_history_layout
    }

    private val viewModel: EditProfileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[EditProfileViewModel::class.java]
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        toolBarText.text = getString(R.string.toolbar_transaction_history)
        imageViewMenu.setImageResource(R.drawable.arrow_back)
        imageViewMenu.setOnClickListener(this::onViewClick)
        toolbarRightArrow.setOnClickListener(this::onViewClick)
        textViewStartDate.setOnClickListener(this::onViewClick)
        textViewEndDate.setOnClickListener(this::onViewClick)
        imageViewApply.setOnClickListener(this::onViewClick)
        setAdapter()
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.imageViewMenu -> {
                navigator.goBack()
            }

            R.id.imageViewApply -> {
                isValidated()

            }

            R.id.textViewStartDate -> {
                textViewEndDate.text = ""
                fillDate = 1
                createDialogWithoutDateField().show(activity?.fragmentManager, "Date")
                /*var cal = Calendar.getInstance()

                DialogUtils.openDatePicker(activity!!, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd MMM,yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                    textViewStartDate.text = sdf.format(cal.time)
                }, null)*/
            }

            R.id.textViewEndDate -> {
                fillDate = 2
                createDialogWithoutDateField().show(activity?.fragmentManager, "Date")

                /*var cal = Calendar.getInstance()
                DialogUtils.openDatePicker(activity!!, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd MMM,yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
                    textViewEndDate.text = sdf.format(cal.time)

                }, null)*/
            }
        }
    }

    private fun isValidated() {
        try {
            validator.submit(textViewStartDate)
                    .checkEmpty()
                    .errorMessage(R.string.validation_empty_start_date)
                    .check()

            validator.submit(textViewEndDate)
                    .checkEmpty()
                    .errorMessage(R.string.validation_select_end_date)
                    .check()

            list = ArrayList()
            paymentHistoryAdapter.items = list

            val parameter = Parameter()
            parameter.page = 1
            parameter.startDate = startDate
            parameter.endDate = endDate
            observeResponse(parameter)

        } catch (e: ApplicationException) {
            e.printStackTrace()
            showMessage(e.message)
        }
    }

    private fun setAdapter() {
        list = ArrayList()
        var linearLayoutManager = LinearLayoutManager(context)
        val adapter = context?.let { PaymentHistoryAdapter(it, list) }
        paymentHistoryAdapter = adapter!!
        recyclerViewHistory.layoutManager = linearLayoutManager
        recyclerViewHistory.adapter = adapter
        recyclerViewHistory.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                val parameter = Parameter()
                parameter.page = page
                parameter.startDate = startDate
                parameter.endDate = endDate
                observeResponse(parameter)
            }
        })
    }

    private fun createDialogWithoutDateField(): com.wdullaer.materialdatetimepicker.date.DatePickerDialog {
        val dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance { _,
                                                                                          selectedYear, monthOfYear, dayOfMonth ->
            val newDate = Calendar.getInstance()
            /**
             * Add one day to current date
             */


            newDate.add(Calendar.DAY_OF_MONTH, 1)
            newDate.set(selectedYear, monthOfYear, dayOfMonth)

            var month = (monthOfYear + 1).toString()

            if (month.toInt() < 10)
                month = "0" + month
            else
                month = month

            if (fillDate == 1) {
                startDate = "$selectedYear-$month-$dayOfMonth"
                textViewEndDate.isEnabled = true
                textViewEndDate.alpha = 1.0f
                textViewStartDate.text = DateUtipPicker.instance.parseDateForDisplay("$selectedYear-$month-$dayOfMonth")
            } else {
                endDate = "$selectedYear-$month-$dayOfMonth"
                textViewEndDate.text = DateUtipPicker.instance.parseDateForDisplay("$selectedYear-$month-$dayOfMonth")
            }
            if (fillDate != 2) {
                minimumDate.set(selectedYear, monthOfYear, dayOfMonth)
            }

        }

        if (fillDate == 2) {
            dpd.minDate = minimumDate
        }
        dpd.maxDate = maxDate

        return dpd
    }

    fun observeResponse(parameter: Parameter) {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.paymentHistoryList.value = null
        viewModel.paymentHistoryList.removeObservers(this)
        //user_id,
        viewModel.paymentHistoryListApi(parameter)
        viewModel.paymentHistoryList.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            //showToastMessage(context!!, responseBody.message)
            if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
                relativeLayoutNoData.visibility = View.GONE
                recyclerViewHistory.visibility = View.VISIBLE
                list.addAll(responseBody.data!!)
                paymentHistoryAdapter.items = list
                paymentHistoryAdapter.notifyDataSetChanged()

            } else if (responseBody.responseCode == 2) {
                if (list.isEmpty()) {
                    textViewNoData.text = responseBody.message
                    relativeLayoutNoData.visibility = View.VISIBLE
                    recyclerViewHistory.visibility = View.GONE
                    paymentHistoryAdapter.isLoading = false
                    paymentHistoryAdapter.isNoData = true
                    paymentHistoryAdapter.notifyDataSetChanged()
                } else {
                    relativeLayoutNoData.visibility = View.GONE
                    recyclerViewHistory.visibility = View.VISIBLE
                    paymentHistoryAdapter.errorMessage = ""
                }
            }
        }, {
            viewModel.paymentHistoryList.removeObservers(this)
            navigator.toggleLoader(false)
            true
        })
    }

}
