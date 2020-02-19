package com.victoria.driver.ui.home.fragment

import android.arch.lifecycle.ViewModelProviders
import android.graphics.RectF
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.victoria.customer.data.URLFactory.ResponseCode.SUCCESS
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.DatewiseEarning
import com.victoria.driver.data.pojo.EarningList
import com.victoria.driver.data.pojo.MonthlyList
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.EditProfileViewModel
import com.victoria.driver.util.CalendarUtils
import com.victoria.driver.util.DateTimeUtility
import com.victoria.driver.util.MyMarkerView
import kotlinx.android.synthetic.main.fragment_earnings.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class EarningsFragment : BaseFragment() {

    private var scroll: Boolean = true
    private var weekDigit: Int = 1
    protected var mOnValueSelectedRectF = RectF()
    private var whichClick: String = "week"
    private var value: String = ""
    private var subValue: String = ""
    var mv: MyMarkerView? = null

    @Inject
    lateinit var validator: Validator
    private var calendarUtil: CalendarUtils? = null
    internal lateinit var currentLocale: Locale
    private var currentYear: Int = 0
    internal lateinit var dataSet: BarDataSet
    private var barData: BarData? = null
    private lateinit var weekDaysList: MutableList<String>
    internal lateinit var weeklyList: MutableList<String>

    override fun createLayout(): Int = com.victoria.driver.R.layout.fragment_earnings

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val viewModel: EditProfileViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[EditProfileViewModel::class.java]
    }

    override fun bindData() {
        toolBarText.text = getString(com.victoria.driver.R.string.earning)

        imageViewMenu.setImageResource(com.victoria.driver.R.drawable.arrow_back)
        textViewEarningsWeekly.setOnClickListener { onViewClick(it) }
        textViewEarningsWeeklySelected.setOnClickListener { onViewClick(it) }
        textViewEarningsMonthly.setOnClickListener { onViewClick(it) }
        textViewEarningsMonthlySelected.setOnClickListener { onViewClick(it) }
        textViewEarningsYearly.setOnClickListener { onViewClick(it) }
        textViewEarningsYearlySelected.setOnClickListener { onViewClick(it) }
        imageViewMenu.setOnClickListener { onViewClick(it) }
        mv = MyMarkerView(activity, com.victoria.driver.R.layout.custome_info_window)

        this.currentLocale = Locale.getDefault()
        this.calendarUtil = CalendarUtils(this.currentLocale)
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR)
        this.dataSet = BarDataSet(ArrayList(), "Earnings")

        this.dataSet.color = context?.let { ContextCompat.getColor(context!!, com.victoria.driver.R.color.text_blue) }!!
        this.chart.setPinchZoom(false)
        this.chart.isDoubleTapToZoomEnabled = false
        this.chart.setScaleEnabled(false)
        this.chart.setDrawGridBackground(false)
        this.chart.getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        val yAxisLeft = this.chart.getAxis(YAxis.AxisDependency.LEFT)
        yAxisLeft.setDrawAxisLine(false)
        yAxisLeft.yOffset = 6.0f
        yAxisLeft.textSize = resources.getDimensionPixelSize(com.victoria.driver.R.dimen.sp_4).toFloat()
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        yAxisLeft.setDrawGridLines(true)
        val xAxis = this.chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = resources.getDimensionPixelSize(com.victoria.driver.R.dimen.sp_4).toFloat()
        this.chart.animateY(600)
        this.chart.invalidate()



        this.chart.extraBottomOffset = 10f
        //this.chart.getLegend().setEnabled(false);

        val description = Description()
        description.text = ""
        this.chart.description = description    // Hide the description

        this.dataSet.highLightColor = ContextCompat.getColor(context!!, com.victoria.driver.R.color.text_pink)
        this.dataSet.highLightAlpha = 255

        this.chart.legend.isEnabled = false

        this.chart.isHighlightPerTapEnabled = true


        this.barData = BarData(this.dataSet)
        this.barData!!.setDrawValues(false)
        barData!!.barWidth = 0.4f
        this.chart.data = this.barData
        calendar = Calendar.getInstance(TimeZone.getDefault())

        month = ArrayList<String>()

        currentMonthList = ArrayList()
        weeklyList = ArrayList()
        weekDaysList = ArrayList()



        mv!!.chartView = chart // For bounds control
        chart.marker = mv
        xAxis.yOffset = 10f
        chart.extraBottomOffset = 10f
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                mv!!.setValue(0f)
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                mv!!.setValue(e?.y!!)
            }
        })

        /*MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custome_info_window);
        mv.setChartView(chart);
        chart.setMarker(mv);*/

        setWeekly()

        tabLayoutSelection.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                var pos = tab.position
                if (whichClick == "week") {
                    weekDigit = tab.position
                    if (pos == 0)
                        value = getDateTime(true) + "-" + getMonth(weekDaysList[pos].subSequence(3, 6).toString()) + "-" + weekDaysList[pos].subSequence(0, 2)
                    else
                        value = getDateTime(false) + "-" + getMonth(weekDaysList[pos].subSequence(3, 6).toString()) + "-" + weekDaysList[pos].subSequence(0, 2)
                    subValue = getDateTime(false) + "-" + getMonth(weekDaysList[pos].subSequence(12, 15).toString()) + "-" + weekDaysList[pos].subSequence(9, 11)
                    //weeklyEarningAdapter?.setSelectedPosition(pos)
                    scroll = false
                    observeResponse(false)

                    //setGraphDataForWeek(tab.position)
                } else if (whichClick == "month") {
                    value = (currentMonthList as ArrayList<String>)[pos].split(" ")[0]
                    scroll = false
                    observeResponse(false)
                    //setGraphDataForMonth(tab.position)
                } else {
                    value = (currentMonthList as ArrayList<String>)[(currentMonthList as ArrayList<String>).size - 1]
                    scroll = false
                    observeResponseYearVice(false)
                    //setGraphDataForYear()
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


    }


    override fun onViewClick(view: View) {

        when (view.id) {
            com.victoria.driver.R.id.textViewEarningsWeekly -> {
                whichClick = "week"
                val autoTransition = AutoTransition()
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsYearlySelected, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsMonthlySelected, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsMonthly, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsYearly, true)
                TransitionManager.beginDelayedTransition(linearLayoutMain, autoTransition)

                if (textViewEarningsWeekly.visibility == View.VISIBLE) {

                    textViewEarningsWeeklySelected.visibility = View.VISIBLE
                    textViewEarningsWeekly.visibility = View.GONE
                    textViewEarningsYearlySelected.visibility = View.GONE
                    textViewEarningsMonthlySelected.visibility = View.GONE
                    textViewEarningsMonthly.visibility = View.VISIBLE
                    textViewEarningsYearly.visibility = View.VISIBLE

                    setWeekly()

                }
            }

            com.victoria.driver.R.id.textViewEarningsWeeklySelected -> {

                if (textViewEarningsWeeklySelected.visibility == View.VISIBLE) {

                    /*textViewEarningsMonthlySelected.visibility = View.GONE
                    textViewEarningsYearlySelected.visibility = View.GONE*/
                }
            }

            com.victoria.driver.R.id.textViewEarningsMonthly -> {
                whichClick = "month"
                val autoTransition = AutoTransition()
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsYearlySelected, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsWeeklySelected, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsWeekly, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsYearly, true)

                TransitionManager.beginDelayedTransition(linearLayoutMain, autoTransition)
                if (textViewEarningsMonthly.visibility == View.VISIBLE) {
                    setMonthly()

                    textViewEarningsMonthlySelected.visibility = View.VISIBLE
                    textViewEarningsMonthly.visibility = View.GONE
                    textViewEarningsWeeklySelected.visibility = View.GONE
                    textViewEarningsYearlySelected.visibility = View.GONE
                    textViewEarningsWeekly.visibility = View.VISIBLE
                    textViewEarningsYearly.visibility = View.VISIBLE
                }
            }

            com.victoria.driver.R.id.textViewEarningsMonthlySelected -> {
                if (textViewEarningsMonthlySelected.visibility == View.VISIBLE) {
                    /*textViewEarningsYearlySelected.visibility = View.GONE
                    textViewEarningsWeeklySelected.visibility = View.GONE*/
                }
            }

            com.victoria.driver.R.id.textViewEarningsYearly -> {
                whichClick = "year"
                val autoTransition = AutoTransition()
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsMonthly, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsWeeklySelected, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsWeekly, true)
                autoTransition.excludeTarget(com.victoria.driver.R.id.textViewEarningsMonthlySelected, true)

                TransitionManager.beginDelayedTransition(linearLayoutMain, autoTransition)

                if (textViewEarningsYearly.visibility == View.VISIBLE) {
                    setYearly()

                    textViewEarningsYearlySelected.visibility = View.VISIBLE
                    textViewEarningsYearly.visibility = View.GONE
                    textViewEarningsWeeklySelected.visibility = View.GONE
                    textViewEarningsMonthlySelected.visibility = View.GONE
                    textViewEarningsMonthly.visibility = View.VISIBLE
                    textViewEarningsWeekly.visibility = View.VISIBLE
                }
            }

            com.victoria.driver.R.id.textViewEarningsYearlySelected -> {
                if (textViewEarningsYearlySelected.visibility == View.VISIBLE) {

                    /*textViewEarningsWeeklySelected.visibility = View.GONE
                    textViewEarningsMonthlySelected.visibility = View.GONE*/
                }
            }

            com.victoria.driver.R.id.imageViewMenu -> {
                navigator.load(HomeFragment::class.java).replace(false)

                //openDrawer()
            }
        }
    }

    private var calendar: Calendar? = null
    private fun setWeekly() {

        weeklyList.clear()


        //    this.textViewLabel.setText(getString(string._this) + " " + getString(string.Week));
        //    this.tabLayoutDate.removeAllTabs();


        /*val year = calendar!!.get(1)
        val totalWeeks = calendar!!.get(3)

        calendar!!.set(1, year)
        val monthList = this.calendarUtil!!.monthList
        var j = 1
        while (j <= totalWeeks) {
            var title = ""
            var k = 0
            while (k < 2) {
                val str: String

                calendar!!.clear()

                calendar!!.set(1, year)
                calendar!!.set(3, j)
                var day = calendar!!.get(5)
                val name = monthList[calendar!!.get(2)]
                if (k == 1) {
                    day--
                    if (day == 0) {
                        day = calendar!!.getMaximum(5)
                    }
                }
                val number = NumberFormat.getInstance().format(day.toLong())
                val append = StringBuilder().append(title)
                if (k == 0) {
                    str = ""
                } else {
                    str = " - "
                }
                title = append.append(str).append(number).append(" ").append(name).toString()
                k++
                j++
            }



            weeklyList.add(title)

            j--
        }*/


        setViewPager(getWeekList(weekDaysList) as ArrayList<String>)
        weekDigit = getWeekList(weekDaysList).size - 1
        //tabLayoutSelection.scrollToPosition(weeklyList.size - 1)
        value = getDateTime(true) + "-" + getMonth(weekDaysList[weekDaysList.size - 1].subSequence(3, 6).toString()) + "-" + weekDaysList[weekDaysList.size - 1].subSequence(0, 2)
        subValue = getDateTime(false) + "-" + getMonth(weekDaysList[weekDaysList.size - 1].subSequence(12, 15).toString()) + "-" + weekDaysList[weekDaysList.size - 1].subSequence(9, 11)
        scroll = true
        observeResponse(true)

        //setGraphDataForWeek(getWeekList(weekDaysList).size - 1)
    }

    private fun observeResponse(scroll: Boolean) {
        if (mv != null)
            mv?.setValue(0f)
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.getdriverreport.value = null
        viewModel.getdriverreportApi(getParams())
        viewModel.getdriverreport.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            if (responseBody.responseCode == SUCCESS) {
                if (whichClick == "month") {

                    Handler().postDelayed({
                        activity?.runOnUiThread(Runnable()
                        {
                            run()
                            {
                                if (this.scroll) {
                                    tabLayoutSelection.scrollX = tabLayoutSelection.width
                                    currentMonthList?.size?.minus(1)?.let { tabLayoutSelection.getTabAt(it)?.select() }
                                    tabLayoutSelection.getTabAt(currentMonthList?.size?.minus(1)!!)
                                }
                            }
                            /*
                            textViewTotalCompletedTrip.text = responseBody.data?.totalCompletedRide.toString()
                            textViewTotalCanceledRide.text = responseBody.data?.totalCancelledRide.toString()
                            textViewTotalTrip.text = ((responseBody.data?.totalTrip?.toInt()?.plus(responseBody.data?.totalCancelledRide?.toInt()!!)).toString())*/
                        })
                    }, 500)
                    setGraphDataForMonth(value.toInt(), responseBody.data?.datewiseEarnings!!)
                    //responseBody.data?.reportData?.let { setChartDataMonthly(it) }
                } else {
                    Handler().postDelayed({
                        activity?.runOnUiThread(Runnable()
                        {
                            run()
                            {
                                if (this.scroll) {
                                    tabLayoutSelection.scrollX = tabLayoutSelection.width
                                    tabLayoutSelection.getTabAt(weekDaysList.size - 1)?.select()
                                    tabLayoutSelection.getTabAt(currentMonthList?.size?.minus(1)!!)
                                }
                            }
                            /*
                            textViewTotalCompletedTrip.text = responseBody.data?.totalCompletedRide.toString()
                            textViewTotalCanceledRide.text = responseBody.data?.totalCancelledRide.toString()
                            textViewTotalTrip.text = ((responseBody.data?.totalTrip?.toInt()?.plus(responseBody.data?.totalCancelledRide?.toInt()!!)).toString())*/
                        })
                    }, 500)
                    setGraphDataForWeek(weekDigit, responseBody.data?.datewiseEarnings!!)
                    //responseBody.data?.reportData?.let { setChartDataWeekly(it) }
                }
                textViewTotalAmount.text = responseBody.data.totalEarning.toString()


                /*
                textViewTotalCompletedTrip.text = responseBody.data?.totalCompletedRide.toString()
                textViewTotalCanceledRide.text = responseBody.data?.totalCancelledRide.toString()
                textViewTotalTrip.text = ((responseBody.data?.totalTrip?.toInt()?.plus(responseBody.data?.totalCancelledRide?.toInt()!!)).toString())*/
            }
        }, {
            viewModel.getdriverreport.removeObservers(this)
            navigator.toggleLoader(true)
            true
        })
    }

    private fun observeResponseYearVice(scroll: Boolean) {
        if (mv != null)
            mv?.setValue(0f)
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.getdriverreportYearApi.value = null
        viewModel.getdriverreportYearApi(getParams())
        viewModel.getdriverreportYearApi.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            if (responseBody.responseCode == SUCCESS) {
                textViewTotalAmount.text = responseBody.data?.totalEarning.toString()

                Handler().postDelayed({
                    activity?.runOnUiThread(Runnable()
                    {
                        run()
                        {
                            if (this.scroll) {
                                tabLayoutSelection.scrollX = tabLayoutSelection.width
                                currentMonthList?.size?.minus(1)?.let { tabLayoutSelection.getTabAt(it)?.select() }
                                tabLayoutSelection.getTabAt(currentMonthList?.size?.minus(1)!!)
                            }
                        }
                        /*
                        textViewTotalCompletedTrip.text = responseBody.data?.totalCompletedRide.toString()
                        textViewTotalCanceledRide.text = responseBody.data?.totalCancelledRide.toString()
                        textViewTotalTrip.text = ((responseBody.data?.totalTrip?.toInt()?.plus(responseBody.data?.totalCancelledRide?.toInt()!!)).toString())*/
                    })
                }, 500)
                setGraphDataForYear(responseBody.data?.datewiseEarnings!!)

            }
        }, {
            viewModel.getdriverreportYearApi.removeObservers(this)
            navigator.toggleLoader(true)
            true
        })
    }

    private fun getParams(): Parameter {
        val parameter = Parameter()
        if (whichClick == "year") {
            parameter.year = value
        } else if (whichClick == "month") {
            parameter.startDate = getDateTime(false) + "-" + value + "-" + 1.toString()
            parameter.endDate = lastDate(value)
        } else {
            parameter.startDate = value
            parameter.endDate = subValue
        }
        return parameter

    }

    fun lastDate(monthDigit: String): String {

        if ((monthDigit.toInt() % 2) == 0 && monthDigit.toInt() < 8) {
            if (monthDigit.toInt() != 2) {
                return getDateTime(false) + "-" + monthDigit + "-" + 30.toString()
            } else {
                if (((getDateTime(false).toInt() % 4 == 0) && (getDateTime(false).toInt() % 100 != 0)) || (getDateTime(false).toInt() % 400 == 0))
                    return getDateTime(false) + "-" + monthDigit + "-" + 29.toString()
                else
                    return getDateTime(false) + "-" + monthDigit + "-" + 28.toString()

            }
            // number is odd
        } else if ((monthDigit.toInt() % 2) == 0 && monthDigit.toInt() >= 8) {
            return getDateTime(false) + "-" + monthDigit + "-" + 31.toString()
        } else {
            return if (monthDigit.toInt() >= 8) getDateTime(false) + "-" + monthDigit + "-" + 30.toString()
            else getDateTime(false) + "-" + monthDigit + "-" + 31.toString()
        }
    }

    private fun getDateTime(isPr: Boolean): String {
        val dateFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
        val date = Date()
        if (isPr && weekDaysList[0].subSequence(3, 6).equals("Dec")) {
            return (dateFormat.format(date).toInt() - 1).toString()
        } else
        /*if (isPr)

        else*/
            return dateFormat.format(date)
    }

    fun getMonth(monthName: String): String {
        val date = SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthName)
        val cal = Calendar.getInstance()
        cal.time = date
        if (date.month + 1 < 10)
            return "0" + (date.month + 1).toString()
        else
            return (date.month + 1).toString()
    }

    private fun getWeekList(mList: MutableList<String>): MutableList<String> {
        mList.clear()
        /*var calendar = Calendar.getInstance(Locale.getDefault())

         val year = calendar.get(1)
         val totalWeeks = calendar.get(3)
         var currentLocale = Locale.getDefault()
         var calendarUtil = CalendarUtils(currentLocale)
         calendar.set(1, year)
         val monthList = calendarUtil.getMonthList()
         var j = 1
         while (j <= totalWeeks) {
             var title = ""
             var k = 0
             while (k < 2) {
                 val str: String

                 calendar.clear()

                 calendar.set(1, year)
                 calendar.set(3, j)
                 var day = calendar.get(5)
                 val name = monthList[calendar.get(2)]
                 if (k == 1) {
                     day--
                     if (day == 0) {
                         day = calendar.getMaximum(5)
                     }
                 }
                 val number = NumberFormat.getInstance().format(day.toLong())
                 val append = StringBuilder().append(title)
                 if (k == 0) {
                     str = ""
                 } else {
                     str = " - "
                 }
                 if (number.toInt() < 10)
                     title = append.append(str).append("0$number").append(" ").append(name).toString()
                 else
                     title = append.append(str).append(number).append(" ").append(name).toString()
                 k++
                 j++*/
        val instance = Calendar.getInstance()
        //instance.firstDayOfWeek = Calendar.MONDAY
        instance.set(Calendar.MONTH, Calendar.JANUARY)
        //instance.add(Calendar.DAY_OF_WEEK, (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) * -1)

        instance.set(Calendar.YEAR, Calendar.JANUARY, Calendar.DATE)
        instance.set(Calendar.WEEK_OF_YEAR, 1)
        instance.set(Calendar.DAY_OF_MONTH, 1)
        var isCurrent = false
        while (!isCurrent) {
            val stringBuilder = StringBuilder()
            stringBuilder.append(DateTimeUtility.format(instance.time, "dd MMM"))
            instance.add(Calendar.DAY_OF_WEEK, 6)
            stringBuilder.append(" - " + DateTimeUtility.format(instance.time, "dd MMM"))
            mList.add(stringBuilder.toString().trim { it <= ' ' })
            isCurrent = instance.get(Calendar.MONTH) >= Calendar.getInstance().get(Calendar.MONTH) && instance.get(Calendar.DAY_OF_MONTH) >= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            instance.add(Calendar.DAY_OF_WEEK, 1)
        }
        return mList

        /*val instance = Calendar.getInstance()
        instance.firstDayOfWeek = Calendar.MONDAY
        instance.set(Calendar.MONTH, Calendar.JANUARY)
        instance.add(Calendar.DAY_OF_WEEK, (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) * -1)
        instance.set(Calendar.DATE, 1)
        instance.set(Calendar.WEEK_OF_YEAR, 1)
        var isCurrent = false
        while (!isCurrent) {
            val stringBuilder = StringBuilder()
            stringBuilder.append(DateTimeUtility.format(instance.time, "dd MMM"))
            instance.add(Calendar.DAY_OF_WEEK, 6)
            stringBuilder.append(" - " + DateTimeUtility.format(instance.time, "dd MMM"))
            mList.add(stringBuilder.toString().trim { it <= ' ' })
            isCurrent = instance.get(Calendar.MONTH) >= Calendar.getInstance().get(Calendar.MONTH) && instance.get(Calendar.DAY_OF_MONTH) >= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            instance.add(Calendar.DAY_OF_WEEK, 1)
        }*/
    }

    lateinit
    var entries: List<BarEntry>
    private lateinit var labels: List<String>

    private fun getMonthList(mList: ArrayList<String>): ArrayList<String> {
        mList.clear()
        val instance = Calendar.getInstance()
        instance.set(Calendar.MONTH, 0)
        var isCurrent = false
        while (!isCurrent) {
            var monthlyList = MonthlyList()
            monthlyList.name = DateTimeUtility.format(instance.time, "MMM").toUpperCase()
            monthlyList.id = DateTimeUtility.format(instance.time, "MM")
            mList.add(monthlyList.id.toString() + " " + monthlyList.name.toString())
            isCurrent = Calendar.getInstance().get(Calendar.MONTH) == instance.get(Calendar.MONTH)
            instance.add(Calendar.MONTH, 1)
        }
        return mList
    }

    fun setGraphDataForWeek(week: Int, datewiseEarnings: MutableList<DatewiseEarning>) {

        //weekDaysList.clear()
        val calendar = Calendar.getInstance(this.currentLocale)
        var i: Int

        val year = calendar.get(1)
        calendar.clear()
        calendar.set(1, year)
        calendar.set(3, week)
        this.dataSet.clear()

        entries = ArrayList()

        labels = ArrayList()
        val weekList = this.calendarUtil!!.weekList


        i = 0
        while (i <= datewiseEarnings.size - 1) {
            (entries as ArrayList<BarEntry>).add(BarEntry((i).toFloat(), datewiseEarnings.get(i).driverEarning.toFloat()))
            (labels as ArrayList<String>).add(weekList[i])
            //weekDaysList.add(weekList[i - 1])
            i++
        }
        /*if (2 > 1) {
            i = 1
            while (i < 2) {
                (entries as ArrayList<BarEntry>).add(BarEntry((i + 5).toFloat(), datewiseEarnings.get(i).driverEarning.toFloat()))
                (labels as ArrayList<String>).add(weekList[i - 1])
                //weekDaysList.add(weekList[i - 1])
                i++
            }
        }*/

        this.dataSet.values = entries
/*
        this.dataSet.color = ContextCompat.getColor(this!!.context!!, R.color.text_blue)
*/
        this.dataSet.notifyDataSetChanged()
        this.barData!!.notifyDataChanged()
        this.chart.xAxis.valueFormatter = IndexAxisValueFormatter(label(getWeeklyChartData(value, subValue)))
        this.chart.notifyDataSetChanged()
        this.chart.animateY(600)
        this.chart.animateX(600)
        this.chart.invalidate()

    }

    fun label(yearlySpend: List<EarningList>): List<String> {
        val strings = java.util.ArrayList<String>()

        for (spendItem in yearlySpend) {
            val date = DateTimeUtility.parseDate(spendItem.date, "y-M-d")

            if (whichClick == "year") strings.add((DateTimeUtility.format(date, "MMM")).toUpperCase())
            else strings.add((DateTimeUtility.format(date, "EEE")).toUpperCase())
        }
        return strings
    }

    fun getWeeklyChartData(value: String, subValue: String): List<EarningList> {
        val weeklySpend = ArrayList<EarningList>()
        var dates = getDates(value, subValue)
        for (i in getDates(value, subValue).indices) {
            weeklySpend.add(EarningList(dates[i], "200"))
        }

        return weeklySpend
    }

    private fun getDates(dateString1: String, dateString2: String): List<String> {
        val dates = ArrayList<String>()
        val df1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        var date1: Date? = null
        var date2: Date? = null

        try {
            date1 = df1.parse(dateString1)
            date2 = df1.parse(dateString2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal1 = Calendar.getInstance()
        cal1.time = date1


        val cal2 = Calendar.getInstance()
        cal2.time = date2

        while (!cal1.after(cal2)) {
            dates.add(df1.format(cal1.time))
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }

    fun setGraphDataForMonth(month: Int, datewiseEarnings: MutableList<DatewiseEarning>) {

        val currentYear = calendar!!.get(Calendar.YEAR)
        val year = currentYear
        calendar!!.set(year, month, 1)

        // Calendar calendar = Calendar.getInstance(this.currentLocale);
        // calendar.set(2, month - 1);
        val actualMaximum = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        this.dataSet.clear()
        entries = ArrayList()
        labels = ArrayList()

        for (i in 1 until datewiseEarnings.size - 1) {
            (entries as ArrayList<BarEntry>).add(BarEntry(i.toFloat(), datewiseEarnings[i].driverEarning.toFloat()))
            (labels as ArrayList<String>).add(NumberFormat.getInstance().format((i + 1).toLong()))
        }
        this.dataSet.values = entries
        this.dataSet.notifyDataSetChanged()
        /*this.dataSet.color = ContextCompat.getColor(this!!.context!!, R.color.text_blue)*/

        this.barData!!.notifyDataChanged()
        this.chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        this.chart.notifyDataSetChanged()
        this.chart.animateY(600)
        this.chart.animateX(600)
        //this.chart.setWillNotDraw(true)
        this.chart.invalidate()
    }

    private lateinit var month: List<String>


    private var currentMonthList: List<String>? = null

    private fun setViewPager(listing: ArrayList<String>) {

        tabLayoutSelection.removeAllTabs()
        currentMonthList = listing

        for (i in listing.indices) {
            if (whichClick == "month")
                tabLayoutSelection.addTab(tabLayoutSelection.newTab().setText(listing[i].split(" ")[1]))
            else
                tabLayoutSelection.addTab(tabLayoutSelection.newTab().setText(listing[i]))

            val tabsView = (tabLayoutSelection.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tabsView.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 10, 0)

            /*if (i == listing.size - 1) {

                val tab = tabLayoutSelection.getTabAt(listing.size - 1)
                if (tab != null) {
                    tab.select()
                }
            }*/
        }
    }

    private fun setMonthly() {
        currentMonthList = ArrayList<String>()
        /*val calendar = Calendar.getInstance(this.currentLocale)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        calendar.clear()
        calendar.set(Calendar.YEAR, year)
        val monthList = this.calendarUtil!!.getMonthList()
        for (i in 0 until month) {
            val name = monthList[i]
            (currentMonthList as ArrayList<String>).add(name)
        }*/

        setViewPager(getMonthList(currentMonthList as ArrayList<String>))
        value = (currentMonthList as ArrayList<String>)[(currentMonthList as ArrayList<String>).size - 1].split(" ")[0]
        scroll = true
        observeResponse(true)
        //setGraphDataForMonth(month)

    }

    private fun setYearly() {

        currentMonthList = ArrayList<String>()
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val year = currentYear
        (currentMonthList as ArrayList<String>).add(year.toString())
        setViewPager(getYearList(currentMonthList as ArrayList<String>))
        value = (currentMonthList as ArrayList<String>)[(currentMonthList as ArrayList<String>).size - 1]
        scroll = true
        observeResponseYearVice(true)
        //setGraphDataForYear()

    }

    private fun getYearList(mList: ArrayList<String>): ArrayList<String> {
        mList.clear()
        val instance = Calendar.getInstance()
        instance.time = DateTimeUtility.parseDate("2019-07-21 01:20:20")
        var isCurrent = false

        while (!isCurrent) {
            mList.add(DateTimeUtility.format(instance.time, "yyyy"))
            isCurrent = Calendar.getInstance().get(Calendar.YEAR) == instance.get(Calendar.YEAR)
            instance.add(Calendar.YEAR, 1)
        }
        return mList
    }

    fun setGraphDataForYear(datewiseEarnings: MutableList<DatewiseEarning>) {

        month = ArrayList<String>()

        val currentYear = calendar!!.get(Calendar.YEAR)
        val year = currentYear
        val daysInMonth = calendar!!.getActualMaximum(Calendar.MONTH) + 1

        calendar!!.set(year, daysInMonth, 1)

        for (i in 1 until daysInMonth) {

            calendar!!.set(Calendar.MONTH, i)

            (month as ArrayList<String>).add(calendar!!.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()))


        }



        entries = java.util.ArrayList()

        labels = java.util.ArrayList()

        this.dataSet.clear()

        val monthList = this.calendarUtil!!.monthList
        for (i in datewiseEarnings.indices) {
            (entries as java.util.ArrayList<BarEntry>).add(BarEntry(i.toFloat(), datewiseEarnings[i].driverEarning.toFloat()))
            (labels as java.util.ArrayList<String>).add(monthList[i])
        }

        this.dataSet.values = entries
        this.dataSet.notifyDataSetChanged()
        /*this.dataSet.color = ContextCompat.getColor(this!!.context!!, R.color.text_blue)*/

        this.barData!!.notifyDataChanged()
        this.chart.xAxis.valueFormatter = IndexAxisValueFormatter(label(getYearlyChartData()))
        this.chart.notifyDataSetChanged()
        this.chart.animateY(600)
        this.chart.animateX(600)
        this.chart.invalidate()

    }

    fun getYearlyChartData(): List<EarningList> {
        val yearlySpend = ArrayList<EarningList>()
        yearlySpend.add(EarningList("2019-01-01", "200"))
        yearlySpend.add(EarningList("2019-02-01", "400"))
        yearlySpend.add(EarningList("2019-03-01", "100"))
        yearlySpend.add(EarningList("2019-04-01", "20"))
        yearlySpend.add(EarningList("2019-05-01", "150"))
        yearlySpend.add(EarningList("2019-06-01", "170"))
        yearlySpend.add(EarningList("2019-07-01", "50"))
        yearlySpend.add(EarningList("2019-08-01", "20"))
        yearlySpend.add(EarningList("2019-09-01", "10"))
        yearlySpend.add(EarningList("2019-10-01", "100"))
        yearlySpend.add(EarningList("2019-11-01", "200"))
        yearlySpend.add(EarningList("2019-12-01", "300"))
        return yearlySpend
    }
}